# DAPS-test bundle

This bundle just a copy from [04-file-transfer](../../src/samples/04-file-transfer) samples but with additional extension needed for DAPS

Extensions that was added:

    implementation(project(":extensions:iam:oauth2:oauth2-core"))
    implementation(project(":extensions:iam:daps"))
    implementation(project(":extensions:filesystem:vault-fs"))

## Docker image

Images can be built with the following command

    docker build -f Dockerfile -t edc-daps-test-consumer ./consumer
    docker build -f Dockerfile -t edc-daps-test-producer ./producer
