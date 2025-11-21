pipeline {
    agent any

    environment {
        PATH = "/usr/local/bin:${env.PATH}"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/BKAPADIA04/SPE_Major_Project.git'
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

        stage('Add Pipeline') {
            steps {
                sh """
                    . venv/bin/activate
                    dvc add MLOpsPipeline/data/data_slices
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

        stage('Push DVC Artifacts') {
            steps {
                sh """
                    . venv/bin/activate
                    dvc push --remote localremote
                """
            }
        }

        // stage('Commit Updated Lockfile') {
        //     steps {
        //         sh """
        //             git config user.email "jenkins@example.com"
        //             git config user.name "Jenkins"

        //             git add dvc.lock
        //             git commit -m "Auto-update: retrained model via Jenkins" || true
        //             git push origin main || true
        //         """
        //     }
        // }
    }
}
