pipeline {
    agent any

    environment {
        PATH = "/usr/local/bin:${env.PATH}"
        REMOTE = "gdrive_remote"                             // your DVC remote name
        VENV = "venv"
        GOOGLE_APPLICATION_CREDENTIALS = "${WORKSPACE}/dvc-remote-479006-8eda1e952b02.json"
    }

    options {
        skipDefaultCheckout(true)
    }

    stages {

        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }

        stage('Checkout Code') {
            steps {
                git branch: 'jenkins', url: 'https://github.com/BKAPADIA04/SPE_Major_Project.git'
            }
        }

        stage('Setup Python Environment') {
            steps {
                sh """
                    python3 -m venv ${VENV}
                    . ${VENV}/bin/activate
                    pip install --upgrade pip
                    pip install -r requirements.txt
                    pip install 'dvc[gdrive]' mlflow
                """
            }
        }

        stage('Install yq') {
            steps {
                sh '''
                    mkdir -p $WORKSPACE/bin
                    curl -L https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64 \
                        -o $WORKSPACE/bin/yq
                    chmod +x $WORKSPACE/bin/yq
                '''
            }
        }

        stage('DVC Pull From GDrive') {
            environment {
                PATH = "$WORKSPACE/bin:${env.PATH}"
            }
            steps {
                sh """
                    . ${VENV}/bin/activate
                    yq --version
                    echo "DVC remotes:"
                    dvc remote list
                    echo "Pulling artifacts from GDrive..."
                    dvc pull -r ${REMOTE} --force
                """
            }
        }

        stage('Run DVC Pipeline') {
            steps {
                sh """
                    . ${VENV}/bin/activate
                    echo "Reproducing DVC pipeline..."
                    dvc repro --force
                """
            }
        }

        stage('Register Model in MLflow') {
            steps {
                sh """
                    . ${VENV}/bin/activate
                    echo "Registering model in MLflow..."
                    python3 MLOpsPipeline/codes/register_model.py
                """
            }
        }

        stage('Push Artifacts to GDrive') {
            steps {
                sh """
                    . ${VENV}/bin/activate
                    echo "Pushing updated artifacts to GDrive..."
                    dvc push -r ${REMOTE}
                """
            }
        }
    }

    // post {
    //     always {
    //         echo "Cleaning up virtual environment..."
    //         sh "rm -rf ${VENV}"
    //     }
    // }
}