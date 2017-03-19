# sample-camel-spring-boot
Microservices with Apache Camel

## Steps

### Checkout and compile source code 
```shell
git clone https://github.com/trifonnt/sample-camel-spring-boot.git
cd sample-camel-spring-boot
mvn compile
```

### Start docker container with consul
```shell
docker run -d --name consul -p 8500:8500 -p 8600:8600 consul:0.7.5
```

### Check if Consul is running. List all services registered in Consul
[http://localhost:8500/v1/agent/services](http://localhost:8500/v1/agent/services)

### Consul WEB UI
[http://localhost:8500/ui](http://localhost:8500/ui)


### Start Account Microservice Service
```shell
cd sample-camel-spring-boot/account
mvn spring-boot:run -Dconsul-url=http://localhost:8500 -Dport=8080
```
### Check if Account Microservice is running
[http://localhost:8080/account/](http://localhost:8080/account/)


### Start Gateway Service
```shell
cd sample-camel-spring-boot/gateway
mvn spring-boot:run -Dconsul-url=http://localhost:8500 -Dport=8000
```
### Use account service through gateway
[http://localhost:8000/account-new/](http://localhost:8000/account-new/)
