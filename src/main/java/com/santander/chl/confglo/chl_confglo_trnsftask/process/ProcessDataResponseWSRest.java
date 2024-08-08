package com.santander.chl.confglo.chl_confglo_trnsftask.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.santander.chl.confglo.chl_confglo_trnsftask.dto.DataWSRest;

@Component
public class ProcessDataResponseWSRest implements Processor {

	private static Logger log = LoggerFactory.getLogger(ProcessDataResponseWSRest.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		DataWSRest dataWSRest = exchange.getIn().getBody(DataWSRest.class);

		if (dataWSRest != null) {
			log.info("Data Web Service lista");
			log.info("Endpoint: " + dataWSRest.getPokemon());
			log.info("Endpoint: " + dataWSRest.getAbility());
			log.info("Endpoint: " + dataWSRest.getBerry());
			log.info("Endpoint: " + dataWSRest.getBerryFlavor());
			log.info("Endpoint: " + dataWSRest.getCharacteristic());
			log.info("Endpoint: " + dataWSRest.getContestEffect());
			log.info("Endpoint: " + dataWSRest.getContestType());
			log.info("Endpoint: " + dataWSRest.getEggGroup());
		}
	}

}
