import mlflow
import mlflow.sklearn
import joblib

# Path to your trained model
MODEL_PATH = "MLOpsPipeline/models/model.pkl"

def register_model():

    # Load your scikit-learn model from local path
    model = joblib.load(MODEL_PATH)

    # Set MLflow server tracking URI
    mlflow.set_tracking_uri("http://127.0.0.1:5001")
    mlflow.set_experiment("SPE_Model_Training")

    with mlflow.start_run():

        # Register model in MLflow Model Registry
        mlflow.sklearn.log_model(
            sk_model=model,
            artifact_path="model",
            registered_model_name="SPE_Regression_Model"   # ⭐ Model Registry Name
        )

        print("Model successfully registered in MLflow Model Registry!")

if __name__ == "__main__":
    register_model()