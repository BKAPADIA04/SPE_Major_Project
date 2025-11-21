import argparse
import joblib
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score

from pipeline import create_pipeline
from data_loader import load_sliding_window


def train_model(current_slice, output_path="../models/ambulance_model.pkl"):
    # Load sliding window data
    df = load_sliding_window(current_slice)

    X = df.drop("fare_amount", axis=1)
    y = df["fare_amount"]

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42
    )

    model = create_pipeline()
    model.fit(X_train, y_train)

    preds = model.predict(X_test)

    print("MSE:", mean_squared_error(y_test, preds))
    print("R2 Score:", r2_score(y_test, preds))

    with open("MLOpsPipeline/codes/train.log", "w") as f:
        f.write(f"MSE: {mean_squared_error(y_test, preds)}\n")
        f.write(f"R2 Score: {r2_score(y_test, preds)}\n")

    joblib.dump(model, output_path)
    print(f"Model saved to {output_path}")

    return model


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--slice", type=int, required=True)
    parser.add_argument("--output", type=str, default="model.pkl")
    args = parser.parse_args()

    train_model(args.slice, args.output)