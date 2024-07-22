#!/bin/bash

./rabbitmqadmin -u codely -p codely -H localhost -P 15672 declare queue name=spark_queue durable=false
