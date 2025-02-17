package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Revision;
import com.cumpleanos.importramite.persistence.repository.RevisionRepository;
import com.cumpleanos.importramite.service.interfaces.IRevisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ =  {@Autowired})
public class RevisionServiceImpl extends GenericServiceImpl<Revision, String> implements IRevisionService {

    private final RevisionRepository repository;

    @Override
    public CrudRepository<Revision, String> getRepository() {
        return repository;
    }

}
