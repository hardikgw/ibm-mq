#!/usr/bin/env bash
docker run -d -e LICENSE=accept -e MQ_QMGR_NAME=QM1 -p 1414:1414 -p 9443:9443  --name=mq ibmcom/mq
docker exec mq /opt/mqm/java/jre64/jre/bin/java -version
docker cp target/gs-messaging-jms-0.1.0.jar mq:.
docker exec mq /opt/mqm/java/jre64/jre/bin/java -jar gs-messaging-jms-0.1.0.jar
mvn install:install-file -Dfile=src/main/resources/ibm/com.ibm.mq.allclient-1.0.0.jar -DgroupId=ibm -DartifactId=all-client -Dversion=1.0.0 -Dpackaging=jar
mvn install:install-file -Dfile=src/main/resources/ibm/jms.jar -DgroupId=ibm -DartifactId=jms -Dversion=1.0.0 -Dpackaging=jar
