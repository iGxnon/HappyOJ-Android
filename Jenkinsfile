pipeline {
  agent any
  stages {
    stage('build') {
      agent {
        docker {
          image 'cimg/android:2021.08.1'
          args '-v $HOME/.m2:/root/.m2 -v $HOME/.gradle:/root/.gradle'
        }

      }
      steps {
        sh './gradlew clean assembleDebug'
        archiveArtifacts(artifacts: 'app/build/outputs/apk/debug/*.apk', allowEmptyArchive: true)
      }
    }

  }
}