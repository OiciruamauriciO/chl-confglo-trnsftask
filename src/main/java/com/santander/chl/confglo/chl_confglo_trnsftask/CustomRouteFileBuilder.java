package com.santander.chl.confglo.chl_confglo_trnsftask;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CustomRouteFileBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("timer://foo?delay=0").to("direct:read-input-file-from-directory").end();

		from("direct:read-input-file-from-directory")
				.pollEnrich("file:target/download/?fileName=incomingfile.csv&noop=true&idempotent=false")
				.to("file:files/outputfiledirectory").log("File in output folder").end();

	}

}