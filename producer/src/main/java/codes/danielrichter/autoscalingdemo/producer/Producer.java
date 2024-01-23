package codes.danielrichter.autoscalingdemo.producer;

import codes.danielrichter.autoscalingdemo.common.BusinessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Component
public class Producer implements CommandLineRunner {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${app.queue.destination}")
    private String destination;

    @Value("${app.queue.batchsize}")
    private int batchsize;

    @Value("${app.queue.rate.amount}")
    private int amount;

    @Value("${app.queue.rate.unit}")
    private ChronoUnit unit;

    @Override
    public void run(String... args) throws Exception {
        while(true){
            IntStream
                    .rangeClosed(counter.get(), counter.get()+batchsize-1)
                    .mapToObj((i) -> new BusinessMessage(counter.getAndIncrement(), "Hello Queue"))
                    .peek(message -> jmsTemplate.convertAndSend(destination, message))
                    .forEach(message -> System.out.println("> " +message));
            if(batchsize>1){
                System.out.println();
            }
            sleep(batchsize,amount);
        }
    }

    private void sleep(int batchsize, int amount) throws InterruptedException {
        if(amount == -1){
            return;
        }
        Duration sleepDuration = Duration.of(batchsize,unit).dividedBy(amount);
        Thread.sleep(sleepDuration);
    }
}
