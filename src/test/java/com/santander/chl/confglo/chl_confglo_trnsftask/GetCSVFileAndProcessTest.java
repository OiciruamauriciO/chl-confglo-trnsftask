package com.santander.chl.confglo.chl_confglo_trnsftask;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import com.santander.chl.confglo.chl_confglo_trnsftask.dto.DataCSVFile;
import com.santander.chl.confglo.chl_confglo_trnsftask.process.ProcessDataResponseCSVFile;

public class GetCSVFileAndProcessTest extends CamelTestSupport {

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testMockGetCSVFileAndProcess001() throws Exception {
		getMockEndpoint("mock:resultPollEnrichGetCSVFileAndProcess001").expectedBodiesReceived("incomingfile.csv");
		getMockEndpoint("mock:resultPollEnrichPersistDataRouteAndProcess002").equals(String.class);
		getMockEndpoint("mock:resultPollEnrichGetCSVFileAndProcess003").equals(DataCSVFile.class);
		getMockEndpoint("mock:resultFileOutputRouteBuilder").expectedBodiesReceived("newSampleData.csv");
		getMockEndpoint("mock:exceptionInProcessResult").assertIsSatisfied();
	}

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {

		return new RouteBuilder() {

			@SuppressWarnings("resource")
			@Override
			public void configure() throws Exception {

				@SuppressWarnings("unused")
				DataFormat bind = new BindyCsvDataFormat(DataCSVFile.class);
				@SuppressWarnings("resource")
				JacksonDataFormat jacksonDataFormat = new JacksonDataFormat(DataCSVFile.class);
				jacksonDataFormat.useList();
				jacksonDataFormat.setUnmarshalType(DataCSVFile.class);

				from("timer://simpleTimer?delay=0s&repeatCount=1").to("direct:read-input-file-001");

				from("direct:read-input-file-001")
						.pollEnrich("file:target/download/?fileName=incomingfile.csv&noop=true&idempotent=false")
						.to("mock:resultPollEnrichGetCSVFileAndProcess001");

				from("timer://simpleTimer?delay=0s&repeatCount=1").to("direct:read-input-file-002");

				from("direct:read-input-file-002")
						.pollEnrich("file:target/download/?fileName=incomingfile.csv&noop=true&idempotent=false")
						.unmarshal(bind).marshal().json(JsonLibrary.Jackson, true).convertBodyTo(String.class)
						.to("mock:resultPollEnrichGetCSVFileAndProcess002");

				from("timer://simpleTimer?delay=0s&repeatCount=1").to("direct:read-input-file-003");

				from("direct:read-input-file-003")
						.pollEnrich("file:target/download/?fileName=incomingfile.csv&noop=true&idempotent=false")
						.unmarshal(bind).marshal().json(JsonLibrary.Jackson, true).convertBodyTo(String.class)
						.process(new ProcessDataResponseCSVFile()).unmarshal(jacksonDataFormat).marshal()
						.bindy(BindyType.Csv, DataCSVFile.class).to("mock:resultPollEnrichGetCSVFileAndProcess003");

				from("file:files/output?fileName=newSampleData.csv").to("direct:resultFileOutputRouteBuilder");
				
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
