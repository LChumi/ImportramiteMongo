package com.cumpleanos.importramite.service.interfaces;


import com.cumpleanos.importramite.persistence.model.Destinatario;
import com.cumpleanos.importramite.persistence.model.Emails;

public interface IEmailService extends IGenericService<Emails, String>{
    Emails getByTipo(Long tipo);
    Emails addAddressee(Long  tipo, Destinatario addressee);
    Emails removeAddressee(Long tipo, String addressee);
}
