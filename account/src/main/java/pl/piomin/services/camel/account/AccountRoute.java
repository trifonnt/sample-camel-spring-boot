package pl.piomin.services.camel.account;

import org.apache.camel.Exchange;
import org.apache.camel.ShutdownRunningTask;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.piomin.services.camel.account.model.Account;

@Component
public class AccountRoute extends RouteBuilder {

	@Value("${consul-url}")
	private String consulUrl; // http://localhost:8500

	@Value("${port}")
	private int port;

	@Override
	public void configure() throws Exception { 
		
		restConfiguration()
			.component("netty-http")
			.bindingMode(RestBindingMode.json)
			.dataFormatProperty("prettyPrint", "true")
			.port( port );

		// This route is invoked by EventNotifier class!
		from("direct:start").marshal().json(JsonLibrary.Jackson)
			.setHeader(Exchange.HTTP_METHOD, constant("PUT"))
			.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
			.to(consulUrl + "/v1/agent/service/register");

		// This route is invoked by EventNotifier class!
		from("direct:stop").shutdownRunningTask(ShutdownRunningTask.CompleteAllTasks)
			.toD(consulUrl + "/v1/agent/service/deregister/${header.id}");

		rest("/account")
			.get("/{id}")
				.to("bean:accountService?method=findById(${header.id})")
			.get("/customer/{customerId}")
				.to("bean:accountService?method=findByCustomerId(${header.customerId})")
			.get("/")
				.to("bean:accountService?method=findAll")
			.post("/").consumes("application/json").type(Account.class)
				.to("bean:accountService?method=add(${body})");	
	}
}