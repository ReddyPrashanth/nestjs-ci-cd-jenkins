job('NestJS Docker Setup') {
    scm {
        git('https://github.com/ReddyPrashanth/nestjs-ci-cd-jenkins.git') { node ->
            node / gitConfigName('ReddyPrashanth')
            node / gitConfigEmail('s.prasantreddy94@gmail.com')
        }
    }
    triggers {
        scm('H/5 * * * *')
    }
    wrappers {
        nodejs('nodejs')
    }
    steps {
        dockerBuildAndPublish {
            repositoryName('psreepathi/nestjs-image')
            tag("${GIT_VERSION}, length=9")
            registryCredentials('dockerhub')
            forcePull(false)
            forceTag(false)
            createFingerprints(false)
            skipDecorate()
        }
    }
}