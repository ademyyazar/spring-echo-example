#!/usr/bin/env groovy
node {
    checkout scm
}

pipeline {
    agent any

    environment {
        imageId = 'ademyyazar/dream-app:1.$BUILD_NUMBER'
        docker_creds = credentials('dockerhub')
    }
    stages {
        stage('Build') {
            steps {
                sh "docker build --no-cache -t $imageId ."
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
            }
        }
        stage('Push') {
            steps {
                sh'''
                    docker login --username $docker_creds_USR --password $docker_creds_PSW
                    docker push $imageId
                    docker logout
                    docker rmi $imageId
                ''' 
            }
        }
    }
}