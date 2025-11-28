import numpy as np
import pandas as pd
from sklearn.base import BaseEstimator, TransformerMixin
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.ensemble import GradientBoostingRegressor
from sklearn.impute import SimpleImputer

# ===== CUSTOM MAPPING =====
def map_ambulance_type(passenger_count):
    if passenger_count <= 1:
        return "BLS"
    elif passenger_count == 2:
        return "ALS"
    else:
        return "CriticalCare"


# ---------- 1. Feature Engineering ----------
class FeatureEngineer(BaseEstimator, TransformerMixin):

    def haversine_distance(self, lat1, lon1, lat2, lon2):
        R = 6371.0
        # convert to radians (work with numpy arrays / scalars)
        lat1 = np.radians(lat1)
        lon1 = np.radians(lon1)
        lat2 = np.radians(lat2)
        lon2 = np.radians(lon2)
        dlat = lat2 - lat1
        dlon = lon2 - lon1
        a = np.sin(dlat/2.0)**2 + np.cos(lat1)*np.cos(lat2)*np.sin(dlon/2.0)**2
        c = 2 * np.arcsin(np.sqrt(a))
        return R * c

    def fit(self, X, y=None):
        return self

    def transform(self, df):
        df = df.copy()
        
        

        # Ensure datetime parsed
        df["pickup_datetime"] = pd.to_datetime(df["pickup_datetime"])

        # Distance km
        df["Distance"] = self.haversine_distance(
            df["pickup_latitude"], df["pickup_longitude"],
            df["dropoff_latitude"], df["dropoff_longitude"]
        )

        # temporal
        df["weekday"] = df["pickup_datetime"].dt.weekday
        df["month"] = df["pickup_datetime"].dt.month
        df["hour"] = df["pickup_datetime"].dt.hour

        df["monthly_quarters"] = df["month"].apply(lambda x: (x - 1) // 3 + 1)
        df["hourly_quarters"] = df["hour"].apply(lambda x: x // 6)

        # ambulance mapping from passenger_count
        df["ambulance_type"] = df["passenger_count"].apply(map_ambulance_type)

        # keep original columns + new ones; downstream pipeline chooses which to use
        return df


# ---------- 2. Define final features ----------
numeric_features = [
    "pickup_longitude",
    "pickup_latitude",
    "dropoff_longitude",
    "dropoff_latitude",
    "Distance",
    "weekday",
    "monthly_quarters",
    "hourly_quarters"
]

categorical_features = ["ambulance_type"]

numeric_transformer = Pipeline(steps=[
    ("imputer", SimpleImputer(strategy="median")),
    ("scaler", StandardScaler())
])

categorical_transformer = Pipeline(steps=[
    ("imputer", SimpleImputer(strategy="most_frequent")),
    ("onehot", OneHotEncoder(handle_unknown="ignore", sparse_output=False))
])

preprocessor = ColumnTransformer(
    transformers=[
        ("num", numeric_transformer, numeric_features),
        ("cat", categorical_transformer, categorical_features)
    ],
    remainder="drop"  # drop any other columns (like raw passenger_count, pickup_datetime, month, hour)
)

# ---------- 3. Final Model Pipeline (default params, tune later) ----------
model_pipeline = Pipeline(
    steps=[
        ("features", FeatureEngineer()),
        ("preprocess", preprocessor),
        ("gbm", GradientBoostingRegressor(random_state=42))
    ]
)


def create_pipeline():
    return model_pipeline