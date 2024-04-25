# Spring boot cloud template using Kotlin 

### This is still work in progress ...

Spring boot Cloud template: keycloak, gateway, etc... 

This setup is intended for local development.

Folder **k8s** contains k8s yaml files intended to be used in local cluster (tested with **kind**)

### Here is a diagram of the template layout

```
                                        +----------+                              
                                        |  Client  |                     
                                        +----------+                                
                                             |                                        
                                      cookie session                             
                                             |
                                       +-----------+         +---------------+  
                                       |  Gateway  + ------- |  Auth Server  |  
                                       +-----------+         +---------------+  
                                             |                       |
                                            jwt                      |      
   +---------------+                         |                       |                           
   |cache/key-value|-------------------------------------------------|                          
   +---------------+       |                                                     
                           |                                                             
                           |            +------------+                                   
   +---------------+       |----------- | Service-A  |                                              
   | Configuration |-------|            +------------+                                              
   |   Service     |       |                                                            
   +---------------+       |            +------------+                               
                           |----------- | Service-B  |                             
                           |            +------------+                           
   +---------------+       |                                                        
   |   Database    |       |            +------------+       +----------+       +-------------+
   |    Server     |-------|------------| Service-C  |-------|  Queue   |-------|  Service-Q  |
   +---------------+                    +------------+       |  Server  |       |   Queue     |
                                                             +----------+       |  Consumer   |
                                                                                +-------------+

```


### Backing services

- Gateway: Spring Cloud Gateway
- Service-A: Spring boot application
- cache/key-value: Redis
- Auth Server: Keycloak
- **(TODO)** Service-B: Spring boot application
- **(TODO)** Service-C: Spring boot application - Queue producer
- **(TODO)** Service-Q: Spring boot application - Queue consumer
- **(TODO)** Configuration: Spring boot Central Configuration application
- **(TODO)** database server: (Mysql)
- **(TODO)** Queue: rabbitMq

We only need to setup our keycloak server, we need to:
- create realm: "**sbService**"
- create a client: "**sb-service-edge**" , get **client secret** from keycloak and update edge application.yml
- create a couple of realm roles: **sbAdmin**, **sbUser1**, **sbUser2**
- create a couple of users: **sbadmin**, **sbuser1**, **sbuser2** and link to corresponding roles.


Available endpoints:

- GET ../service-a (public)
- GET ../service-a/auth-user1 (private)
- GET ../service-a/auth-user2 (private)

--

- GET ../service-b (public)
- GET ../service-b/auth-user1 (private)
- GET ../service-b/auth-user2 (private)

--

- GET ../service-c (public)
- GET ../service-c/auth-user1 (private)
- GET ../service-c/auth-user2 (private)


- POST../service-c/queue-job (any authenticated user)

request:
```
{
    "jobType": "...",
    "jobData": [
        {
            "key": "...",
            "value": "..."
        }
    ]
}
```


