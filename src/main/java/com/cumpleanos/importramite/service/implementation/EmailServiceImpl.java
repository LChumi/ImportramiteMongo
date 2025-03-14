package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Emails;
import com.cumpleanos.importramite.persistence.repository.EmailRepository;
import com.cumpleanos.importramite.service.interfaces.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

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
        return repository.findByTipo(tipo);
    }
}
