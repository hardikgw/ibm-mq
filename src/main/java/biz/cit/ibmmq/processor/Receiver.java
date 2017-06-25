package biz.cit.ibmmq.processor;

/**
 * Created by hp on 6/24/17.
 */
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

//@Component
public class Receiver {

    @JmsListener(destination = "DEV.QUEUE.1", containerFactory = "myFactory")
    public void receiveMessage(String email) {
        System.out.println("Received <" + email + ">" + System.currentTimeMillis());
    }

}
