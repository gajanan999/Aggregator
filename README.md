# Aggregator

# TNT Digital

### API Aggregation Service
    This aggregation service takes a input for folowing parameters and provide the combined response. but before combining it satisfy the following criteria
    1. it waits for 5 sec to fill the queue with 5 request
    2. when queue is full i.e queue is size of 5
    it request the API of xyzassessent service and fetch the result from it 
    
    # API 
      http://localhost:8081/api/aggregation?pricing=109347263,123456862&track=109347263,123456862&shipments=109347263,123456862











