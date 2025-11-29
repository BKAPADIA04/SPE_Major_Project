import mlflow
import pandas as pd
from mlflow.tracking import MlflowClient

MLFLOW_URI = "http://127.0.0.1:5001"
MODEL_NAME = "SPE_Regression_Model"

def load_model(model_name=MODEL_NAME):
    mlflow.set_tracking_uri(MLFLOW_URI)
    client = MlflowClient()

    versions = client.search_model_versions(f"name='{model_name}'")
    latest = max(versions, key=lambda v: int(v.version))

    model_uri = f"models:/{model_name}/{latest.version}"
    return mlflow.pyfunc.load_model(model_uri)


def predict_price(input_data: dict) -> float:
    """
    input_data example:
    {
        "pickup_latitude": 12.34,
        "pickup_longitude": 77.89,
        "dropoff_latitude": 12.56,
        "dropoff_longitude": 77.65,
        "distance_km": 5.6,
        "plateNumber": "KA01AB1234"
    }
    """

    df = pd.DataFrame([input_data])
    model = load_model()
    result = model.predict(df)[0]

    return float(result)
