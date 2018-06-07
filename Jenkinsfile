node ('nimble-jenkins-slave') {
    stage('Download Latest') {
        git(url: 'https://github.com/nimble-platform/collaboration-tools.git', branch: 'master')
    }

    stage ('Build docker image') {
        sh 'cd collaboration-service'
        sh 'mvn clean install'
        sh 'docker build -t nimbleplatform/collaboration-service:${BUILD_NUMBER} .'
    }

    stage ('Push docker image') {
        withDockerRegistry([credentialsId: 'NimbleDocker']) {
            sh 'docker push nimbleplatform/collaboration-service:${BUILD_NUMBER}'
        }
    }

    stage ('Deploy') {
        sh ''' sed -i 's/IMAGE_TAG/'"$BUILD_NUMBER"'/g' kubernetes/deploy.yaml '''
        sh 'kubectl apply -f kubernetes/deploy.yaml -n prod --validate=false'
        sh 'kubectl apply -f kubernetes/svc.yaml    -n prod --validate=false'
    }
    
    stage ('Print-deploy logs') {
        sh 'sleep 30'
        sh 'kubectl -n prod logs deploy/collaboration-service -c collaboration-service'
    }    
}
