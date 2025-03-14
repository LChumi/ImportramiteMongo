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
    public Emails addAddressee(Long tipo, Destinatario addressee) {
        Emails emails = repository.findByTipo(tipo).orElseThrow(() -> new DocumentNotFoundException("No se encontraron Emails"));
        Set<String> existingDestinatarios = new HashSet<>();

        //Cargar direcciones existentes en un HashSet para verificar la unicidad
        emails.getDestinatarios().forEach(addressees -> existingDestinatarios.add(addressees.getDireccion()));

        if (existingDestinatarios.contains(addressee.getDireccion())) {
            throw  new RuntimeException("Destinatario existente");
        }

        emails.getDestinatarios().add(addressee);
        return repository.save(emails);
    }

    @Override
    public Emails removeAddressee(Long tipo, String addressee) {
        Emails emails = repository.findByTipo(tipo).orElseThrow(() -> new DocumentNotFoundException("No se encontraron Emails"));

        //Buscar el destinatario en la lista y eliminarlo
        boolean removed = emails.getDestinatarios().removeIf(destinatario -> destinatario.getDireccion().equals(addressee));

        if (!removed) {
            throw  new RuntimeException("No se encontró Destinatario");
        }

        return repository.save(emails);
    }
}
