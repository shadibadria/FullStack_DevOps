#!/usr/bin/env groovy

def gv

pipeline {
  agent {
    docker 'node:16'  // Correct docker agent syntax
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
