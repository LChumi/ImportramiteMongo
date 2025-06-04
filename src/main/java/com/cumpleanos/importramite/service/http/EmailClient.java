package com.cumpleanos.importramite.service.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "notification-service", url = "http://localhost:8083/email")
public interface EmailClient {

    @PostMapping(value = "/enviar/adjuntos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> enviarConAdjuntos(
            @RequestPart("email") MultipartFile emailJson,
            @RequestPart("attachments") List<MultipartFile> archivos,
            @RequestPart("filenames") MultipartFile nombres
    );
}