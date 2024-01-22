package codes.danielrichter.autoscalingdemo.consumer;

import codes.danielrichter.autoscalingdemo.common.BusinessMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class Consumer {
	@Value("${app.queue.rate}")
	private Duration rate;

	@JmsListener(destination = "${app.queue.destination}")
	public void processMessage(BusinessMessage message) throws InterruptedException {
		Thread.sleep(rate);
		System.out.println("< " +message);
	}
}
