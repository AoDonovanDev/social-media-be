pipeline {
    agent any
    
    environment {
        IMAGE_TAG = 'new-sm-img-22time'
	    DOCKER_CREDENTIALS = credentials('docker-credentials')
        ENV_PROPERTIES_FILE = credentials('env-properties')
        BACKEND_EC2_SSH_KEY = credentials('backend-ec2-ssh-key')
       
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }
        
        stage('Set Environment'){
            steps {
                withCredentials([file(credentialsId: "env-properties", variable: 'secrets')]){
                    sh "cp ${secrets} /ec2-user/var/Jenkins/workspace/social-media-be2/src/main/resources"
                    }
            }
        }

        stage('Build jar file') {
            steps {
                script {
                    sh "mvn -DskipTests package";
                }
            }
        }
        
        stage('Build and push docker image') {
            steps {
                script {
                    sh "docker build -t be_test .";
                    sh "docker tag be_test:latest aliamfrager/revature-project-2-be:latest";
                    sh "echo ${DOCKER_CREDENTIALS_PSW}| docker login --username ${DOCKER_CREDENTIALS_USR} --password-stdin"
                    sh "docker push aodonovan/social-media-be-docker-repo:latest"
                }
            }
        }
        stage('Remote into docker runner ec2, pull and run image') {
            steps {
                script {
                    sh "ssh -i ${BACKEND_EC2_SSH_KEY} ec2-user@ec2-3-137-181-232.us-east-2.compute.amazonaws.com -y";
                    sh "sudo docker image pull aodonovan/social-media-be-docker-repo:latest";
                    sh "docker run -d aodonovan/social-media-be-docker-repo";
                    sh "exit"
                }
            }
        }
    }
}