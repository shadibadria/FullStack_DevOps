#!/usr/bin/env groovy

def gv
pipeline {
  agent any
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
