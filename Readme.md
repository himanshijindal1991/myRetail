# myRetail RESTFul Service


## Description
1. This project is build on springBoot and exposes a GET endpoint /products/{id}.
2. The project contains a dockerfile that is used to containerize the project. 
3. The project can be deployed in Kubernetes by using deployment.yaml and service.yaml 
files present in k8s-templates folder.
4. Error scenario handling while calling downstream service : 
     a) Has a support to retry calling downstream service 3 times, 
        this retry will occur after certain duration to avoid bottleneck at the downstream level.
        (This scenario totally depends upon a business requirement as it could be a costly operation)
     b) If service won't respond for certain milliseconds, it will get timed out.
   
5.  Mongo db atlas is used as a no-SQL database for the project. 
    As Complex queries needs to be made on this data, so that's why selected row based oriented DB which is "MongoDB".
   
   
## Requirements
For this project to work an environment variable with the name "DOWNSTREAM_URI" has to be created which should
contain the URL of the external API server. The code will append the "{productId}" to this URL
to fetch the data from supplied URL.



## Future Enhancements
1. Needs to have "cache" support also, so before making a query to Mongo DB. It will try to fetch the data 
   from cache as it can "reduce latency". Cache could be implemented on LRU concept which will keep the 
   popular products cached in memory. We can use Redis as a cache.

