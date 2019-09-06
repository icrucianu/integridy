#!/bin/bash
set -e

createuser integridy
createdb -U postgres -O integridy integridy
pg_restore -U postgres -d integridy --clean --if-exists /docker-entrypoint-initdb.d/schema.dmp
pg_restore -U postgres -d integridy --data-only -t users /docker-entrypoint-initdb.d/users.dmp
pg_restore -U postgres -d integridy --data-only -t consumer_client /docker-entrypoint-initdb.d/consumer_client.dmp
pg_restore -U postgres -d integridy --data-only -t n_rule_domain /docker-entrypoint-initdb.d/n_rule_domain.dmp
pg_restore -U postgres -d integridy --data-only -t n_rule_operand /docker-entrypoint-initdb.d/n_rule_operand.dmp
