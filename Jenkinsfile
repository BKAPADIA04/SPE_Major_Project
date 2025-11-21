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

        stage('Install yq') {
            steps {
                sh '''
                    mkdir -p $WORKSPACE/bin
                    curl -L https://github.com/mikefarah/yq/releases/latest/download/yq_darwin_amd64 \
                        -o $WORKSPACE/bin/yq
                    chmod +x $WORKSPACE/bin/yq
                '''
            }
        }

        stage('DVC Pipeline Execution (with yq PATH)') {
            environment {
                PATH = "$WORKSPACE/bin:${env.PATH}"
            }
            stages {

                stage('DVC Pull') {
                    steps {
                        sh """
                            . venv/bin/activate
                            yq --version
                            dvc pull --remote local_remote
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
                            yq --version
                            dvc repro
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
    }
}
