package codes.danielrichter.autoscalingdemo.consumer;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Configuration
@Component
public class Metrics {
    @Value("${app.queue.destination}")
    private String destination;

    @Autowired
    private JmsTemplate template;

    @Bean
    public MeterBinder queueSize(JmsTemplate template){
        return (registry) -> Gauge.builder("queueSize", template,t-> t.browse("messages", (BrowserCallback<Integer>) (session, browser) -> Collections.list(browser.getEnumeration()).size())).register(registry);
    }
}
