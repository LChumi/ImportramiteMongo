package com.cumpleanos.importramite.service.http;

import com.cumpleanos.importramite.persistence.api.EmailRecord;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "notification-service", url = "http://192.168.112.245:8083")
public interface EmailClient {

    @PostMapping("/enviar/adjunto")
    public ResponseEntity<String> enviarMailAdjunto(@Valid @RequestBody EmailRecord email,
                                                    @RequestParam("file") MultipartFile file,
                                                    @RequestParam("filename") String filename);
}
