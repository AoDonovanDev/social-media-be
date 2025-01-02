pipeline {
    agent any
    
    environment {
        IMAGE_TAG = 'new-sm-img-22time'
	DOCKER_CREDENTIALS = credentials('docker-credentials')
        SPRING_DATASOURCE_URL = credentials('SPRING_DATASOURCE_URL')
        SPRING_DATASOURCE_USERNAME = credentials('SPRING_DATASOURCE_USERNAME')
        SPRING_DATASOURCE_PASSWORD = credentials('SPRING_DATASOURCE_PASSWORD')
        AWS_ACCESS_KEY_ID = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_ACCESS_KEY')
    }
    
    tools {
        maven 'Maven-3.9.9'
    }
	
    stages {
        stage('Checkout Code') {
            steps {
                // checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/BitsuRevature/social-media-be']])
		checkout scm
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
                    sh "docker build --platform linux/amd64 -t be_test .";
                    sh "docker tag be_test:latest liamfrager/revature-project-2-be:latest";
                    sh "echo ${DOCKER_CREDENTIALS_PSW}| docker login --username ${DOCKER_CREDENTIALS_USR} --password-stdin"
                    sh "docker push liamfrager/revature-project-2-be:latest"
                }
            }
        }
        stage('Remote into docker runner ec2, pull and run image') {
            steps {
                script {
			
                    withCredentials([sshUserPrivateKey(credentialsId: 'backend-ec2-ssh-key', keyFileVariable: 'ssh_private_key', usernameVariable: 'ssh_username')]) {
                        sh 'chmod 600 ${ssh_private_key}'
                        sh '''
			    ssh -o StrictHostKeyChecking=no -i ${ssh_private_key} ${ssh_username}@ec2-3-137-181-232.us-east-2.compute.amazonaws.com -y 'container_id=$(docker ps -q --filter "ancestor=liamfrager/revature-project-2-be:latest" --filter "publish=80") && [ ! -z "$container_id" ] && docker stop $container_id && docker rm $container_id'
			'''
		    	sh 'ssh -o StrictHostKeyChecking=no -i ${ssh_private_key} ${ssh_username}@ec2-3-137-181-232.us-east-2.compute.amazonaws.com -y "docker pull --platform linux/amd64 liamfrager/revature-project-2-be:latest"'
                        sh 'ssh -o StrictHostKeyChecking=no -i ${ssh_private_key} ${ssh_username}@ec2-3-137-181-232.us-east-2.compute.amazonaws.com -y "docker run --platform linux/amd64 -p 80:8080 -d -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} -e SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL} -e SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME} -e SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD} liamfrager/revature-project-2-be"'
                    }
                }
            }
        }
    }
}
