package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Muestra;
import com.cumpleanos.importramite.persistence.model.Revision;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.repository.MuestraRepository;
import com.cumpleanos.importramite.persistence.repository.RevisionRepository;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.IMuestraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MuestraServiceImpl extends GenericServiceImpl<Muestra, String> implements IMuestraService {

    private final MuestraRepository repository;
    private final RevisionRepository revisionRepository;
    private final TramiteRepository tramiteRepository;

    @Override
    public CrudRepository<Muestra, String> getRepository() {
        return repository;
    }

    @Override
    public List<Muestra> findByRevision_Tramite_Id(String tramiteId) {
        return repository.findByTramiteId(tramiteId);
    }

    @Override
    public Muestra saveAndCompare(String barra, String muestra, String tramite, Boolean status) {
        Revision rev = revisionRepository.findByBarraAndTramite(barra, tramite);
        if (rev == null) {
            throw new DocumentNotFoundException("Muestra no encontrada ");
        }
        Muestra mr = repository.findByBarraBultoAndTramiteId(barra, tramite);
        if (mr == null) {
            mr = new Muestra();
            mr.setBarraBulto(barra);
            mr.setBarraMuestra(muestra);
            mr.setRevisionId(rev.getId());
            mr.setStatus(validateMuestra(mr));
            mr.setCantidad(1);
            mr.setFecha(LocalDate.now());
            mr.setHora(LocalTime.now());
        } else {
            if (mr.getBarraMuestra() == null || mr.getBarraMuestra().isEmpty()) {
                mr.setBarraMuestra(muestra);
                mr.setStatus(validateMuestra(mr));
                mr.setCantidad(1);
            } else {
                if (mr.getBarraMuestra().equals(muestra)) {
                    if (status) {
                        mr.setCantidad(mr.getCantidad() + 1);
                        mr.setStatus(validateMuestra(mr));
                    } else {
                        int nuevaCantidad = mr.getCantidad() - 1;
                        if (nuevaCantidad >= 0) {
                            mr.setCantidad(mr.getCantidad() - 1);
                            mr.setStatus(validateMuestra(mr));
                        } else {
                            throw new DocumentNotFoundException("Cantidad no puede ser menor que cero");
                        }
                    }
                } else {
                    log.error("Barra de muestra no coincide con el historial");
                    if (mr.getBarrasNovedad() == null) {
                        mr.setBarrasNovedad(new ArrayList<>());
                    }
                    mr.getBarrasNovedad().add(muestra);

                }
            }
        }
        repository.save(mr);
        return mr;
    }

    @Override
    public List<Muestra> updateWithRevision(String tramite) {
        List<Muestra> muestras = repository.findByTramiteId(tramite);
        List<Revision> revisiones = revisionRepository.findByTramiteOrderBySecuenciaAsc(tramite);
        Tramite tr = tramiteRepository.findById(tramite).orElseThrow(() -> new DocumentNotFoundException("Tramite no encontrado"));

        Map<String, Muestra> muestraMap = muestras.stream()
                .collect(Collectors.toMap(Muestra::getBarraBulto, muestra -> muestra));

        for (Revision rev : revisiones) {
            Muestra muestra = muestraMap.get(rev.getBarra());

            if (muestra == null) {
                //Si no existe la muestra creo el registro
                muestra = new Muestra();
                muestra.setRevisionId(rev.getId());
                muestra.setBarraBulto(rev.getBarra());
                muestra.setProceso("FALTANTE");
            } else {
                if (muestra.getBarraMuestra() == null || muestra.getBarraMuestra().isEmpty()) {
                    muestra.setProceso("FALTANTE");
                } else {
                    muestra.setProceso("COMPLETA");
                }
            }
            repository.save(muestra);
        }
        muestras = repository.findByTramiteId(tramite);

        boolean allComplete = muestras.stream().allMatch(muestra -> "COMPLETA".equals(muestra.getProceso()));

        if (allComplete) {
            tr.setProceso((short) 5);
            tramiteRepository.save(tr);
        }

        return repository.findByTramiteId(tramite);
    }

    private static boolean validateMuestra(Muestra muestra) {
        return muestra.getBarraBulto().equals(muestra.getBarraMuestra());
    }
}
