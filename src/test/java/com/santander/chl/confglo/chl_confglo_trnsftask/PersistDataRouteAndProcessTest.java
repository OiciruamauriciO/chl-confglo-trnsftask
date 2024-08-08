package com.santander.chl.confglo.chl_confglo_trnsftask;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import com.santander.chl.confglo.chl_confglo_trnsftask.entity.Operation;
import com.santander.chl.confglo.chl_confglo_trnsftask.process.PersistCSVDataProcess;

public class PersistDataRouteAndProcessTest extends CamelTestSupport {

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testMockPersistDataRouteAndProcess001() throws Exception {
		getMockEndpoint("mock:resultPollEnrichPersistDataRouteAndProcess001").expectedBodiesReceived("json.json");
		getMockEndpoint("mock:resultPollEnrichPersistDataRouteAndProcess002")
				.equals(new ListJacksonDataFormat(Operation.class));
		getMockEndpoint("mock:resultPollEnrichPersistDataRouteAndProcess003").createExchange().equals(List.class);
		getMockEndpoint("mock:resultPollEnrichPersistDataRouteAndProcess003").createExchange().equals(null);
		getMockEndpoint("mock:exceptionInProcessResult").assertIsSatisfied();
	}

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {

		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {

				from("timer://simpleTimer?delay=0s&repeatCount=1").to("direct:read-new-csv-file-001").end();

				from("direct:read-new-csv-file-001")
						.pollEnrich("file:src/main/resources/todata?fileName=json.json&noop=true&idempotent=false")
						.to("mock:resultPollEnrichPersistDataRouteAndProcess001");

				from("timer://simpleTimer?delay=0s&repeatCount=1").to("direct:read-new-csv-file-002").end();

				from("direct:read-new-csv-file-002")
						.pollEnrich("file:src/main/resources/todata?fileName=json.json&noop=true&idempotent=false")
						.unmarshal(new ListJacksonDataFormat(Operation.class))
						.to("mock:resultPollEnrichPersistDataRouteAndProcess002");

				from("timer://simpleTimer?delay=0s&repeatCount=1").to("direct:read-new-csv-file-003").end();

				from("direct:read-new-csv-file-003")
						.pollEnrich("file:src/main/resources/todata?fileName=json.json&noop=true&idempotent=false")
						.unmarshal(new ListJacksonDataFormat(Operation.class)).process(new PersistCSVDataProcess())
						.to("mock:resultPollEnrichPersistDataRouteAndProcess003");
				
				from("direct:exceptionInProcessResult").to("mock:exceptionInProcessResult");

			}
		};
	}

	@Override
	protected CamelContext createCamelContext() throws Exception {
		DefaultCamelContext defaultCamelContext = new DefaultCamelContext();
		assertValidContext(defaultCamelContext);
		return defaultCamelContext;
	}
}
