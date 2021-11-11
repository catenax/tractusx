#!/bin/bash
PG_HOST=cax-sb-dev-psql.postgres.database.azure.com
caxdb() {
    DBNAME=$1
    shift
    psql "host=$PG_HOST port=5432 user=psqladmin@$PG_HOST password=$PG_PASS sslmode=require dbname=$DBNAME" $@
}

caxdb membercompany -f drop-tables.sql
caxdb membercompany -f create-tables.sql
caxdb membercompany -f insert-business-partners.sql
caxdb membercompany -f insert-member-companies.sql
caxdb membercompany -f insert-member-company-roles.sql
