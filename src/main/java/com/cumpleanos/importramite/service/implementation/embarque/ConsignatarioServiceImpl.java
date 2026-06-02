package com.cumpleanos.importramite.service.implementation.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.Consignatario;
import com.cumpleanos.importramite.persistence.repository.embarques.ConsignatarioRepository;
import com.cumpleanos.importramite.service.implementation.GenericServiceImpl;
import com.cumpleanos.importramite.service.interfaces.embarque.IConsignatarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsignatarioServiceImpl extends GenericServiceImpl<Consignatario, String> implements IConsignatarioService {

    private final ConsignatarioRepository repository;

    @Override
    public CrudRepository<Consignatario, String> getRepository() {
        return repository;
    }

    @Override
    public List<Consignatario> findAll() {
        return repository.findAllByOrderByNombre();
    }
}