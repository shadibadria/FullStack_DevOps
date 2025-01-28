#!/usr/bin/env groovy

def gv

pipeline {
    agent {
        docker {
            image 'alpine' // You can also use 'ubuntu' or any other Linux-based image
        }
    }
  stages {
    stage('Init') {
      steps {
        script {
          gv = load "script.groovy"  // Ensure script.groovy exists
        }
      }
    }
    stage('Unit Testing') {
      steps {
        dir('backend') {
          script {
            gv.unit_testing()  // Calls function from script.groovy
          }
        }
      }
    }
  }
}
