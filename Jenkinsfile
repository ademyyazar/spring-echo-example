podTemplate(yaml: '''
    apiVersion: v1
    kind: Pod
    spec:
      containers:
      - name: kaniko
        image: gcr.io/kaniko-project/executor:debug
        args: ["--context=git://github.com/ademyyazar/spring-echo-example.git",
                "--destination=ademyyazar/dream-app:latest"]
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

    stage('Build Image') {
      container('kaniko') {
        stage('Build') {
          sh '''
          echo "Building..."
          '''
        }
      }
    }
  }
}