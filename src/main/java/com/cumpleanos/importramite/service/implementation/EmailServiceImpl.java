package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Destinatario;
import com.cumpleanos.importramite.persistence.model.Emails;
import com.cumpleanos.importramite.persistence.repository.EmailRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class EmailServiceImpl extends GenericServiceImpl<Emails, String> implements IEmailService {

    private final EmailRepository repository;

    @Override
    public CrudRepository<Emails, String> getRepository() {
        return repository;
    }

    @Override
    public Emails getByTipo(Long tipo) {
        return repository.findByTipo(tipo).orElseThrow(() -> new DocumentNotFoundException("No se encontraron Emails"));
    }

    @Override
    public Emails addDestinatario(Long tipo, Destinatario destinatario) {
        Emails emails = repository.findByTipo(tipo).orElseThrow(() -> new DocumentNotFoundException("No se encontraron Emails"));
        Set<String> existingDestinatarios = new HashSet<>();

        //Cargar direcciones existentes en un HashSet para verificar la unicidad
        emails.getDestinatarios().forEach(destinatarios -> existingDestinatarios.add(destinatarios.getDireccion()));

        if (existingDestinatarios.contains(destinatario.getDireccion())) {
            throw  new RuntimeException("Destinatario existente");
        }

        emails.getDestinatarios().add(destinatario);
        return repository.save(emails);
    }
}
