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
        script{
          sh 'pwd'
        }
        dir('backend') {
          script {
            
            gv.unit_testing()
          }
        }
      }
    }
  }
}