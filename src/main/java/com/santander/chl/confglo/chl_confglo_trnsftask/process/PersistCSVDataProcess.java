package com.santander.chl.confglo.chl_confglo_trnsftask.process;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.santander.chl.confglo.chl_confglo_trnsftask.entity.Operation;

@Component
public class PersistCSVDataProcess implements Processor {

	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(PersistCSVDataProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		@SuppressWarnings("unchecked")
		List<Operation> operationList = exchange.getIn().getBody(List.class);		

		if (operationList != null) {
			// log.info("Data en PersistCSVDataProcess lista");
			exchange.getIn().setHeader("name", operationList.get(0).getName());
			exchange.getIn().setHeader("money", operationList.get(0).getMoney());
			exchange.getIn().setHeader("theme", operationList.get(0).getTheme());
			// log.info("Procesado...");

		} else {
			// log.info("Data en PersistCSVDataProcess no lista (null)");
		}
	}

}
