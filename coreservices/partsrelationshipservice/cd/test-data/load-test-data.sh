#!/bin/bash

set -euo pipefail

echo "PostgreSQL host: $POSTGRES_HOST"
echo "PostgreSQL database: $POSTGRES_DB"
echo "PostgreSQL user: $POSTGRES_USER"

shutdown_on_error() {
    exit 1
}

trap shutdown_on_error INT TERM ERR

sql_data_file=data.sql.tmp

./generate-test-data.sh > $sql_data_file
psql -v ON_ERROR_STOP=1 "host=$POSTGRES_HOST dbname=$POSTGRES_DB user=$POSTGRES_USER password=$POSTGRES_PASSWORD" -f $sql_data_file
