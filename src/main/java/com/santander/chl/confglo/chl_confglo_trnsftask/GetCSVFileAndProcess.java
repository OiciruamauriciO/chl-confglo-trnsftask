package com.santander.chl.confglo.chl_confglo_trnsftask;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

import com.santander.chl.confglo.chl_confglo_trnsftask.dto.DataCSVFile;
import com.santander.chl.confglo.chl_confglo_trnsftask.process.ProcessDataResponseCSVFile;

@Component
public class GetCSVFileAndProcess extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		DataFormat bind = new BindyCsvDataFormat(DataCSVFile.class);
		JacksonDataFormat jacksonDataFormat = new JacksonDataFormat(DataCSVFile.class);
		jacksonDataFormat.useList();
		jacksonDataFormat.setUnmarshalType(DataCSVFile.class);

		from("timer://simpleTimer?delay=0s&repeatCount=1").to("direct:read-input-file").end();

		from("direct:read-input-file")
				.pollEnrich("file:target/download/?fileName=incomingfile.csv&noop=true&idempotent=false")
				// .log("Data recibida desde el archivo")
				.unmarshal(bind)
				// .log("Body -> ${body}")
				.marshal().json(JsonLibrary.Jackson, true).convertBodyTo(String.class)
				.to("direct:convert-process-csv-file")
				// .log("Data pushedada a la cola")
				.end();

		from("direct:convert-process-csv-file")
				// .log("Data consumida desde la cola")
				.process(new ProcessDataResponseCSVFile()).unmarshal(jacksonDataFormat)
				// .log("Body -> ${body}")
				.marshal().bindy(BindyType.Csv, DataCSVFile.class)
				// .log("Body to Body -> ${body}")
				.to("file:files/output?fileName=newSampleData.csv")
				// .log("Archivo nuevo csv creado exitosamente")
				.end();
	}

}
