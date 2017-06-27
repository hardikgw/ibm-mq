package biz.cit.ibmmq;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;

import java.io.IOException;

/**
 * Created by hp on 6/27/17.
 */
public class MqDirect {

    public static void benchmark() throws MQException, IOException {

        MQEnvironment.hostname = "localhost";
        MQEnvironment.channel = "DEV.ADMIN.SVRCONN";
        MQEnvironment.port = 1414;
        MQEnvironment.userID ="admin";
        MQEnvironment.password= "passw0rd";

        MQQueueManager queueManager = new MQQueueManager("QM1");

        MQQueue queue = queueManager.accessQueue("DEV.QUEUE.1", CMQC.MQOO_OUTPUT);
        MQMessage myMessage = new MQMessage();
        String message = "Hello World!";
        myMessage.writeInt(message.length());
        myMessage.writeString(message);
        MQPutMessageOptions pmo = new MQPutMessageOptions();
        for (int i = 0; i < 1000; i ++) {
            queue.put(myMessage, pmo);
        }
        queue.close();
        queue = queueManager.accessQueue("DEV.QUEUE.1", CMQC.MQOO_INPUT_SHARED);
        MQMessage theMessage    = new MQMessage();
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        long start = System.currentTimeMillis();
        for (int i=0; i < 1000; i++) {
            queue.get(theMessage, gmo);
            int strLen = theMessage.readInt();
            byte[] strData = new byte[strLen];
//            System.out.println(theMessage.readStringOfByteLength(strLen));
        }
        System.out.println("Avg time for 100 messages (Direct): " + String.valueOf((System.currentTimeMillis() - start)/1000.00));
        queue.close();
        queueManager.disconnect();

    }

}
