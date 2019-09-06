FROM postgres
ADD ./dmp/schema.dmp /docker-entrypoint-initdb.d/schema.dmp
ADD ./dmp/users.dmp /docker-entrypoint-initdb.d/users.dmp
ADD ./dmp/consumer_client.dmp /docker-entrypoint-initdb.d/consumer_client.dmp
ADD ./dmp/n_rule_domain.dmp /docker-entrypoint-initdb.d/n_rule_domain.dmp
ADD ./dmp/n_rule_operand.dmp /docker-entrypoint-initdb.d/n_rule_operand.dmp
ADD ./script/01-initialize.sh /docker-entrypoint-initdb.d/01-initialize.sh