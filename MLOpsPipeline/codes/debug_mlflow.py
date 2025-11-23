import mlflow

mlflow.set_tracking_uri("http://127.0.0.1:5001")

client = mlflow.MlflowClient()

print("\n=== Registered Models ===")
for m in client.search_registered_models():
    print(m.name)

print("\n=== Model Versions ===")
for v in client.search_model_versions("name='SPE_Regression_Model'"):
    print(v)
