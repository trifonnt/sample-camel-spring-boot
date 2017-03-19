package pl.piomin.services.camel.account;

import java.util.Arrays;
import java.util.EventObject;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.management.event.CamelContextStartedEvent;
import org.apache.camel.management.event.CamelContextStoppingEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.piomin.services.camel.account.model.Register;

@Component
public class EventNotifier extends EventNotifierSupport {

	@Value("${port}")
	private int port;

	@Override
	public void notify(EventObject event) throws Exception {
		String serviceId = "account" + port;
		if (event instanceof CamelContextStartedEvent) {
			CamelContext context = ((CamelContextStartedEvent) event).getContext();
			ProducerTemplate t = context.createProducerTemplate();
			t.sendBody("direct:start", new Register(serviceId, "account-service", "127.0.0.1", port, Arrays.asList("default")));
		}
		if (event instanceof CamelContextStoppingEvent) {
			CamelContext context = ((CamelContextStoppingEvent) event).getContext();
			ProducerTemplate t = context.createProducerTemplate();
			t.sendBodyAndHeader("direct:stop", null, "id", serviceId);
		}
	}

	@Override
	public boolean isEnabled(EventObject event) {
		return (event instanceof CamelContextStartedEvent || event instanceof CamelContextStoppingEvent);
	}
}