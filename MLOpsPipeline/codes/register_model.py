import mlflow
import mlflow.sklearn
import joblib
import os
import sys

# Path to this script
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))

# Add custom modules (pipeline.py, data_loader.py, etc.)
# sys.path.append(SCRIPT_DIR)

# Model path
MODEL_PATH = os.path.join(SCRIPT_DIR, "../models/model.pkl")

def register_model():

    model = joblib.load(MODEL_PATH)

    mlflow.set_tracking_uri("http://127.0.0.1:5001")
    mlflow.set_experiment("SPE_Model_Training")

    with mlflow.start_run():

        mlflow.sklearn.log_model(
            sk_model=model,
            name="model",               # MUST USE THIS
            registered_model_name="SPE_Regression_Model"
        )

        print("Model successfully registered!")

if __name__ == "__main__":
    register_model()
