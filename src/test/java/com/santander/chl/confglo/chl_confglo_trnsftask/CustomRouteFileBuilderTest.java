package com.santander.chl.confglo.chl_confglo_trnsftask;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

public class CustomRouteFileBuilderTest extends CamelTestSupport {

	@Test
	public void testMockCustomRouteFileBuilder001() throws Exception {
		getMockEndpoint("mock:resultPollEnrichCustomRouteFileBuilder001").expectedBodiesReceived("incomingfile.csv");
		getMockEndpoint("mock:resultPollEnrichCustomRouteFileBuilder002").expectedBodiesReceived("incomingfile.csv");
		getMockEndpoint("mock:exceptionInProcessResult").assertIsSatisfied();
	}

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {

		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {

				from("direct:read-input-file-from-directory")
						.pollEnrich("file:target/download/?fileName=incomingfile.csv&noop=true&idempotent=false")
						.to("mock:resultPollEnrichCustomRouteFileBuilder001");

				from("file:files/outputfiledirectory?fileName=incomingfile.csv")
						.to("direct:resultPollEnrichCustomRouteFileBuilder002");
				
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
