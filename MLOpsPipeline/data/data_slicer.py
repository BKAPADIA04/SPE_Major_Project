import pandas as pd
import numpy as np
import os

def split_into_slices(input_path, output_dir="data_slices", num_slices=10):
    # Load data
    df = pd.read_csv(input_path)

    # Shuffle for randomness
    df = df.sample(frac=1, random_state=42).reset_index(drop=True)

    # Create output folder
    os.makedirs(output_dir, exist_ok=True)

    # Split into slices
    chunks = np.array_split(df, num_slices)

    slice_paths = []

    for i, chunk in enumerate(chunks, start=1):
        path = os.path.join(output_dir, f"slice_{i}.csv")
        chunk.to_csv(path, index=False)
        slice_paths.append(path)

    print(f"Created {num_slices} data slices in {output_dir}")
    return slice_paths


if __name__ == "__main__":
    split_into_slices("uber.csv")