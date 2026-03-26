package com.cumpleanos.importramite.service.implementation.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ConfiteriaDetalle;
import com.cumpleanos.importramite.persistence.model.confiteria.ReposicionConfiteria;
import com.cumpleanos.importramite.persistence.records.ReposicionRequest;
import com.cumpleanos.importramite.persistence.repository.confiteria.ConfiteriaDetalleRepository;
import com.cumpleanos.importramite.persistence.repository.confiteria.ReposicionConfiteriaRepository;
import com.cumpleanos.importramite.service.implementation.GenericServiceImpl;
import com.cumpleanos.importramite.service.interfaces.confiteria.IConfiteriaDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfiteriaDetallaServiceImpl extends GenericServiceImpl<ConfiteriaDetalle, String> implements IConfiteriaDetalleService {

    private final ConfiteriaDetalleRepository repository;
    private final ReposicionConfiteriaRepository reposicionRepository;
    private final ExelConfiteriaService excelService;

    @Override
    public CrudRepository<ConfiteriaDetalle, String> getRepository() {
        return repository;
    }

    /**
     * Consulta: solo lectura, optimizada con readOnly.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ConfiteriaDetalle> findByReposicionId(String reposisicionId) {
        return repository.findByReposicionId(reposisicionId);
    }

    /**
     * Escritura: guarda reposición y detalles en una transacción completa.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ConfiteriaDetalle> saveList(ReposicionRequest request) {
        request.repo().setFecha(LocalDate.now());
        ReposicionConfiteria repo = reposicionRepository.save(request.repo());
        for (ConfiteriaDetalle item : request.detalles()) {
            item.setReposicionId(repo.getId());
            repository.save(item);
        }
        return repository.findByReposicionId(repo.getId());
    }

    /**
     * Consulta por rango de fechas: también lectura.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReposicionConfiteria> findByFechaBetween(LocalDate fechaAfter, LocalDate fechaBefore) {
        return reposicionRepository.findByFechaBetween(fechaAfter, fechaBefore);
    }

    @Override
    public byte[] getExcelConfiteria(String reposisicionId) {
        ReposicionConfiteria repo = reposicionRepository.findById(reposisicionId).orElseThrow(() -> new RuntimeException(reposisicionId + " no encontrado"));

        List<ConfiteriaDetalle> detalles = repository.findByReposicionId(repo.getId());

        ReposicionRequest request = new ReposicionRequest(repo, detalles);

        return excelService.generarExcelConfiteria(request);
    }
}