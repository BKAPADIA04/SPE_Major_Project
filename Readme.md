# 🚑 Smart Emergency Response System (SPE)

A **cloud-native microservices-based Smart Emergency Response System** built using  
**Spring Boot, Kubernetes, Horizontal Pod Autoscaling (HPA), Ansible Vault**, and an **MLOps pipeline using DVC** to dynamically generate **ambulance pricing using machine learning**.

---

## 📌 Project Overview

This project simulates a **real-world emergency response platform** consisting of multiple microservices:

- 🚑 **Ambulance Service** – Calculates ambulance pricing using an ML model
- 📞 **Dispatch Service** – Assigns ambulances and communicates with ambulance service
- 🚨 **Emergency Service** – Entry point for emergency requests
- 📊 **ELK Stack** – Centralized logging
- ⚙️ **Kubernetes** – Container orchestration
- 📈 **HPA** – Auto-scaling based on CPU usage
- 🔐 **Ansible Vault** – Secure environment variable management
- 🤖 **MLOps with DVC** – Versioned ML pipeline for ambulance price prediction

---

## 🏗️ System Architecture~