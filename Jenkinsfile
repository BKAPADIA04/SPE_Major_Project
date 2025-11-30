// pipeline {
//     agent any

//     environment {
//         PATH = "/usr/local/bin:${env.PATH}"
//     }

//     stages {

//         stage('Checkout Code') {
//             steps {
//                 git branch: 'mlflow', url: 'https://github.com/BKAPADIA04/SPE_Major_Project.git'

//             }
//         }

//         stage('Setup Python Environment') {
//             steps {
//                 sh """
//                     python3 -m venv venv
//                     . venv/bin/activate
//                     pip install --upgrade pip
//                     pip install -r requirements.txt
//                     pip install dvc[yaml]
//                     pip install ansible requests docker
//                 """
//             }
//         }

//         stage('DVC Pull') {
//             steps {
//                 sh """
//                     . venv/bin/activate
//                     dvc pull --remote localremote
//                 """
//             }
//         }


//         // stage('Add Pipeline') {
//         //     steps {
//         //         sh """
//         //             . venv/bin/activate
//         //             dvc add MLOpsPipeline/data/data_slices
//         //         """
//         //     }
//         // }

//         stage('Show Slice Count') {
//             steps {
//                 sh """
//                     echo "==== GENERATED SLICE COUNT ===="
//                     cat MLOpsPipeline/codes/params.log || echo "No params.log found"
//                 """
//             }
//         }

//         stage('Run DVC Pipeline') {
//             steps {
//                 sh """
//                     . venv/bin/activate
//                     dvc repro
//                 """
//             }
//         }

//         stage('Show Training Metrics') {
//             steps {
//                 sh """
//                     echo "==== TRAINING METRICS (from train.log) ===="
//                     cat MLOpsPipeline/codes/train.log || echo "No metrics file found"
//                 """
//             }
//         }


//         stage('Push DVC Artifacts') {
//             steps {
//                 sh """
//                     . venv/bin/activate
//                     dvc push --remote localremote
//                 """
//             }
//         }

//         stage('Train & Register Model (MLflow v2, v3...)') {
//             steps {
//                 sh """
//                     . venv/bin/activate
//                     python3 MLOpsPipeline/codes/register_model.py
//                 """
//             }
//         }

//         // stage('Start Minikube') {
//         //     steps {
//         //         sh """
//         //             minikube start --driver=docker --cpus=4 --memory=7800
//         //             minikube status
//         //             eval \$(minikube -p minikube docker-env)
//         //         """
//         //     }
//         // }

//         // stage('Start Minikube') {
//         //     steps {
//         //         sh """
//         //             minikube start --driver=docker --cpus=4 --memory=7800
//         //             minikube status
//         //             eval \$(minikube -p minikube docker-env)
//         //         """
//         //     }
//         // }

//         stage('Build Docker Images') {
//             steps {
//                 sh """
//                     docker build -t ambulance-location-service:latest ./AmbulanceLocationService
//                     docker build -t dispatch-service:latest ./DispatchService
//                     docker build -t emergency-service:latest ./EmergencyRequestService
//                 """
//             }
//         }

//         stage('Apply k8s') {
//             steps {
//                 sh """
//                     kubectl apply -f k8s/ --recursive
//                 """
//             }
//         }

//         stage('Wait for Pods Ready Services') {
//             steps {
//                 sh """
//                     echo "Waiting for all pods to be ready..."
//                     kubectl wait --for=condition=ready pod --all -n spe-system-1 --timeout=240s
//                 """
//             }
//         }

//         stage('Wait for Pods Ready Default') {
//             steps {
//                 sh """
//                     echo "Waiting for all pods to be ready..."
//                     kubectl wait --for=condition=ready pod --all -n default --timeout=300s
//                 """
//             }
//         }

//         stage('Verify Deployments') {
//             steps {
//                 sh """
//                     kubectl get pods -n=spe-system-1
//                     kubectl get pods -n=default
//                 """
//             }
//         }
//     }
// }


pipeline {
    agent any

    environment {
        PATH = "/usr/local/bin:${env.PATH}"
        DOCKERHUB_USER = "siddeshwarsk"
        DOCKERHUB_TOKEN = credentials('dckr_pat_nhth7AgxKVeKp9DtQpoBe8LIMRc')   
    }

    stages {

        stage('Checkout Code') {
            steps {
                cleanWs()
                git branch: 'mlflow', url: 'https://github.com/BKAPADIA04/SPE_Major_Project.git'
            }
        }

        stage('Setup Python Environment') {
            steps {
                sh """
                    python3 -m venv venv
                    . venv/bin/activate
                    pip install --upgrade pip
                    pip install -r requirements.txt
                    pip install dvc[yaml]
                    pip install ansible requests docker
                """
            }
        }

        stage('DVC Pull') {
            steps {
                sh """
                    . venv/bin/activate
                    dvc pull --remote localremote
                """
            }
        }

        stage('Show Slice Count') {
            steps {
                sh """
                    echo "==== GENERATED SLICE COUNT ===="
                    cat MLOpsPipeline/codes/params.log || echo "No params.log found"
                """
            }
        }

        stage('Run DVC Pipeline') {
            steps {
                sh """
                    . venv/bin/activate
                    dvc repro
                """
            }
        }

        stage('Show Training Metrics') {
            steps {
                sh """
                    echo "==== TRAINING METRICS ===="
                    cat MLOpsPipeline/codes/train.log || echo "No metrics found"
                """
            }
        }

        stage('Push DVC Artifacts') {
            steps {
                sh """
                    . venv/bin/activate
                    dvc push --remote localremote
                """
            }
        }

        stage('Train & Register Model (MLflow)') {
            steps {
                sh """
                    . venv/bin/activate
                    python3 MLOpsPipeline/codes/register_model.py
                """
            }
        }

        stage('Start Minikube') {
            steps {
                sh """
                    minikube start --driver=docker --cpus=4 --memory=7800
                    minikube status
                    eval \$(minikube -p minikube docker-env)
                """
            }
        }

        stage('Docker Hub Login') {
            steps {
                sh """
                    echo "Logging in to Docker Hub..."
                    docker login -u ${DOCKERHUB_USER} -p ${DOCKERHUB_TOKEN}
                """
            }
        }

        stage('Build & Push Docker Images') {
            steps {
                sh """
                    echo "Building Docker images..."

                    docker build -t ambulance-location-service:local ./AmbulanceLocationService
                    docker build -t dispatch-service:local ./DispatchService
                    docker build -t emergency-service:local ./EmergencyRequestService

                    echo "Tagging..."
                    docker tag ambulance-location-service:local ${DOCKERHUB_USER}/ambulance-location-service:local
                    docker tag dispatch-service:local ${DOCKERHUB_USER}/dispatch-service:local
                    docker tag emergency-service:local ${DOCKERHUB_USER}/emergency-service:local

                    echo "Pushing..."
                    docker push ${DOCKERHUB_USER}/ambulance-location-service:local
                    docker push ${DOCKERHUB_USER}/dispatch-service:local
                    docker push ${DOCKERHUB_USER}/emergency-service:local
                """
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh """
                    echo "Applying Kubernetes manifests..."
                    kubectl apply -f k8s/namespace.yaml
                    kubectl apply -f k8s/ --recursive
                """
            }
        }

        stage('Wait for Pods Ready (spe-system-1)') {
            steps {
                sh """
                    kubectl wait --for=condition=ready pod --all -n spe-system-1 --timeout=240s
                """
            }
        }

        stage('Wait for Pods Ready (default)') {
            steps {
                sh """
                    kubectl wait --for=condition=ready pod --all -n default --timeout=300s
                """
            }
        }

        stage('Verify Deployments') {
            steps {
                sh """
                    kubectl get pods -n spe-system-1
                    kubectl get pods -n default
                """
            }
        }
    }
}