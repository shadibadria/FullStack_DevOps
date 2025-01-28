/*
  Configuration for Pipeline
*/
def unit_testing() {
  echo "Running Unit Testing..."
  sh "docker build -t my-test-image ."
  sh "docker run --rm my-test-image"
}


//increment backend version
def increment_backend_version() {
    echo 'incrementing backend app version...'
    sh "npm version patch >> backend.txt"
    def version=readFile('backend').trim()
    echo "${version} , ${BUILD_NUMBER}"
    env.BACKEND_IMAGE_NAME = "$version-$BUILD_NUMBER"
    echo "${BACKEND_IMAGE_NAME}"
}
//increment frontend version
def increment_frontend_version() {
    echo 'incrementing frontend app version...'
    sh "npm version patch >> front_end";
    def version=readFile('front_end').trim()
    env.FRONTEND_IMAGE_NAME = "$version-$BUILD_NUMBER"
    echo "${FRONTEND_IMAGE_NAME}"
}

//build docker images
def buildimage() {
    echo "Building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
      sh "sudo docker build -f  frontend/Dockerfile-prod -t shadibadria/fullstack:frontend-${FRONTEND_IMAGE_NAME} ."
      sh "sudo docker build  -t  shadibadria/fullstack:backend-${BACKEND_IMAGE_NAME} ./backend "
      sh "echo $PASSWORD | sudo docker login -u $USERNAME --password-stdin"
      sh "sudo docker push shadibadria/fullstack:backend-${BACKEND_IMAGE_NAME} "
      sh "sudo docker push shadibadria/fullstack:frontend-${FRONTEND_IMAGE_NAME} "
    }
}

//deploy to git as new branch
def git_deploy(){
    withCredentials([usernamePassword(credentialsId: 'github-cred', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
    sh "git config --global user.name ${USER}"
    sh "git config --global user.email shadibadria@gmail.com"
    sh "git remote set-url origin https://${USER}:${PASS}@github.com/shadibadria/CI-CD_Terraform_FullStack"
    sh "git checkout -b jenkins-jobs-${FRONTEND_IMAGE_NAME}"
    sh 'git add .'
    sh 'git commit -m "ci: version bump"'
    sh "git push origin HEAD:jenkins-jobs-${FRONTEND_IMAGE_NAME}"
}

}
// deploy to nexus
def deploy_to_nexus_docker(){

}


//deploy frontend app to production
def deploy_frontend_app() {
      echo "Deploying the application..."
    withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
      def frontend_image = "shadibadria/fullstack:frontend-${FRONTEND_IMAGE_NAME}"
      def script = " bash ./frontend_server.sh ${frontend_image} "
      def ec2_instance = "ec2-user@54.236.22.179"
      sshagent(['ec2-server-key']) {
      sh " ssh -o StrictHostKeyChecking=no ${ec2_instance} echo "
      sh "sudo docker pull ${frontend_image}"
      sh "scp frontend_server.sh ${ec2_instance}:/home/ec2-user"
      sh " scp frontend.yaml ${ec2_instance}:/home/ec2-user"
      sh " ssh -o StrictHostKeyChecking=no ${ec2_instance} ${script} "
    }
      
    }
}

//deploy backend app to production
def deploy_backend_app() {
      echo "Deploying the application..."
    withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
      def backend_image = "shadibadria/fullstack:backend-${BACKEND_IMAGE_NAME}"
      def script = " bash ./backend_server.sh  ${backend_image}"
      def ec2_instance = "ec2-user@54.89.203.91"
      sshagent(['backend_server']) {
      sh " ssh -o StrictHostKeyChecking=no ${ec2_instance} echo "
      sh "sudo docker pull ${backend_image}"
      sh "scp backend_server.sh ${ec2_instance}:/home/ec2-user"
      sh "scp -r  env ${ec2_instance}:/home/ec2-user"
      sh " scp backend.yaml ${ec2_instance}:/home/ec2-user"
      sh " ssh -o StrictHostKeyChecking=no ${ec2_instance} ${script} "
    }
      
    }
}


    return this
