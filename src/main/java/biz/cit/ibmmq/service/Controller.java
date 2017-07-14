package biz.cit.ibmmq.service;

import biz.cit.ibmmq.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hp on 6/25/17.
 */
@RestController
@RequestMapping("/jms")
public class Controller {

    @Autowired
    ApplicationContext context;

    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    String admin(@RequestBody String command) {
        JmsListenerEndpointRegistry jmsRegistry =
                context.getBean(JmsListenerEndpointRegistry.class);
        String response = "";
        switch (command.trim().toLowerCase()) {
            case "start":
                if (!jmsRegistry.isRunning()) {
                    jmsRegistry.start();
                }
                response = "started";
                break;
            case "stop":
                jmsRegistry.stop();
                response = "stopped";
                break;
            default:
                response = jmsRegistry.isRunning() ? "ok" : "down";
        }
        return  response;
    }

    @RequestMapping(value = "/halt", method = RequestMethod.GET)
    public @ResponseBody
    String haltJmsListener() {
        JmsListenerEndpointRegistry jmsRegistry =
                context.getBean(JmsListenerEndpointRegistry.class);
        jmsRegistry.stop();
        return "Jms Listener Stopped";
    }

    @RequestMapping(value = "/restart", method = RequestMethod.GET)
    public @ResponseBody
    String reStartJmsListener() {
        JmsListenerEndpointRegistry jmsRegistry =
                context.getBean(JmsListenerEndpointRegistry.class);
        jmsRegistry.start();
        return "Jms Listener restarted";
    }

    @RequestMapping(value = "/stopApp", method = RequestMethod.GET)
    public @ResponseBody
    String stopApp() {
        String[] args = {};
        SpringApplication.run(App.class, args).close();
        return "stopped";
    }

    @RequestMapping(value = "/post", method = RequestMethod.GET)
    void post() {
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        System.out.println("Sending messages.");
        for (int i = 0; i < 100; i++) {
            jmsTemplate.convertAndSend("DEV.QUEUE.1", "Hello World");
        }
    }
}