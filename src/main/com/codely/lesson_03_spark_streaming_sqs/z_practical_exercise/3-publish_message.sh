aws events put-events \
    --endpoint-url http://localhost:4566 \
    --region us-east-1 \
    --entries '[{
        "EventBusName": "codely.domain_events",
        "Source": "codely",
        "DetailType": "user.registered",
        "Detail": "{ \"detail_type\": \"userRegistered\", \"user_id\": \"123\", \"email\": \"javi@hola.com\", \"timestamp\": \"2023-07-21T10:00:00Z\" }"
    }, {
        "EventBusName": "codely.domain_events",
        "Source": "codely",
        "DetailType": "user.registered",
        "Detail": "{ \"detail_type\": \"userRegistered\", \"user_id\": \"124\", \"email\": \"ana@hola.com\", \"timestamp\": \"2023-07-21T10:01:00Z\" }"
    }, {
        "EventBusName": "codely.domain_events",
        "Source": "codely",
        "DetailType": "user.registered",
        "Detail": "{ \"detail_type\": \"other\", \"user_id\": \"125\", \"email\": \"pep@hola.com\", \"timestamp\": \"2023-07-21T10:02:00Z\" }"
    }]'
