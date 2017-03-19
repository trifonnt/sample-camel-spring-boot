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

### Check if Consul is running. Open URL
[All services registered in Consul](http://localhost:8500/v1/agent/services)


### Start Account Microservice Service
```shell
cd sample-camel-spring-boot/account
mvn spring-boot:run -Dconsul-url=http://localhost:8500 -Dport=8080
```
