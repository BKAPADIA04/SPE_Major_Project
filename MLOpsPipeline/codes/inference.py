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
    model = load_model("../models/slice10.pkl")
    model = load_model("../models/model.pkl")

    sample = {
        "pickup_datetime": "2022-05-07 14:30:00 UTC",
    }

    fare = predict_fare(model, sample)
    print("Predicted Fare:", fare)
    print("Predicted Fare:", fare*89.876)
    