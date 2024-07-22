#!/bin/bash

./rabbitmqadmin -u codely -p codely -H localhost -P 15672 publish exchange=amq.default routing_key=spark_queue payload="Hello, World!"
