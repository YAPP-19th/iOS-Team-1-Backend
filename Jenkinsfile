pipeline {
  agent any
  stages {
    stage('db restart') {
      parallel {
        stage('db restart') {
          steps {
            sh '''docker rm -f postgres
docker rm -f redis
docker run -p 5432:5432 --name postgres -e POSTGRES_HOST_AUTH_METHOD=$POSTGRES_HOST_AUTH_METHOD -e POSTGRES_DB=$POSTGRES_DB -e POSTGRES_USER=$POSTGRES_USER -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD -d vixx170627/postgres-ko
docker run -p 6379:6379 --name redis -d redis'''
          }
        }

        stage('setting properties') {
          steps {
            sh 'echo Yet'
          }
        }

      }
    }

    stage('build') {
      steps {
        sh './gradlew build'
      }
    }

    stage('docker build') {
      steps {
        sh '''docker build --tag yapp19th/develop .
docker push yapp19th/develop
docker rmi -f $(docker images -f "dangling=true" -q)'''
      }
    }

    stage('db stop') {
      steps {
        sh '''docker rm -f postgres
docker rm -f redis'''
      }
    }

  }
}