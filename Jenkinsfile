// Pipeline to build the (TSI) Catena-X environment
// uses the original Software Development Cloud library
@Library('sdcloud') _

pipeline {
    agent any
    tools {
        jdk 'jdk11'
    }
    options {
        buildDiscarder(logRotator(daysToKeepStr: '30',numToKeepStr: '5'))     /*This will keep only the last n builds in Jenkins.*/
        disableConcurrentBuilds()                         /*Disallow concurrent executions of the Pipeline*/
        timestamps()                                      /*Prepend all console output generated by the Pipeline run with the time at which the line was emitted*/
    }
    environment {
        /*MAVEN_SETTINGS = '1050e0d9-7514-4db7-a61e-3f78b0c83d0b'        name of maven-setting file*/
        PROJECT_NAME = 'tsi-catenax'
        CATENA_SERVICE_URL = 'catenaxtsidevakssrv.germanywestcentral.cloudapp.azure.com'
        CATENA_PORTAL_URL = 'catenaxtsidevaksportal.germanywestcentral.cloudapp.azure.com'
        CATENA_TENANT = '62c61770-cf81-426f-a4ca-524fbf987ea0'
        APPID = '1e529c78-7363-400f-9005-781c5c1c85f8'
        WORKSPACE = 'dev'
        ENVIRONMENT= 'tsi'
        SHARED_SERVICES_RG = 'shared-services-rg'
        STORAGE_ACCOUNT_NAME = 'catenax${ENVIRONMENT}storage'
        CATENA_ADMIN_MAIL = 'c-jung@t-systems.com'
        POSTGRES_RESOURCE_NAME = 'catenax${ENVIRONMENT}${WORKSPACE}database'
        VERSION = 'latest'
        IMAGE_PULL_POLICY = 'Always'
        CONTAINER_REGISTRY = 'catenax${ENVIRONMENT}acr.azurecr.io'
    }
    stages {

        stage('semantics') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'azure-service-principal', usernameVariable: 'AZURE_PRINCIPAL', passwordVariable: 'AZURE_PASSWORD'),
                    usernamePassword(credentialsId: 'catenax-admin', usernameVariable: 'CATENAX_ADMIN_USER', passwordVariable: 'CATENAX_ADMIN_PASSWORD'),
                    usernamePassword(credentialsId: 'catenax-user', usernameVariable: 'CATENAX_USER', passwordVariable: 'CATENAX_PASSWORD'),
                    string(credentialsId: 'catenaxtsi-shared-storage', variable: 'STORAGE_ACCOUNT_KEY'),
                ]) {
                    withMaven(
                                maven: 'maven',
                                options: [artifactsPublisher(disabled: true),
                                  findbugsPublisher(disabled: true),
                                  openTasksPublisher(disabled: true),
                                  junitPublisher(disabled: true),
                                  pipelineGraphPublisher(disabled: true),
                                  invokerPublisher(disabled: true)]
                    ) {
                        dir("semantics") {
                            sh 'mvn verify'
                            script {
                                docker.withRegistry('https://catenaxtsiacr.azurecr.io', 'azure-service-principal') {
                                    image1 = docker.build('semantics/adapterdev',' -f adapter/Dockerfile .')
                                    image1.push("latest");
                                }
                            } 

                            script {
                                docker.withRegistry('https://catenaxtsiacr.azurecr.io', 'azure-service-principal') {
                                    image1 = docker.build('semantics/servicesdev',' -f services/Dockerfile .')
                                    image1.push("latest");
                                }
                            }
                        }
                        dir("infrastructure") {
                            sh '''docker login -u ${AZURE_PRINCIPAL} -p ${AZURE_PASSWORD} https://catenaxtsiacr.azurecr.io'''
                            sh '''docker pull catenaxtsiacr.azurecr.io/catenax/deploy'''
                            sh '''docker tag catenaxtsiacr.azurecr.io/catenax/deploy catenax/deploy'''
                            sh '''
                                 docker build --progress=plain --no-cache -f Dockerfile.deploy \
                                        --build-arg SERVICE_PRINCIPAL_ID=${AZURE_PRINCIPAL} \
                                        --build-arg SERVICE_PRINCIPAL_SECRET=${AZURE_PASSWORD} \
                                        --build-arg KUBERNETES_TARGET_NAMESPACE=semantics \
                                        --build-arg MANIFEST_FILE=manifests/semantics.yaml \
                                        --build-arg CATENAX_ADMIN_USER=${CATENAX_ADMIN_USER} \
                                        --build-arg CATENAX_ADMIN_PASSWORD=${CATENAX_ADMIN_PASSWORD} \
                                        --build-arg CATENAX_USER=${CATENAX_USER} \
                                        --build-arg CATENAX_PASSWORD=${CATENAX_PASSWORD} \
                                        --build-arg WORKSPACE=dev \
                                        --build-arg ENVIRONMENT=tsi \
                                        --build-arg TENANT=62c61770-cf81-426f-a4ca-524fbf987ea0 \
                                        --build-arg DEPLOYMENTS=semantics,adapter \
                                        . 
                            '''
                        }
                    }
                }
            }
        }

        stage('portal') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'azure-service-principal', usernameVariable: 'AZURE_PRINCIPAL', passwordVariable: 'AZURE_PASSWORD'),
                    usernamePassword(credentialsId: 'catenax-admin', usernameVariable: 'CATENAX_ADMIN_USER', passwordVariable: 'CATENAX_ADMIN_PASSWORD'),
                    usernamePassword(credentialsId: 'catenax-user', usernameVariable: 'CATENAX_USER', passwordVariable: 'CATENAX_PASSWORD'),
                    string(credentialsId: 'catenaxtsi-shared-storage', variable: 'STORAGE_ACCOUNT_KEY'),
                ]) {
                        dir("portal/code/tractus-x-portal") {
                            sh '''
                                echo -n "REACT_APP_BASIC_SERVICES_AUTHENTICATION=" >.env;
                                echo -n "${CATENAX_USER}:${CATENAX_PASSWORD}" | base64 >>.env;
                                echo "" >>.env;
                                echo "REACT_APP_BUSINESSPARTNER_SERVICE_URL=https://${CATENA_SERVICE_URL}/businesspartners/businesspartner" >>.env;
                                echo "REACT_APP_PORTAL_URL=https://${CATENA_PORTAL_URL}/" >>.env;
                                echo "REACT_APP_SEMANTIC_SERVICE_LAYER_URL=https://${CATENA_SERVICE_URL}/semantics/api/v1/" >>.env;
                                echo "REACT_APP_APPLICATION_ID=${APPID}" >>.env;
                                echo "REACT_APP_DEFAULT_TENANT_ID=${CATENA_TENANT}" >>.env;
                                echo "REACT_APP_BROKER_ENDPOINT=https://${CATENA_SERVICE_URL}/connectorprovider" >>.env;
                                echo "REACT_APP_CONNECTOR_ENDPOINT=https://${CATENA_SERVICE_URL}/connectorconsumer" >>.env;
                                echo -n "REACT_APP_CONNECTOR_AUTHENTICATION=" >>.env;
                                echo -n "${CATENAX_ADMIN_USER}:${CATENAX_ADMIN_PASSWORD}" | base64 >> .env;
                                echo "" >>.env;
                            '''
                        
                            script {
                                docker.withRegistry('https://catenaxtsiacr.azurecr.io', 'azure-service-principal') {
                                    image1 = docker.build('frontend/portaldev',' -f Dockerfile .')
                                    image1.push("latest");
                                }
                            }
                        }
                        dir("infrastructure") {
                            sh '''docker login -u ${AZURE_PRINCIPAL} -p ${AZURE_PASSWORD} https://catenaxtsiacr.azurecr.io'''
                            sh '''docker pull catenaxtsiacr.azurecr.io/catenax/deploy'''
                            sh '''docker tag catenaxtsiacr.azurecr.io/catenax/deploy catenax/deploy'''
                            sh '''
                                    docker build --progress=plain --no-cache -f Dockerfile.deploy \
                                    --build-arg SERVICE_PRINCIPAL_ID=${AZURE_PRINCIPAL} \
                                    --build-arg SERVICE_PRINCIPAL_SECRET=${AZURE_PASSWORD} \
                                    --build-arg KUBERNETES_TARGET_NAMESPACE=portal \
                                    --build-arg MANIFEST_FILE=manifests/portal.yaml \
                                    --build-arg CATENAX_ADMIN_USER=${CATENAX_ADMIN_USER} \
                                    --build-arg CATENAX_ADMIN_PASSWORD=${CATENAX_ADMIN_PASSWORD} \
                                    --build-arg CATENAX_USER=${CATENAX_USER} \
                                    --build-arg CATENAX_PASSWORD=${CATENAX_PASSWORD} \
                                    --build-arg WORKSPACE=dev \
                                    --build-arg ENVIRONMENT=tsi \
                                    --build-arg TENANT=62c61770-cf81-426f-a4ca-524fbf987ea0 \
                                    --build-arg DEPLOYMENTS=portal \
                                    . 
                            '''
                        }
                    }
                }
        }
    }
    post {
        always {
            echo 'Job: ' + JOB_NAME + '; Branchname: ' + BRANCH_NAME + '; Project: ' + PROJECT_NAME
            deleteDir() /* clean up our workspace */
        }
        success {
            echo 'Job ' + JOB_NAME + ' in Branch ' + BRANCH_NAME + ' succeeeded!'
        }
        unstable {
            echo 'I am unstable :/'
        }
        failure {
            echo 'I failed :('
            emailext body: "${PROJECT_NAME} build failed, \n please go to ${BUILD_URL} console and check the logs ",
                    subject: "FAILED - ${BRANCH_NAME}: Jenkins Build for ${JOB_NAME} (${BUILD_NUMBER}) failed.",
                    to: 'c-jung@t-systems.com',
                    recipientProviders: [developers()]
        }
        fixed {
            echo 'I fixed :)'
            emailext body: "${PROJECT_NAME} build fixed, \n please go to ${BUILD_URL} console and check the logs ",
                    subject: "FIXED - ${BRANCH_NAME}: Jenkins Build for ${JOB_NAME} (${BUILD_NUMBER}) fixed.",
                    to: 'c-jung@t-systems.com',
                    recipientProviders: [developers()]
        }
        changed {
            echo 'Things were different before...'
        }
    }
}
