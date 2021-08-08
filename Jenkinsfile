node('slave1') {
  def imageTag = 'psreepathi/nestjs-image:1.0.0'
  stage('scm') {
    git 'https://github.com/ReddyPrashanth/nestjs-ci-cd-jenkins.git'
  }

  stage('Build Docker Image') {
    sh "docker build -t ${imageTag} ."
  }

  stage('Push Image to DockerHub') {
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'password', usernameVariable: 'username')]) {
      sh 'docker login -u $username -p $password'
    }
    sh "docker push ${imageTag}"
  }

  stage('Remove Old Containers') {
    sshagent(['jenkins-ssh']) {
      try{
        def sshCmd = 'ssh -o StrictHostKeyChecking=no jenkins@18.189.180.227'
        def dockerRm = 'docker rm -f nest-app'
        sh "${sshCmd} ${dockerRm}"
      }catch(error) {
        echo 'Image nest-app not found.'
      }
    }
  }

  stage('Deploying to Dev') {
    sshagent(['jenkins-ssh']) {
        input 'Deploy to Dev?'
        def sshCmd = 'ssh -o StrictHostKeyChecking=no jenkins@18.189.180.227'
        def dockerRm = "docker run -d -p 3000:3000 --name nest-app ${imageTag}"
        sh "${sshCmd} ${dockerRm}"
    }
  }
} 