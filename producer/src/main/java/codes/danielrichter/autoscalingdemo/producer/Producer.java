package codes.danielrichter.autoscalingdemo.producer;

import codes.danielrichter.autoscalingdemo.common.BusinessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Component
public class Producer {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${app.queue.destination}")
    private String destination;

    @Value("${app.queue.batchsize}")
    private int batchsize;

    @Scheduled(fixedRateString = "${app.queue.rate}")
    public void sendMessage() {
        IntStream
                .rangeClosed(counter.get(), counter.get()+batchsize-1)
                .mapToObj((i) -> new BusinessMessage(counter.getAndIncrement(), "Hello Queue"))
                .peek(message -> jmsTemplate.convertAndSend(destination, message))
                .forEach(message -> System.out.println("> " +message));
    }
}
