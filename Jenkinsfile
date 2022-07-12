pipeline {
  agent {
    kubernetes {
      defaultContainer 'kaniko'
      yaml '''
        kind: Pod
        spec:
          containers:
          - name: kaniko
            image: gcr.io/kaniko-project/executor:debug
            imagePullPolicy: Always
            command:
            - sleep
            args:
            - 99d
            volumeMounts:
              - name: jenkins-docker-cfg
                mountPath: /kaniko/.docker
          volumes:
          - name: jenkins-docker-cfg
            projected:
              sources:
              - secret:
                  name: dockercred
                  items:
                    - key: .dockerconfigjson
                      path: config.json
'''
    }
  }
  stages {
    stage('Build with Kaniko') {
      steps {
        git 'https://github.com/ademyyazar/spring-echo-example.git'
        sh '/kaniko/executor -f `pwd`/Dockerfile -c `pwd` --insecure --skip-tls-verify --cache=true --destination=ademyyazar/dream-app:latest'
      }
    }
  }
}