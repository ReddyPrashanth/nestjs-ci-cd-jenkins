node('slave1') {
  def imageTag
  def commit_id
  stage('scm') {
    git 'https://github.com/ReddyPrashanth/nestjs-ci-cd-jenkins.git'
    sh "git rev-parse --short HEAD > .git/commit-id"
    commit_id = readFile('.git/commit-id').trim()
    sh 'echo "commit id: ${commit_id}"'
    imageTag = "psreepathi/nestjs-image:${commit_id}"
  }

  stage('Build Docker Image') {
    sh "docker build -t ${imageTag} ."
  }

  stage('Push Image to DockerHub') {
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'password', usernameVariable: 'username')]) {
      sh 'docker login -u $username -p $password'
    }
    sh "docker push ${imageTag}"
    sh "docker logout && docker rmi ${imageTag}"
  }

  stage('Remove Old Containers') {
    sshagent(['jenkins-ssh']) {
      try{
        def sshCmd = 'ssh -o StrictHostKeyChecking=no ubuntu@18.191.139.173'
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
        def sshCmd = 'ssh -o StrictHostKeyChecking=no ubuntu@18.191.139.173'
        def dockerRm = "docker run -d -p 3000:3000 --name nest-app ${imageTag}"
        sh "${sshCmd} ${dockerRm}"
    }
  }
} 