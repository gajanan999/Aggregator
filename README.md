# Aggregator

# TNT Digital

### API Aggregation Service
    This aggregation service takes a input for folowing parameters and provide the combined response. but before combining it satisfy the following criteria
    1. it waits for 5 sec to fill the queue with 5 request
    2. when queue is full i.e queue is size of 5
    it request the API of xyzassessent service and fetch the result from it 
    
    # API 
      http://localhost:8081/api/aggregation?pricing=109347263,123456862&track=109347263,123456862&shipments=109347263,123456862
    
    ### Queue handling
        When first request comes with the parameter two 
        for example: http://localhost:8081/api/aggregation?pricing=109347261,123456862&track=109347261,123456862&shipments=109347261,123456862
        
        queues:
        pricing : [109347261,123456862]
        track:  [109347261,123456862]
        shipments: [109347261,123456862]
        
        in above example we can see we have recieved only two parameters. so in this case the service will wait until another request. and fill the queue with 
        parameters. consider we have sent another request.
        
        for example: http://localhost:8081/api/aggregation?pricing=109347263,123456864,123456865&track=109347263,123456864&shipments=109347263,123456864
        
        here queue will be 
        pricing : [109347261,123456862,109347263,123456864,123456865]
        track:  [109347261,123456862,109347263]
        shipments: [109347261,123456862,109347263]

        so here the pricing queue has been filled with the 5 request parameters so it will immedietely trigger the API to backend service for response.
        and other two queues will wait for another request parameters or they will wait for 5 secs. and then they will trigger the APIs.
        
        after getting all the responses from the backend service. the responses will get combined and then sent back to respective requests.
        









