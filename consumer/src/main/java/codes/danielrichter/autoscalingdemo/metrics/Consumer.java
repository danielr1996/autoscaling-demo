package codes.danielrichter.autoscalingdemo.metrics;

import codes.danielrichter.autoscalingdemo.common.BusinessMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@ConditionalOnProperty(prefix = "app", name = "metrics", havingValue = "false")
public class Consumer {
	@Value("${app.queue.rate.amount}")
	private int amount;

	@Value("${app.queue.rate.unit}")
	private ChronoUnit unit;

	@JmsListener(destination = "${app.queue.destination}")
	public void processMessage(BusinessMessage message) throws InterruptedException {
		System.out.println("< " +message);
		sleep(amount);
	}

	private void sleep(int amount) throws InterruptedException {
		if(amount == -1){
			return;
		}
		Duration sleepDuration = Duration.of(1,unit).dividedBy(amount);
		Thread.sleep(sleepDuration);
	}
}
