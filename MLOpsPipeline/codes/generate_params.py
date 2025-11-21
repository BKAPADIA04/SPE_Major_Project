import os
import yaml

# Path where slices are stored
slices_dir = "MLOpsPipeline/data/data_slices"

# Count number of slice CSVs
slice_count = len([f for f in os.listdir(slices_dir) if f.endswith(".csv")])

params = {
    "train": {
        "slice": slice_count,
        "output": "MLOpsPipeline/models/model.pkl"
    }
}

# Write params.yaml
with open("params.yaml", "w") as f:
    yaml.dump(params, f)

print(f"params.yaml updated → slice = {slice_count}")