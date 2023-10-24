# Microservice
The spring boot project consists of API gateway, Service Registry, Config server,Zipkin for tracing and Internal communication between two microservice with the use of RestTemplate
Services: Identity-service, employee-service, Department-service, Api-gateway service, Config-server.
#Zipkin - for log tracing I used Zipkin and sleuth
#Fault tolerance handling - I used Resilience4j Circuit Breaker
#API gateway - i used API gateway for routing the request, load balancing and Also authentication by redirecting request to Identity service.

