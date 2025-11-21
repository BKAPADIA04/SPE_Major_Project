import os
import pandas as pd
import glob

def load_sliding_window(slice_id):
    base_dir = os.path.dirname(os.path.abspath(__file__))  # codes/
    project_root = os.path.dirname(base_dir)               # MLOpsPipeline/
    data_dir = os.path.join(project_root, "data", "data_slices")

    # 1. Load all existing slices
    slice_files = glob.glob(os.path.join(data_dir, "slice_*.csv"))

    dfs = []

    for file in slice_files:
        dfs.append(pd.read_csv(file))

    # 2. Load the requested slice (append last)
    new_slice_path = os.path.join(data_dir, f"slice_{slice_id}.csv")

    if not os.path.exists(new_slice_path):
        raise FileNotFoundError(f"{new_slice_path} does not exist")

    dfs.append(pd.read_csv(new_slice_path))

    # 3. Concatenate all slices
    combined = pd.concat(dfs, ignore_index=True)

    print(combined.head(10).to_string())

    return combined
