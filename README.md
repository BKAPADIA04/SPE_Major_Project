# 🚑 RapidAid — Intelligent Emergency Ambulance Dispatch System

> A production-grade, microservices-based platform for real-time emergency ambulance dispatch, powered by an ML-driven fare/ETA prediction engine and a full MLOps pipeline.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Microservices](#microservices)
  - [EmergencyRequestService](#1-emergencyrequestservice)
  - [AmbulanceLocationService](#2-ambulancelocationservice)
  - [DispatchService](#3-dispatchservice)
- [MLOps Pipeline](#mlops-pipeline)
- [Observability — ELK Stack](#observability--elk-stack)
- [Infrastructure & DevOps](#infrastructure--devops)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Local Development (Docker Compose)](#local-development-docker-compose)
  - [Kubernetes Deployment](#kubernetes-deployment)
  - [ML Pipeline](#ml-pipeline)
- [Project Structure](#project-structure)
- [Contributing](#contributing)

---

## Overview

**RapidAid** is a cloud-native emergency response platform that coordinates ambulance dispatch using real-time location tracking, intelligent routing, and machine learning–based ETA/fare prediction. The system is designed for high availability and observability, deployed on Kubernetes and monitored through a full ELK (Elasticsearch, Logstash, Kibana) stack.

### Key Features

- 🆘 **Emergency Request Intake** — REST API to submit and manage patient emergency requests
- 📍 **Real-time Ambulance Tracking** — Live GPS location updates and fleet status management
- 🤖 **ML-Powered Prediction** — Gradient Boosting model trained on ride data predicts ETA/fare with haversine-distance feature engineering
- 🔄 **Automated MLOps** — DVC-versioned data, automated retraining, and MLflow model registry
- 📊 **Full Observability** — Structured JSON logs shipped via Filebeat → Logstash → Elasticsearch → Kibana
- ☸️ **Kubernetes-native** — All services deployed on Minikube/K8s with health checks and auto-scaling support
- 🔧 **CI/CD with Jenkins** — Fully automated pipeline from code push to production deployment

---

## Architecture

```
┌──────────────────────────────────────────────────────────────────┐
│                        Client / Frontend                          │
└────────────────────────────┬─────────────────────────────────────┘
                             │  REST
          ┌──────────────────┼──────────────────┐
          ▼                  ▼                  ▼
┌──────────────────┐ ┌──────────────┐ ┌──────────────────┐
│ EmergencyRequest │ │  Ambulance   │ │    Dispatch      │
│    Service       │ │  Location    │ │    Service       │
│  (Spring Boot)   │ │   Service    │ │  (Spring Boot)   │
│  Port: 8082      │ │ (Spring Boot)│ │  Port: 8081      │
└────────┬─────────┘ │  Port: 8080  │ └────────┬─────────┘
         │           └──────┬───────┘          │
         │                  │ location data    │
         └──────────────────┴──────────────────┘
                             │ Structured Logs (JSON)
                             ▼
                    ┌─────────────────┐
                    │    Filebeat     │
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐
                    │    Logstash     │ :5044
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐     ┌─────────┐
                    │ Elasticsearch   │◄────►│ Kibana  │
                    │   :9200         │     │  :5601  │
                    └─────────────────┘     └─────────┘

                    ┌───────────────────────────────────┐
                    │           MLOps Pipeline           │
                    │  DVC ─► Train ─► MLflow Registry  │
                    │         (GBM Model + FastAPI)      │
                    └───────────────────────────────────┘
```

---

## Microservices

All three services are **Spring Boot 3.x / Java 17** applications built with Maven, containerized via Docker, and deployed on Kubernetes.

### 1. EmergencyRequestService

**Port:** `8082`

Handles patient emergency intake. Accepts and stores emergency requests with patient location, severity, and type details.

**Key Endpoints:**
| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/emergency` | Submit a new emergency request |
| `GET`  | `/api/emergency` | List all emergency requests |
| `GET`  | `/api/emergency/{id}` | Get request by ID |

**Dependencies:** Spring Web, Spring Actuator, Spring Validation, Jackson JSR310, Logstash-Logback-Encoder

---

### 2. AmbulanceLocationService

**Port:** `8080`

Manages the ambulance fleet — registration, GPS location updates, and status transitions (AVAILABLE → DISPATCHED → ON_SCENE → etc.).

**Key Endpoints:**
| Method | Path | Description |
|--------|------|-------------|
| `POST`   | `/api/ambulances` | Register a new ambulance |
| `GET`    | `/api/ambulances` | Get all ambulances |
| `GET`    | `/api/ambulances/{id}` | Get ambulance by UUID |
| `PUT`    | `/api/ambulances/{id}/location` | Update GPS coordinates |
| `PUT`    | `/api/ambulances/{id}/status` | Update ambulance status |

**Dependencies:** Spring Web, Spring Actuator, Spring Docker Compose, Logstash-Logback-Encoder

---

### 3. DispatchService

**Port:** `8081`

Coordinates the assignment of ambulances to emergency requests. Acts as the orchestrator between the `EmergencyRequestService` and `AmbulanceLocationService`.

**Dependencies:** Spring Web, Spring Actuator, Logstash-Logback-Encoder

---

## MLOps Pipeline

Located in `MLOpsPipeline/`, the ML subsystem predicts ambulance ETA/fare using a **Gradient Boosting Regressor** trained on Uber NYC ride data as a proxy for ambulance dispatch routing.

### Data Pipeline (DVC)

```
data_slices/ (versioned via DVC)
       │
       ▼
generate_params.py  ──►  params.yaml
       │
       ▼
train.py  ──►  models/model.pkl  ──►  register_model.py  ──►  MLflow Registry
```

### ML Model

| Component | Details |
|-----------|---------|
| **Algorithm** | `GradientBoostingRegressor` (scikit-learn) |
| **Feature Engineering** | Haversine distance, time-of-day buckets, day-of-week, quarterly bins |
| **Ambulance Type** | Derived from passenger count → `BLS` / `ALS` / `CriticalCare` |
| **Preprocessing** | StandardScaler (numeric), OneHotEncoder (categorical), SimpleImputer |
| **Target** | `fare_amount` (proxy for ETA/routing cost) |
| **Tracking** | MLflow experiment: `SPE_Model_Training`, model: `SPE_Regression_Model` |

### Sliding Window Training

Data is split into time-ordered slices (`slice_1.csv`, `slice_2.csv`, …) for incremental/sliding-window retraining, enabling the model to adapt to new traffic patterns over time.

### Inference API

```bash
# Start FastAPI server (from MLOpsPipeline/codes/)
uvicorn api:app --reload --port 8000

# POST /predict
curl -X POST http://localhost:8000/predict \
  -H "Content-Type: application/json" \
  -d '{"pickup_latitude": 40.7614, "pickup_longitude": -73.9776, ...}'
```

---

## Observability — ELK Stack

All microservices emit **structured JSON logs** via `logstash-logback-encoder`. Logs are collected and visualized through the ELK stack.

| Component | Port | Role |
|-----------|------|------|
| Elasticsearch | `9200` | Log storage and indexing |
| Logstash | `5044` | Log aggregation and parsing |
| Kibana | `5601` | Dashboards and log exploration |
| Filebeat | — | Log shipping agent (DaemonSet in K8s) |

Start the ELK stack locally:
```bash
docker-compose up -d
```

---

## Infrastructure & DevOps

### CI/CD — Jenkins Pipeline

The `Jenkinsfile` defines a full end-to-end pipeline:

```
Checkout → Setup Python Env → DVC Pull → Show Slice Count
    → DVC Repro (retrain) → Show Metrics → Push DVC Artifacts
    → Register Model (MLflow) → Start Minikube → Build Docker Images
    → Apply K8s Manifests → Wait for Pods Ready → Verify Deployments
```

### Kubernetes

All services are deployed in the `spe-system-1` namespace:

```
k8s/
├── namespace.yaml
├── ambulance/   (deployment.yaml, service.yaml)
├── dispatch/    (deployment.yaml, service.yaml)
├── emergency/   (deployment.yaml, service.yaml)
└── elk/         (Elasticsearch, Logstash, Kibana manifests)
```

Apply all manifests:
```bash
kubectl apply -f k8s/ --recursive
kubectl wait --for=condition=ready pod --all -n spe-system-1 --timeout=240s
```

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Microservices | Java 17, Spring Boot 3.x, Maven |
| ML / Data | Python 3, scikit-learn, pandas, numpy |
| ML Tracking | MLflow, joblib |
| Data Versioning | DVC |
| Inference API | FastAPI, Uvicorn |
| Containerization | Docker |
| Orchestration | Kubernetes (Minikube) |
| CI/CD | Jenkins |
| Logging | Logstash-Logback-Encoder, Filebeat |
| Observability | Elasticsearch 8.8, Logstash 8.8, Kibana 8.8 |
| Testing | JUnit, pytest, requests |

---

## Getting Started

### Prerequisites

- Java 17+
- Python 3.9+
- Docker & Docker Compose
- Minikube + kubectl
- Jenkins (for CI/CD)
- DVC (`pip install dvc`)
- MLflow (`pip install mlflow`)

### Local Development (Docker Compose)

**1. Start the ELK observability stack:**
```bash
docker-compose up -d
# Kibana available at http://localhost:5601
# Elasticsearch at http://localhost:9200
```

**2. Run each microservice locally:**
```bash
# AmbulanceLocationService
cd AmbulanceLocationService
./mvnw spring-boot:run

# EmergencyRequestService
cd EmergencyRequestService
./mvnw spring-boot:run

# DispatchService
cd DispatchService
./mvnw spring-boot:run
```

### Kubernetes Deployment

```bash
# Start Minikube
minikube start --driver=docker --cpus=4 --memory=7800
eval $(minikube -p minikube docker-env)

# Build Docker images
docker build -t ambulance-location-service:latest ./AmbulanceLocationService
docker build -t dispatch-service:latest ./DispatchService
docker build -t emergency-service:latest ./EmergencyRequestService

# Deploy
kubectl apply -f k8s/ --recursive
kubectl get pods -n spe-system-1
```

### ML Pipeline

**1. Install Python dependencies:**
```bash
pip install -r requirements.txt
```

**2. Pull data from DVC remote:**
```bash
dvc pull --remote localremote
```

**3. Run the full DVC pipeline (generate params + train model):**
```bash
dvc repro
```

**4. Start MLflow tracking server:**
```bash
mlflow server \
    --backend-store-uri sqlite:///mlflow.db \
    --default-artifact-root ./mlruns \
    --host 0.0.0.0 \
    --port 5001
# UI at http://localhost:5001
```

**5. Register the trained model:**
```bash
python3 MLOpsPipeline/codes/register_model.py
```

**6. Train on a specific data slice manually:**
```bash
# From MLOpsPipeline/codes/
python3 train.py --slice 3 --output ../models/slice3.pkl
```

**7. Serve inference API:**
```bash
# From MLOpsPipeline/codes/
uvicorn api:app --reload --port 8000
```

---

## Project Structure

```
RapidAid/
├── AmbulanceLocationService/       # Spring Boot — fleet GPS & status management
│   ├── src/main/java/com/example/ambulance/
│   │   ├── controller/             # REST controllers
│   │   ├── model/                  # Ambulance, AmbulanceStatus entities
│   │   └── service/                # Business logic
│   └── Dockerfile
├── DispatchService/                # Spring Boot — ambulance assignment orchestrator
│   └── Dockerfile
├── EmergencyRequestService/        # Spring Boot — emergency intake & management
│   └── Dockerfile
├── MLOpsPipeline/                  # Python ML subsystem
│   ├── codes/
│   │   ├── pipeline.py             # sklearn Pipeline (feature eng + GBM)
│   │   ├── train.py                # Training script (sliding window)
│   │   ├── data_loader.py          # Sliding window data loader
│   │   ├── generate_params.py      # DVC param generation
│   │   ├── register_model.py       # MLflow model registration
│   │   ├── inference.py            # Inference utilities
│   │   └── api.py                  # FastAPI prediction endpoint
│   ├── data/
│   │   ├── uber.csv                # Raw Uber NYC dataset
│   │   └── data_slices/            # DVC-tracked time-ordered slices
│   └── models/                     # Trained model artifacts (.pkl)
├── k8s/                            # Kubernetes manifests
│   ├── namespace.yaml
│   ├── ambulance/
│   ├── dispatch/
│   ├── emergency/
│   └── elk/
├── logstash/                       # Logstash pipeline config
├── docker-compose.yaml             # ELK stack local setup
├── filebeat-configmap.yaml         # Filebeat K8s ConfigMap
├── dvc.yaml                        # DVC pipeline definition
├── dvc.lock                        # DVC lock file
├── Jenkinsfile                     # Jenkins CI/CD pipeline
└── requirements.txt                # Python dependencies
```

---

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m "feat: add your feature"`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request

---

<div align="center">
  <sub>Built with ❤️ as part of the SPE (Software & Production Engineering) Major Project</sub>
</div>
