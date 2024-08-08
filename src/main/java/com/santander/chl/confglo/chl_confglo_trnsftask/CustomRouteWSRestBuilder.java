package com.santander.chl.confglo.chl_confglo_trnsftask;
/*
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

import com.santander.chl.confglo.chl_confglo_trnsftask.dto.DataWSRest;
import com.santander.chl.confglo.chl_confglo_trnsftask.process.ProcessDataResponseWSRest;

@Component
public class CustomRouteWSRestBuilder extends RouteBuilder {
	
	private JacksonDataFormat jacksonDataFormat;
	
	public CustomRouteWSRestBuilder() {
		jacksonDataFormat = new JacksonDataFormat(DataWSRest.class);
	}

	@Override
	public void configure() throws Exception {

		errorHandler(defaultErrorHandler().maximumRedeliveries(0));
		
		from("timer:simple?period=1000")
		.to("direct:consumirWSRest")
		.end();
		
		from("direct:consumirWSRest")
		.setHeader(Exchange.HTTP_METHOD,constant("GET")) 
		.to("https://pokeapi.co/api/v2/")
		.unmarshal(jacksonDataFormat)
		//.log("Response WS REST: $(body)")
		.process(new ProcessDataResponseWSRest())
		.end();
		
	}

}
*/

