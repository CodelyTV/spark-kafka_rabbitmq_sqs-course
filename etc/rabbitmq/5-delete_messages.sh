#!/bin/bash

QUEUE_NAME="spark_queue"
USER="codely"
PASS="codely"
HOST="localhost"
PORT="15672"

# Purge all messages from the queue
curl -u $USER:$PASS -H "content-type:application/json" \
    -X DELETE http://$HOST:$PORT/api/queues/%2f/$QUEUE_NAME/contents
