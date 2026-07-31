package com.cumpleanos.importramite.service.implementation.dispositivos;

import com.cumpleanos.importramite.persistence.model.dispositivosinv.Dispositivo;
import com.cumpleanos.importramite.persistence.model.dispositivosinv.EstadoDispositivo;
import com.cumpleanos.importramite.persistence.repository.dispositivos.DispositivoRepository;
import com.cumpleanos.importramite.service.implementation.GenericServiceImpl;
import com.cumpleanos.importramite.service.interfaces.dispositivos.IDispositivoService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DispositivoServiceImpl extends GenericServiceImpl<Dispositivo, String> implements IDispositivoService {

    private final DispositivoRepository dispositivoRepository;

    public DispositivoServiceImpl(DispositivoRepository dispositivoRepository) {
        this.dispositivoRepository = dispositivoRepository;
    }

    @Override
    public CrudRepository<Dispositivo, String> getRepository() {
        return dispositivoRepository;
    }

    @Override
    public Dispositivo registrar(Dispositivo dispositivo) {
        if (dispositivoRepository.existsBySerial(dispositivo.getSerial())) {
            throw new IllegalArgumentException("Ya existe un dispositivo con ese S/N: " + dispositivo.getSerial());
        }
        dispositivo.setEstadoActual(EstadoDispositivo.DISPONIBLE);
        dispositivo.setActivo(true);
        dispositivo.setCreatedAt(LocalDateTime.now());
        return dispositivoRepository.save(dispositivo);
    }

    @Override
    public List<Dispositivo> disponibles() {
        return dispositivoRepository.findByEstadoActualAndActivoTrue(EstadoDispositivo.DISPONIBLE);
    }

    @Override
    public List<Dispositivo> disponiblesPorMarca(String marca) {
        return dispositivoRepository.findByMarcaAndEstadoActualAndActivoTrue(marca, EstadoDispositivo.DISPONIBLE);
    }

    @Override
    public Dispositivo cambiarEstado(String dispositivoId, EstadoDispositivo nuevoEstado) {
        Dispositivo d = findById(dispositivoId);
        if (d == null) throw new IllegalArgumentException("Dispositivo no encontrado");
        d.setEstadoActual(nuevoEstado);
        d.setUpdatedAt(LocalDateTime.now());
        return dispositivoRepository.save(d);
    }

    @Override
    public Dispositivo actualizar(String dispositivoId, Dispositivo dispositivo) {
        Dispositivo f =  findById(dispositivoId);
        if (f == null) throw new IllegalArgumentException("Dispositivo no encontrado");
        f.setMarca(dispositivo.getMarca());
        f.setSerial(dispositivo.getSerial());
        f.setModelo(dispositivo.getModelo());
        f.setUbicacion(dispositivo.getUbicacion());
        f.setFechaCompra(f.getFechaCompra());
        f.setActivo(dispositivo.isActivo());
        return  dispositivoRepository.save(f);
    }
}