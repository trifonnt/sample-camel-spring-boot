package pl.piomin.services.camel.gateway;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.remote.ConsulConfigurationDefinition;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RouteGateway extends RouteBuilder {

	@Value("${consul-url}")
	private String consulUrl; // http://localhost:8500

	@Value("${port}")
	private int port; // 8000

	@Autowired
	CamelContext context;

	@Override
	public void configure() throws Exception {
		ConsulConfigurationDefinition config = new ConsulConfigurationDefinition();
		config.setComponent("netty4-http");
		config.setUrl( consulUrl );
		context.setServiceCallConfiguration(config);

		restConfiguration()
			.component("netty4-http")
			.bindingMode(RestBindingMode.json)
			.dataFormatProperty("prettyPrint", "true")
			.port( port );

		from("rest:get:account-new:/{accountId}").serviceCall("account-service", "http://account-service/account/${header.accountId}?bridgeEndpoint=true");
		from("rest:get:account-new:/customer/{customerId}").serviceCall("account-service", "http://account-service/account/customer?bridgeEndpoint=true");
		from("rest:post:account-new:/").serviceCall("account-service", "http://account-service/account?bridgeEndpoint=true");

//		from("rest:get:hello/{me}").transform().simple("Bye ${header.me}"); // WORKS! http://localhost:8000/hello/trifon
		from("rest:get:hello:/{me}").transform().simple("Hi ${header.me}"); // WORKS! http://localhost:8000/hello/trifon
		from("rest:get:hello:/french/{me}").transform().simple("Bonjour ${header.me}"); // WORKS! http://localhost:8000/hello/french/trifon

		// - http://localhost:8000/account-new WORKS!!!
		from("rest:get:account-new").serviceCall("account-service", "http://account-service/account?connectTimeout=1000&bridgeEndpoint=true");

		// - http://localhost:8000/account-new/ WORKS!!!
		from("rest:get:account-new:").serviceCall("account-service", "http://account-service/account?connectTimeout=1000&bridgeEndpoint=true");
	}
}