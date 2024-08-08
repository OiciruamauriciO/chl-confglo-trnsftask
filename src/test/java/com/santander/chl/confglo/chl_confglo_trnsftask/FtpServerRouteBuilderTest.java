package com.santander.chl.confglo.chl_confglo_trnsftask;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

public class FtpServerRouteBuilderTest extends CamelTestSupport {

	@Test
	public void testMockFtpClientRouteBuilder001() throws Exception {
		getMockEndpoint("mock:resultFtpServerRouteBuilder").expectedBodiesReceived("incomingfile.csv");
		getMockEndpoint("mock:exceptionInProcessResult").assertIsSatisfied();
	}

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {

		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {

				getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");
				getContext().getShutdownStrategy().setTimeout(10);

				from("{{ftp.server}}").to("direct:resultFtpServerRouteBuilder");
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
