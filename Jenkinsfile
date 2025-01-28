#!/usr/bin/env groovy

def gv
pipeline {
  agent {
    docker {
      image 'node:16'  // Use Node.js image with npm installed
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
        dir('backend') {
          script {
            gv.unit_testing()
          }
        }
      }
    }
  }
}
