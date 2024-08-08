package com.santander.chl.confglo.chl_confglo_trnsftask;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import com.santander.chl.confglo.chl_confglo_trnsftask.dto.DataCSVFile;

public class GetJSONFileAndProcessTest extends CamelTestSupport {

	@Test
	public void testMockGetJSONFileAndProcess001() throws Exception {
		getMockEndpoint("mock:resultPollEnrichGetJSONFileAndProcess001").expectedBodiesReceived("newSampleData.csv");
		getMockEndpoint("mock:resultPollEnrichGetJSONFileAndProcess002").expectedBodiesReceived("json.json");
		getMockEndpoint("mock:resultFtpCustomRouteBuilder001").expectedBodiesReceived("incomingfile.csv");
		getMockEndpoint("mock:exceptionInProcessResult").assertIsSatisfied();
	}

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {

		return new RouteBuilder() {

			@SuppressWarnings("resource")
			@Override
			public void configure() throws Exception {

				getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");
				getContext().getShutdownStrategy().setTimeout(10);

				@SuppressWarnings("unused")
				DataFormat bind = new BindyCsvDataFormat(DataCSVFile.class);

				from("timer://simpleTimer?delay=0s&repeatCount=1").to("direct:read-new-csv-file-to-save-json-file-001");

				from("direct:read-new-csv-file-to-save-json-file-001")
						.pollEnrich("file:files/output/?fileName=newSampleData.csv&noop=true&idempotent=false")
						.to("mock:resultPollEnrichGetJSONFileAndProcess001");

				from("timer://simpleTimer?delay=0s&repeatCount=1").to("direct:read-new-csv-file-to-save-json-file-002");

				from("file:src/main/resources/todata?fileName=json.json")
						.to("mock:resultPollEnrichGetJSONFileAndProcess002");

				from("{{ftp.custom}}").unmarshal(bind).marshal().json(JsonLibrary.Jackson, true)
						.convertBodyTo(String.class).to("direct:resultFtpCustomRouteBuilder001");
				
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
