import mlflow
import pandas as pd

def load_model(model_name="SPE_Regression_Model", stage="Production"):
    """
    Load the latest model from MLflow Model Registry.
    """
    mlflow.set_tracking_uri("http://127.0.0.1:5001")
    model_uri = f"models:/{model_name}/{stage}"
    print("Loading model from:", model_uri)
    return mlflow.pyfunc.load_model(model_uri)


def predict(model, input_data: dict):
    df = pd.DataFrame([input_data])
    return model.predict(df)[0]


if __name__ == "__main__":
    model = load_model(stage="Production")

    sample = {
        "pickup_datetime": "2022-05-07 14:30:00 UTC",
        "pickup_longitude": -73.985428,
        "pickup_latitude": 40.748817,
        "dropoff_longitude": -73.985428,
        "dropoff_latitude": 40.748817,
        "passenger_count": 1,
    }

    fare = predict(model, sample)

    print("Predicted Fare:", fare)
    print("Fare in INR:", fare * 89.876)