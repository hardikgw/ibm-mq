#!/bin/sh bash
docker run -d -e LICENSE=accept -e MQ_QMGR_NAME=QM1 -p 1414:1414 -p 9443:9443 --name=mq ibmcom/mq
docker exec mq /opt/mqm/java/jre64/jre/bin/java -version
docker cp target/gs-messaging-jms-0.1.0.jar mq:.
docker cp src/main/resources/setjmsenv64.sh mq:/.
docker exec mq chmod 755 setjmsenv64.sh
docker exec mq ./setjmsenv64.sh
docker cp mq.sh mq:/usr/local/bin/.


docker exec mq usr/local/bin/mq-stop-container.sh


#docker exec mq opt/mqm/java/bin/setjmsenv64

docker exec mq /opt/mqm/java/jre64/jre/bin/java -Djava.library.path=/opt/mqm/java/lib64 -jar gs-messaging-jms-0.1.0.jar


mvn install:install-file -Dfile=src/main/resources/ibm/com.ibm.mq.allclient-1.0.0.jar -DgroupId=ibm -DartifactId=all-client -Dversion=1.0.0 -Dpackaging=jar
mvn install:install-file -Dfile=src/main/resources/ibm/jms.jar -DgroupId=ibm -DartifactId=jms -Dversion=1.0.0 -Dpackaging=jar
mvn install:install-file -Dfile=src/main/resources/ibm/com.ibm.mq.traceControl.jar -DgroupId=ibm -DartifactId=traceControl -Dversion=1.0.0 -Dpackaging=jar