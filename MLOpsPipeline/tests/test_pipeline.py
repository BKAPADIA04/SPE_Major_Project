# tests/test_feature_engineer.py
import pandas as pd
from MLOpsPipeline.codes.pipeline import FeatureEngineer, map_ambulance_type

def test_map_ambulance_type():
    assert map_ambulance_type(0) == "BLS"
    assert map_ambulance_type(1) == "BLS"
    assert map_ambulance_type(2) == "ALS"
    assert map_ambulance_type(3) == "CriticalCare"
    assert map_ambulance_type(10) == "CriticalCare"

def test_feature_engineer_outputs():
    df = pd.DataFrame([{
        "pickup_longitude": -73.985,
        "pickup_latitude": 40.7484,
        "dropoff_longitude": -73.9857,
        "dropoff_latitude": 40.7331,
        "passenger_count": 2,
        "pickup_datetime": "2015-05-07 19:52:06"
    }])

    fe = FeatureEngineer()
    out = fe.transform(df)

    # check new columns exist
    for col in ["Distance", "weekday", "monthly_quarters", "hourly_quarters", "ambulance_type"]:
        assert col in out.columns

    # check ambulance_type
    assert out.loc[0, "ambulance_type"] == "ALS"

    # Distance should be non-negative and finite
    dist = out.loc[0, "Distance"]
    assert dist >= 0
    assert pd.notna(dist)