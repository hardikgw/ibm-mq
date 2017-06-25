# IBM MQ (docker) with Spring Boot 
## Sample project to use Spring Boot with IBM MQ on docker container
## Steps
#### 1. Download allclient.jar and jms.jar from IBM
#### 2. Install dependencies in local maven repo
#### mvn install:install-file -Dfile=src/main/resources/ibm/com.ibm.mq.allclient.jar -DgroupId=ibm -DartifactId=all-client -Dversion=1.0.0 -Dpackaging=jar
#### mvn install:install-file -Dfile=src/main/resources/ibm/jms.jar -DgroupId=ibm -DartifactId=jms -Dversion=1.0.0 -Dpackaging=jar
#### 3. docker run -d -e LICENSE=accept -e MQ_QMGR_NAME=QM1 -p 1414:1414 -p 9443:9443  --name=mq ibmcom/mq
#### 4. git clone https://github.com/hardikgw/ibm-mq.git
#### 5. cd ibm-mq
#### 6. mvn clean install
#### 7. docker cp target/gs-messaging-jms-0.1.0.jar mq:.
#### 8. docker exec mq /opt/mqm/java/jre64/jre/bin/java -jar gs-messaging-jms-0.1.0.jar
