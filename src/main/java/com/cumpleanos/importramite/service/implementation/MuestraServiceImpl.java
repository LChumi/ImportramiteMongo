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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return repository.findByRevision_Tramite_Id(tramiteId);
    }

    @Override
    public Muestra saveAndCompare(String barra, String muestra, String tramite, Boolean status) {
        Revision rev = revisionRepository.findByBarraAndTramite_Id(barra, tramite);
        if (rev == null) {
            throw new DocumentNotFoundException("Muestra no encontrada ");
        }
        Muestra mr = repository.findByBarraBultoAndRevision_Tramite_Id(barra, tramite);
        if (mr == null) {
            mr = new Muestra();
            mr.setBarraBulto(barra);
            mr.setBarraMuestra(muestra);
            mr.setRevision(rev);
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
                    throw new DocumentNotFoundException("Barra de muestra no coincide con el historial");
                }
            }
        }
        repository.save(mr);
        return mr;
    }

    @Override
    public List<Muestra> updateWithRevision(String tramite) {
        List<Muestra> muestras = repository.findByRevision_Tramite_Id(tramite);
        List<Revision> revisiones = revisionRepository.findByTramite_IdOrderBySecuenciaAsc(tramite);
        Tramite tr = tramiteRepository.findById(tramite).orElseThrow(() -> new DocumentNotFoundException("Tramite no encontrado"));
        Map<String, Revision> revisionMap = revisiones.stream()
                .collect(Collectors.toMap(Revision::getBarra, rev -> rev));
        Map<String, Muestra> muestraMap = muestras.stream()
                .collect(Collectors.toMap(Muestra::getBarraBulto, muestra -> muestra));

        for (Revision rev : revisiones) {
            Muestra muestra = muestraMap.get(rev.getBarra());

            if (muestra == null) {
                //Si no existe la muestra creo el registro
                muestra = new Muestra();
                muestra.setRevision(rev);
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
        muestras = repository.findByRevision_Tramite_Id(tramite);

        boolean allComplete = muestras.stream().allMatch(muestra -> "COMPLETA".equals(muestra.getProceso()));

        if (allComplete) {
            tr.setProceso((short) 5);
            tramiteRepository.save(tr);
        }

        return repository.findByRevision_Tramite_Id(tramite);
    }

    private static boolean validateMuestra(Muestra muestra) {
        return muestra.getBarraBulto().equals(muestra.getBarraMuestra());
    }
}
