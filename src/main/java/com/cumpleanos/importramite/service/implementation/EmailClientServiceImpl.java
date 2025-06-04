package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.service.exception.HttpResponseHandler;
import com.cumpleanos.importramite.service.http.EmailClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class EmailClientServiceImpl {

    private final EmailClient emailClient;

    public void enviarConAdjuntos(MultipartFile email, List<MultipartFile> files, MultipartFile nombresAdjunto) {
        HttpResponseHandler.handle(() -> emailClient.enviarConAdjuntos(email, files, nombresAdjunto ),
                "Error al enviar con adjuntos :" + nombresAdjunto);
    }
}
