package com.cumpleanos.importramite.service.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "notification-service", url = "http://192.168.112.245:8083/email")
public interface EmailClient {

    @PostMapping(value = "/enviar/adjunto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> enviarMailAdjunto(@RequestPart("file") MultipartFile file,
                                                    @RequestPart("filename") String filename,
                                                    @RequestPart("email") MultipartFile email);
}

