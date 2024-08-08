package com.santander.chl.confglo.chl_confglo_trnsftask;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import com.santander.chl.confglo.chl_confglo_trnsftask.dto.DataWSRest;
import com.santander.chl.confglo.chl_confglo_trnsftask.process.ProcessDataResponseWSRest;

public class CustomRouteWSRestBuilderTest extends CamelTestSupport {

	private JacksonDataFormat jacksonDataFormat;

	public CustomRouteWSRestBuilderTest() {
		jacksonDataFormat = new JacksonDataFormat(DataWSRest.class);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testMockCustomRouteWSRestBuilder001() throws Exception {
		getMockEndpoint("mock:resultProcessDataResponseWSRest001").equals(DataWSRest.class);
		getMockEndpoint("mock:exceptionInProcessResult").assertIsSatisfied();
	}

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {

		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {

				errorHandler(defaultErrorHandler().maximumRedeliveries(0));

				from("timer://simpleTimer?delay=0s&repeatCount=1").to("direct:consumirWSRestTest");

				from("direct:consumirWSRestTest").setHeader(Exchange.HTTP_METHOD, constant("GET"))
						.to("https://pokeapi.co/api/v2/").unmarshal(jacksonDataFormat)
						.process(new ProcessDataResponseWSRest())
						.to("mock:resultProcessDataResponseWSRest001");
				
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
