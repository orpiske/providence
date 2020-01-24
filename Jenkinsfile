node("master") {
    def providenceVersion = "0.0.1-SNAPSHOT"

    stage("build") {
        properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '10')), pipelineTriggers([pollSCM('H/15 * * * *')])])

        checkout([$class: 'GitSCM',
                  branches: [[name: '*/master']],
                  doGenerateSubmoduleConfigurations: false,
                  extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'providence']],
                  submoduleCfg: [],
                  userRemoteConfigs: [[credentialsId: '17418727-0198-4097-b160-4792a84ba9f8', url: 'git+ssh://otavio@media-center//home/otavio/repos/providence.git']]])

        sh 'cd providence && ./mvnw -PPackage clean package'

        archiveArtifacts '**/*.tar.gz'
    }

    stage("deploy") {

        checkout([$class: 'GitSCM',
                  branches: [[name: '*/master']],
                  doGenerateSubmoduleConfigurations: false,
                  extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'providence-deploy']],
                  submoduleCfg: [],
                  userRemoteConfigs: [[credentialsId: '17418727-0198-4097-b160-4792a84ba9f8', url: 'git+ssh://otavio@media-center//home/otavio/repos/providence-deploy.git']]])

        sh "cd providence-deploy && PROVIDENCE_COLLECTOR_DOWNLOAD_URL=${BUILD_URL}artifact/providence/providence-collector/target/providence-collector-${providenceVersion}-bin.tar.gz  ansible-playbook -u pi -i inventory/casa providence.yml"

    }

}