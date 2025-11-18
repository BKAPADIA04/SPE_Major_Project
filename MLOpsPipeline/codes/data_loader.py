import pandas as pd
import os

def load_sliding_window(current_slice, base_dir="../data/data_slices", window=2):
    dfs = []

    # Calculate start slice number (can't be < 1)
    start = max(1, current_slice - window + 1)

    for i in range(start, current_slice + 1):
        path = os.path.join(base_dir, f"slice_{i}.csv")
        if not os.path.exists(path):
            raise FileNotFoundError(f"{path} does not exist")
        
        dfs.append(pd.read_csv(path))

    # Combine into one dataframe
    return pd.concat(dfs, ignore_index=True)