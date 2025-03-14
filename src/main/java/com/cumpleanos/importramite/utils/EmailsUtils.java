package com.cumpleanos.importramite.utils;

import com.cumpleanos.importramite.persistence.model.Destinatario;

import java.util.List;

public class EmailsUtils {

    public static String[] addresseeToArray(List<Destinatario> addressee){
        return addressee.stream()
                .map(Destinatario::getDireccion)
                .toArray(String[]::new);
    }
}
