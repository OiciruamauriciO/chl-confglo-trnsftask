package com.santander.chl.confglo.chl_confglo_trnsftask;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

import com.santander.chl.confglo.chl_confglo_trnsftask.dto.DataCSVFile;

@Component
public class GetJSONFileAndProcess extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// Configura propiedades del componente
		getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");

		// Apague más rápido en caso de que se acumulen mensajes en vuelo
		getContext().getShutdownStrategy().setTimeout(10);

		DataFormat bind = new BindyCsvDataFormat(DataCSVFile.class);

		from("timer://simpleTimer?delay=0s&repeatCount=1").to("direct:read-new-csv-file-to-save-json-file").end();

		from("direct:read-new-csv-file-to-save-json-file")
				.pollEnrich("file:files/output/?fileName=newSampleData.csv&noop=true&idempotent=false")
				// .log("Documento con Body a marshalear -> ${body}")
				.unmarshal(bind).marshal().json(JsonLibrary.Jackson)
				// .log("Documento marshaleado a json: ${body}")
				.to("file:src/main/resources/todata?fileName=json.json")
				// .log("Guardado")
				.end();

		from("{{ftp.custom}}").unmarshal(bind).marshal().json(JsonLibrary.Jackson, true).convertBodyTo(String.class)
				.end();
	}
}
