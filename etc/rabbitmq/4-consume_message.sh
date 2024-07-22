#!/bin/bash

QUEUE_NAME="spark_queue"
USER="codely"
PASS="codely"
HOST="localhost"
PORT="15672"

# Consume a message from the queue without auto-ack
response=$(curl -u $USER:$PASS -H "content-type:application/json" \
    -X POST -d '{"count":1,"ackmode":"ack_requeue_true","encoding":"auto","truncate":50000}' \
    http://$HOST:$PORT/api/queues/%2f/$QUEUE_NAME/get)

# Print the message and delivery_tag
echo "Message: $(echo $response | jq -r '.[0].payload')"
