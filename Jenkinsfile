pipeline {
    agent any

    environment {
        PATH = "/usr/local/bin:${env.PATH}"
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
                    python3 -m venv venv
                    . venv/bin/activate
                    pip install --upgrade pip
                    pip install -r requirements.txt
                    pip install dvc[yaml]
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

        stage('DVC Pull') {
            environment {
                PATH = "$WORKSPACE/bin:${env.PATH}"
            }
            steps {
                sh """
                    . venv/bin/activate
                    yq --version
                    dvc pull --remote local_remote || true
                """
            }
        }

        stage('Run DVC Pipeline (Force)') {
            steps {
                sh """
                    . venv/bin/activate
                    dvc repro --force
                """
            }
        }

        stage('Show Training Metrics') {
            steps {
                sh """
                    echo "==== TRAINING METRICS (from train.log) ===="
                    cat MLOpsPipeline/codes/train.log || echo "No metrics file found"
                """
            }
        }

        stage('Push DVC Artifacts') {
            steps {
                sh """
                    . venv/bin/activate
                    dvc push --remote local_remote
                """
            }
        }

    }
}