import joblib
import pandas as pd

def load_model(model_path="models/ambulance_model.pkl"):
    model = joblib.load(model_path)
    return model

def predict_fare(model, input_data: dict):
    df = pd.DataFrame([input_data])
    prediction = model.predict(df)[0]
    return prediction

if __name__ == "__main__":
    model = load_model("../models/model.pkl")

    sample = {
        "pickup_datetime": "2022-05-07 14:30:00 UTC",
        "pickup_longitude": -73.985428,
        "pickup_latitude": 40.748817,
        "dropoff_longitude": -73.985656,
        "dropoff_latitude": 40.758896,
        "passenger_count": 1
    }

    fare = predict_fare(model, sample)
    print("Predicted Fare:", fare*89.876)