import mlflow
import mlflow.sklearn
import joblib
import os

# Option 1: relative to script location
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
MODEL_PATH = os.path.join(SCRIPT_DIR, "../models/model.pkl")


def register_model():

    model = joblib.load(MODEL_PATH)

    mlflow.set_tracking_uri("http://127.0.0.1:5001")
    mlflow.set_experiment("SPE_Model_Training")

    with mlflow.start_run():

        mlflow.sklearn.log_model(
            sk_model=model,
            name="model",
            registered_model_name="SPE_Regression_Model"
        )

        print("Model successfully registered in MLflow Model Registry!")

if __name__ == "__main__":
    register_model()
