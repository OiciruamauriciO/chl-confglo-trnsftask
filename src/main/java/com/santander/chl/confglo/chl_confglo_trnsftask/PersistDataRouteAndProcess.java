package com.santander.chl.confglo.chl_confglo_trnsftask;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.santander.chl.confglo.chl_confglo_trnsftask.entity.Operation;
import com.santander.chl.confglo.chl_confglo_trnsftask.process.PersistCSVDataProcess;

import org.apache.camel.component.jackson.ListJacksonDataFormat;

@Component
public class PersistDataRouteAndProcess extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("timer://simpleTimer?delay=0s&repeatCount=1").to("direct:read-new-csv-file").end();

		from("direct:read-new-csv-file")
				.pollEnrich("file:src/main/resources/todata?fileName=json.json&noop=true&idempotent=false")
				// .log("Documento encontrado: ${body}")
				.unmarshal(new ListJacksonDataFormat(Operation.class))
				// .log("Documento unmarshaleado: ${body}")
				.process(new PersistCSVDataProcess())
				// .log("Documento procesado: ${body}")
				.split(body())
				.to("jpa:com.santander.chl.confglo.chl_confglo_trnsftask.entity.Operation?persistenceUnit=postgresql&usePersist=true&flushOnSend=true")
				// .log("Documento procesado con flushOnSend=true: ${body}")
				.end();

		/*
		 * from(
		 * "jpa:com.santander.chl.confglo.chl_confglo_trnsftask.entity.Operation?persistenceUnit=postgresql&usePersist=true&consumeDelete=false")
		 * .log("Data procesado con consumeDelete=false") .end();
		 */

	}

}
