package com.cumpleanos.importramite;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Revision de Tramites", description = "Documentación de Tramites WEB API v1 MongoDB"
		))
@EnableFeignClients
public class ImportramiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImportramiteApplication.class, args);
	}

}
