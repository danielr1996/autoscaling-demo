package codes.danielrichter.autoscalingdemo.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class Metrics {
    @Value("${app.queue.destination}")
    private String destination;

    @Bean
    public MeterBinder queueSize(JmsTemplate template){
        return (registry) -> Gauge.builder("queue_length", template,(t)-> t.browse(destination, (BrowserCallback<Integer>) (session, browser) ->Collections.list(browser.getEnumeration()).size())).register(registry);
    }
}
