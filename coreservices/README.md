# Speedboat Onboarding

Backend code for the Catena-X Speedboat Onboarding use case.

Prerequisites to follow this guide (tested with given versions on OS X terminal)

* existing Azure subscription
* zsh 5.7
* azure-cli 2.28
* Terraform 1.0.7
* .NET SDK 3.1
* PostgreSQL client 13.4
* Docker 20.10

## Infrastructure

    cd infrastructure/terraform
    terraform apply --var-file env.dev.tfvars

## Test database

    export PG_HOST=cax-sb-dev-psql.postgres.database.azure.com
    export PG_PASS=Secret+Passw0rd
    caxdb() {
        DBNAME=$1
        shift
        psql "host=$PG_HOST port=5432 user=psqladmin@$PG_HOST password=$PG_PASS sslmode=require dbname=$DBNAME" $@
    }
    caxdb membercompany -c "select version()"

## Create schema and insert data

    cd networkservices/membershipcompany/Resources/ddl
    ./init-db.sh
    caxdb membercompany -c "select bpn, name from member_companies" 

## Run Service

    cd networkservices/onboarding
    docker build -t cax.onboarding -f CatenaX.NetworkServices.Onboarding.Service/Dockerfile .
    docker run --name cax.onboarding -p 9080:80 cax.onboarding
    open http://localhost:9080/WeatherForecast
