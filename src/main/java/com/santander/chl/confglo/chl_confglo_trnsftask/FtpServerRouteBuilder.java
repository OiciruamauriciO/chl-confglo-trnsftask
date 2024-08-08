package com.santander.chl.confglo.chl_confglo_trnsftask;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FtpServerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// Configura propiedades del componente
		getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");

		// Apague más rápido en caso de que se acumulen mensajes en vuelo
		getContext().getShutdownStrategy().setTimeout(10);

		from("{{ftp.server}}").to("file:target/download")
				// .log("Archivo ${file:name} descargado completamente.")
				.end();

		// log.info("Camel enrutará archivos desde el servidor FTP: " +
		// getContext().resolvePropertyPlaceholders("{{ftp.server}}") + " al directorio
		// de destino/descarga");
	}
}
