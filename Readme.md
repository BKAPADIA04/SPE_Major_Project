# 🚑 Smart Emergency Response System (SPE)

A **mlops-based microservices-based Smart Emergency Response System** built using  
**Spring Boot, Kubernetes, Horizontal Pod Autoscaling (HPA)**, and an **MLOps pipeline using DVC** to dynamically generate **ambulance pricing using machine learning**.

---

## 📌 Project Overview

This project simulates a **real-world emergency response platform** consisting of multiple microservices:

- 🚑 **Ambulance Service** – Calculates ambulance pricing using an ML model
- 📞 **Dispatch Service** – Assigns ambulances and communicates with ambulance service
- 🚨 **Emergency Service** – Entry point for emergency requests
- 📊 **ELK Stack** – Centralized logging
- ⚙️ **Kubernetes** – Container orchestration
- 📈 **HPA** – Auto-scaling based on CPU usage
- 🤖 **MLOps with DVC** – Versioned ML pipeline for ambulance price prediction

---

## 🏗️ System Architecture

The Smart Emergency Response System follows a **microservices-based cloud-native architecture** integrated with an **end-to-end MLOps pipeline** to generate real-time ambulance price predictions.

---

### 🔹 High-Level Architecture

- Users interact with the system through the Emergency Service
- Requests are routed internally across microservices
- Pricing predictions are generated using a deployed ML model

---
## 🔹 Main Application Flow

```mermaid
flowchart TD
    A[Client / User]
    B[Emergency Service]
    C[Dispatch Service]
    D[Ambulance Service]
    E[Flask ML Prediction Service (/predict)]

    A --> B
    B --> C
    C --> D
    D --> E
```
