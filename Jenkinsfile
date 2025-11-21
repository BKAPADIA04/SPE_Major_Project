pipeline {
    agent any

    environment {
        PATH = "/usr/local/bin:${env.PATH}"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'mlops-dvc', url: 'https://github.com/BKAPADIA04/SPE_Major_Project.git'
            }
        }

        stage('Install Dependencies') {
            steps {
                sh """
                    python3 -m venv venv
                    . venv/bin/activate
                    pip install -r requirements.txt
                """
            }
        }

        stage('Pull DVC Data') {
            steps {
                sh """
                    . venv/bin/activate
                    dvc pull --remote local_remote
                """
            }
        }

        stage('Reproduce Pipeline') {
            steps {
                sh """
                    . venv/bin/activate
                    dvc repro
                """
            }
        }

        stage('Push Updated Artifacts to Local DVC Storage') {
            steps {
                sh """
                    . venv/bin/activate
                    dvc push --remote local_remote
                """
            }
        }
    }
}