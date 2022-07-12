podTemplate(yaml: '''
    apiVersion: v1
    kind: Pod
    spec:
      containers:
      - name: kaniko
        image: gcr.io/kaniko-project/executor:debug
        volumeMounts:
        - name: kaniko-secret
          mountPath: /kaniko/.docker
      restartPolicy: Never
      volumes:
      - name: kaniko-secret
        secret:
            secretName: dockercred
            items:
            - key: .dockerconfigjson
              path: config.json
''') {
  node(POD_LABEL) {
    stage('Checkout') {
      git url: 'https://github.com/ademyyazar/spring-echo-example.git', branch: 'master'
    }

    stage('Build Image') {
      container('kaniko') {
        stage('Build') {
          sh '''
            /kaniko/executor --context `pwd` --destination ademyyazar/dream-app:latest
          '''
        }
      }
    }
  }
}