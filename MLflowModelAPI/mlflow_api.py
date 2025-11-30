import mlflow
import mlflow.pyfunc
from fastapi import FastAPI
from pydantic import BaseModel
import pandas as pd
import uvicorn

# ---------------------------
# 1. Load MLflow Model
# ---------------------------
mlflow.set_tracking_uri("http://127.0.0.1:5001")
model_name = "SPE_Regression_Model"

# Get latest version
client = mlflow.tracking.MlflowClient()
versions = client.search_model_versions(f"name='{model_name}'")
latest = max(versions, key=lambda v: int(v.version))
model_uri = f"models:/{model_name}/{latest.version}"

print("Loading model:", model_uri)
model = mlflow.pyfunc.load_model(model_uri)

# ---------------------------
# 2. FastAPI App
# ---------------------------
app = FastAPI()

# This is the ONLY input ML model needs
class PricePredictionInput(BaseModel):
    pickup_datetime: str
    pickup_longitude: float
    pickup_latitude: float
    dropoff_longitude: float
    dropoff_latitude: float
    passenger_count: int

@app.post("/predict")
def predict(input: PricePredictionInput):

    df = pd.DataFrame([input.dict()])

    prediction = model.predict(df)[0]

    return {
        "predicted_fare_usd": float(prediction),
        "predicted_fare_inr": float(prediction * 89.87)
    }

# Run server
if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=6001)