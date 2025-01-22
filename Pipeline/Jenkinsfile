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
    stage('increment frontend version') {
      steps {
        dir('frontend') {
          script {
            gv.increment_frontend_version()
          }
        }
      }
    }
    stage('increment backend version') {
      steps {
        dir('backend') {
          script {
            gv.increment_backend_version()
          }
        }
      }
    }
    stage('build image ') {
      steps {
        script {
          gv.buildimage()
        }
      }
    }
    stage('deploy_backend_app') {
      steps {
        script {
          gv.deploy_backend_app()
        }
      }
    }
    stage('deploy_frontend_app') {
      steps {
        script {
          gv.deploy_frontend_app()
        }
      }
    }
    stage('commit version update') {
      steps {
        script {
          gv.git_deploy()
        }
      }
    }
  }
}