# Data store microservice

This application receives data from [Data generator service](https://github.com/qReolq/data-generator-microservice) with Apache Kafka and stores it in Redis.

### Technologies
Spring(Boot), Docker, Apache Kafka, CI/CD(Github actions), Redis

### Usage

You can find Docker compose file in [Data analyser service](https://github.com/qReolq/data-analyser-microservice) <code>docker/docker-compose.yaml</code>.

Application is running on port 8083.

### Environments
To start an application you need to pass variables to .env file.

* <code>REDIS_HOST</code> - Host of Redis instance
* <code>REDIS_PORT</code> - Port of Redis instance
* <code>KAFKA_BOOTSTRAP_SERVERS</code> - Bootstrap server in Kafka
* <code>GROUP_ID</code> - Kafka group id
