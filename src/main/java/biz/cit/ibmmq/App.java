package biz.cit.ibmmq;

/**
 * Created by hp on 6/24/17.
 */
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.jms.JmsQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import javafx.scene.control.SelectionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.*;
import java.io.IOException;

//and setting MQEnvironment.hostname and MQEnvironment.channel.


@SpringBootApplication
@Configuration
@EnableJms
public class App {

    @Autowired
    CachingConnectionFactory cachingConnectionFactory;

    @Bean
    public ConnectionFactory connectionFactory() {
        return mqQueueConnectionFactory();
    }

    @Bean
    public QueueConnectionFactory queueConnectionFactory() {
        return mqQueueConnectionFactory();
    }

    @Bean
    public MQQueueConnectionFactory mqQueueConnectionFactory() {
        MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
        mqQueueConnectionFactory.setHostName("mq");
        try {
            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_BINDINGS_THEN_CLIENT);
//            mqQueueConnectionFactory.setCCSID(1208);
            mqQueueConnectionFactory.setQueueManager("QM1");
            mqQueueConnectionFactory.setPort(1414);
            mqQueueConnectionFactory.setChannel("DEV.ADMIN.SVRCONN");
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return mqQueueConnectionFactory;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(CachingConnectionFactory cachingConnectionFactory){
        cachingConnectionFactory.setCacheConsumers(true);
        cachingConnectionFactory.setSessionCacheSize(5);
        cachingConnectionFactory.setReconnectOnException(true);
        return new JmsTransactionManager(cachingConnectionFactory);
    }

    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory(PlatformTransactionManager platformTransactionManager, CachingConnectionFactory cachingConnectionFactory) {
        DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        defaultJmsListenerContainerFactory.setConnectionFactory(cachingConnectionFactory);
        defaultJmsListenerContainerFactory.setTransactionManager(platformTransactionManager);
        defaultJmsListenerContainerFactory.setConcurrency("1-1");
        defaultJmsListenerContainerFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        defaultJmsListenerContainerFactory.setSessionTransacted(true);
        return defaultJmsListenerContainerFactory;
    }

    @Bean
    public UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter(MQQueueConnectionFactory mqQueueConnectionFactory) {
        UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
        userCredentialsConnectionFactoryAdapter.setUsername("admin");
        userCredentialsConnectionFactoryAdapter.setPassword("passw0rd");
        userCredentialsConnectionFactoryAdapter.setTargetConnectionFactory(mqQueueConnectionFactory);
        return userCredentialsConnectionFactoryAdapter;
    }

    @Bean
    public JmsListenerContainerFactory<?> myFactory(CachingConnectionFactory cachingConnectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionTransacted(false);
        factory.setConcurrency("1-1");
        configurer.configure(factory, cachingConnectionFactory);
        return factory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory(UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(userCredentialsConnectionFactoryAdapter);
        cachingConnectionFactory.setCacheConsumers(true);
        cachingConnectionFactory.setSessionCacheSize(5);
        cachingConnectionFactory.setCacheProducers(true);
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    @Bean
    public JmsOperations jmsOperations(CachingConnectionFactory cachingConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
        jmsTemplate.setReceiveTimeout(10);
        return jmsTemplate;
    }

    public static void main(String[] args) {

//        try {
//            MqDirect.benchmark();
//        } catch (MQException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
//
//        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
//        System.out.println("Sending messages.");
//        for (int i = 0; i < 100; i++) {
//            jmsTemplate.convertAndSend("DEV.QUEUE.1", "Hello World");
//        }
//
////        while (true) {
////            jmsTemplate.convertAndSend("DEV.QUEUE.1", "Hello World");
////        };
//
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            jmsTemplate.receive("DEV.QUEUE.1");
//        }

//        System.out.println("Avg time for 100 messages (JMS Template): " + String.valueOf((System.currentTimeMillis() - start)/100));

    }

}