package com.santander.chl.confglo.chl_confglo_trnsftask;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FtpClientRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// Configura propiedades del componente
		getContext().getPropertiesComponent().setLocation("classpath:ftp.properties");

		// Apague más rápido en caso de que se acumulen mensajes en vuelo
		getContext().getShutdownStrategy().setTimeout(10);

		from("file:target/upload?moveFailed=../error")
				// .log("Subiendo archivo ${file:name}")
				.to("{{ftp.client}}")
				// .log("Carga de archivo ${file:name} completa.")
				.end();

		// log.info("Camel enrutará archivos desde el directorio de destino/carga al
		// servidor FTP: " +
		// getContext().resolvePropertyPlaceholders("{{ftp.server}}"));
	}
}
