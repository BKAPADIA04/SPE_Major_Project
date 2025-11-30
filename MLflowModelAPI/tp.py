import mlflow

print("Tracking URI:", mlflow.get_tracking_uri())
print("Default artifact URI:", mlflow.get_artifact_uri())
