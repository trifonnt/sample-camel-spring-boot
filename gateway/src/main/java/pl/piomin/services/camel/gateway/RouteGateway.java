package pl.piomin.services.camel.gateway;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
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
		ConsulConfigurationDefinition consulConfig = new ConsulConfigurationDefinition();
		consulConfig.setComponent("netty4-http");
		consulConfig.setUrl( consulUrl );
		context.setServiceCallConfiguration( consulConfig );

		restConfiguration()
			.component("netty4-http")
			.bindingMode(RestBindingMode.json)
			.dataFormatProperty("prettyPrint", "true")
			.port( port );

//		from("rest:get:hello/{me}").transform().simple("Bye ${header.me}"); // WORKS! http://localhost:8000/hello/trifon
		from("rest:get:hello:/{me}").transform().simple("Hi ${header.me}"); // WORKS! http://localhost:8000/hello/trifon
		from("rest:get:hello:/french/{me}").transform().simple("Bonjour ${header.me}"); // WORKS! http://localhost:8000/hello/french/trifon

		// - http://localhost:8000/account-new WORKS!!!
		from("rest:get:account-new")
			.serviceCall("account-service", "http://account-service/account?connectTimeout=1000&bridgeEndpoint=true");

		// - http://localhost:8000/account-new/ WORKS!!!
		from("rest:get:account-new:")
			.serviceCall("account-service", "http://account-service/account?connectTimeout=1000&bridgeEndpoint=true");

		// WORKS!!!
		//   HTTP Component
		// + http://camel.apache.org/http
		//   serviceCall with exchangeProperty in URI 
		// + http://camel.465427.n5.nabble.com/serviceCall-with-exchangeProperty-in-URI-td5788921.html
//		from("rest:get:account-new:{accountId}") // works!!!
//		from("rest:get:account-new/{accountId}") // works!!!
		from("rest:get:account-new:/{accountId}")
			//.setHeader(Exchange.HTTP_QUERY, simple("${exchangeProperty.accountId}"))
			.setHeader(Exchange.HTTP_PATH, simple("account/${header.accountId}"))
			.serviceCall("account-service", "http://account-service?bridgeEndpoint=true");

		// WORKS!!!
		from("rest:get:account-new:/customer/{customerId}")
			.setHeader(Exchange.HTTP_PATH, simple("account/customer/${header.customerId}"))
			.serviceCall("account-service", "http://account-service?bridgeEndpoint=true");

		from("rest:post:account-new:/")
			.setHeader(Exchange.HTTP_PATH, simple("account"))
			.serviceCall("account-service", "http://account-service?bridgeEndpoint=true");
	}
}