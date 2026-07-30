package com.cumpleanos.importramite.service.implementation.dispositivos;

import com.cumpleanos.importramite.persistence.model.dispositivosinv.Dispositivo;
import com.cumpleanos.importramite.persistence.model.dispositivosinv.EstadoDispositivo;
import com.cumpleanos.importramite.persistence.model.dispositivosinv.EstadoPrestamo;
import com.cumpleanos.importramite.persistence.model.dispositivosinv.Prestamo;
import com.cumpleanos.importramite.persistence.repository.dispositivos.DispositivoRepository;
import com.cumpleanos.importramite.persistence.repository.dispositivos.PrestamoRepository;
import com.cumpleanos.importramite.service.implementation.GenericServiceImpl;
import com.cumpleanos.importramite.service.interfaces.dispositivos.IPrestamoService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoServiceImpl extends GenericServiceImpl<Prestamo, String> implements IPrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final DispositivoRepository dispositivoRepository;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository,
                               DispositivoRepository dispositivoRepository) {
        this.prestamoRepository = prestamoRepository;
        this.dispositivoRepository = dispositivoRepository;
    }

    @Override
    public CrudRepository<Prestamo, String> getRepository() {
        return prestamoRepository;
    }

    @Override
    public Prestamo entregar(Prestamo prestamo) {
        Dispositivo dispositivo = dispositivoRepository.findById(prestamo.getDispositivoId())
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo no encontrado"));

        if (dispositivo.getEstadoActual() != EstadoDispositivo.DISPONIBLE) {
            throw new IllegalStateException("El dispositivo no está disponible (estado actual: "
                    + dispositivo.getEstadoActual() + ")");
        }

        prestamo.setSerialDispositivo(dispositivo.getSerial());
        prestamo.setEstado(EstadoPrestamo.ENTREGADO);
        prestamo.setFechaEntrega(prestamo.getFechaEntrega() != null ? prestamo.getFechaEntrega() : LocalDate.now());
        prestamo.setFechaDevolucion(null);
        // fechaEsperadaDevolucion queda como venga: puede ser null si es indefinido
        prestamo.setCreatedAt(LocalDateTime.now());

        Prestamo guardado = prestamoRepository.save(prestamo);

        dispositivo.setEstadoActual(EstadoDispositivo.EN_USO);
        dispositivo.setUpdatedAt(LocalDateTime.now());
        dispositivoRepository.save(dispositivo);

        return guardado;
    }

    @Override
    public Prestamo devolver(String prestamoId, EstadoDispositivo estadoFinalDispositivo, String observaciones) {
        Prestamo prestamo = findById(prestamoId);
        if (prestamo == null) throw new IllegalArgumentException("Préstamo no encontrado");
        if (prestamo.getFechaDevolucion() != null) {
            throw new IllegalStateException("Este préstamo ya fue devuelto");
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        if (observaciones != null) prestamo.setObservaciones(observaciones);
        prestamo.setUpdatedAt(LocalDateTime.now());
        Prestamo actualizado = prestamoRepository.save(prestamo);

        Dispositivo dispositivo = dispositivoRepository.findById(prestamo.getDispositivoId())
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo no encontrado"));
        dispositivo.setEstadoActual(estadoFinalDispositivo); // normalmente DISPONIBLE, o DANADO/MANTENIMIENTO si aplica
        dispositivo.setUpdatedAt(LocalDateTime.now());
        dispositivoRepository.save(dispositivo);

        return actualizado;
    }

    @Override
    public Optional<Prestamo> ocupadoPor(String dispositivoId) {
        return prestamoRepository.findByDispositivoIdAndFechaDevolucionIsNull(dispositivoId);
    }

    @Override
    public List<Prestamo> historialPorSerial(String serial) {
        return prestamoRepository.findBySerialDispositivoOrderByFechaEntregaDesc(serial);
    }

    @Override
    public List<Prestamo> prestamosActivos() {
        return prestamoRepository.findByEstado(EstadoPrestamo.ENTREGADO);
    }
}