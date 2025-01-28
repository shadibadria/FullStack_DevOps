#!/usr/bin/env groovy

def gv
pipeline {
  agent {
    docker {
      image 'alpine' // You can also use 'ubuntu', 'node', or any Linux-based image
    }
  }
  stages {
    stage('init') {
      steps {
        script {
          gv = load "script.groovy"
        }
      }
    }
    stage('Unit Testing') {
      steps {
        script {
          echo "here"
          sh 'pwd'
          echo "here"
        }
      }
    }
  }
}
