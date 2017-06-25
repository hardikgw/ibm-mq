package biz.cit.ibmmq.service;

import biz.cit.ibmmq.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hp on 6/25/17.
 */
@RestController
@RequestMapping("/jms")
public class Controller {

    @Autowired
    ApplicationContext context;

    @RequestMapping(value = "/halt", method = RequestMethod.GET)
    public @ResponseBody
    String haltJmsListener() {
        JmsListenerEndpointRegistry customRegistry =
                context.getBean(JmsListenerEndpointRegistry.class);
        customRegistry.stop();
        return "Jms Listener Stopped";
    }

    @RequestMapping(value = "/restart", method = RequestMethod.GET)
    public @ResponseBody
    String reStartJmsListener() {
        JmsListenerEndpointRegistry customRegistry =
                context.getBean(JmsListenerEndpointRegistry.class);
        customRegistry.start();
        return "Jms Listener restarted";
    }

    @RequestMapping(value = "/stopApp", method = RequestMethod.GET)
    public @ResponseBody
    String stopApp() {
        String[] args = {};
        SpringApplication.run(App.class, args).close();
        return "stopped";
    }

}