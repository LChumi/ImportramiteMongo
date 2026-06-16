package com.cumpleanos.importramite.persistence.model.pos;

import com.cumpleanos.importramite.persistence.api.MedianetRequesDTO;
import com.cumpleanos.importramite.persistence.api.MedianetResponseDTO;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "medianet_pos")
public class MedianetPOS {
    private String id;
    private String empresa;
    private String ruc;
    private String direccion;
    private String ciudad;
    private String red;
    private String telefono;
    private MedianetRequesDTO request;
    private MedianetResponseDTO response;
}