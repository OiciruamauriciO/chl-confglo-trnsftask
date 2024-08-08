package com.santander.chl.confglo.chl_confglo_trnsftask.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProcessDataResponseCSVFile implements Processor {

	//private static Logger log = LoggerFactory.getLogger(ProcessDataResponseCSVFile.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		String dataStringCSVFile = exchange.getIn().getBody(String.class);

		if (dataStringCSVFile != null) {
			//log.info("Data CSV archivo como representación String está lista");
			/*
			 * A partir de este punto puede utilizarse otra vía para que la data entrante sea transformada como
			 * String para persistir la información en una base de datos por ejemplo, con ciertas otras particularidades
			 */
			//log.info(dataStringCSVFile);

		} else {
			//log.info(" CSV archivo como representación String está no lista (null)");
		}

	}

}
