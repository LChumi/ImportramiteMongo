package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Usuario;
import com.cumpleanos.importramite.persistence.repository.UsuarioRepository;
import com.cumpleanos.importramite.service.interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl extends GenericServiceImpl<Usuario,String> implements IUsuarioService {

    private final UsuarioRepository repository;


    @Override
    public CrudRepository<Usuario, String> getRepository() {
        return repository;
    }

    @Override
    public Usuario upsertUsuario(Usuario usuario) {
        return repository.findByIdUsuario(usuario.getIdUsuario()).orElse(
                repository.save(usuario)
        );
    }

    @Override
    public Usuario addRoles(String idUsuario, List<String> nuevosRoles) {
        Usuario usuario = repository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<String> rolesActuales = usuario.getRoles();
        if (rolesActuales == null) {
            rolesActuales = new ArrayList<>();
        }

        // Agregar sin duplicar
        for (String rol : nuevosRoles) {
            if (!rolesActuales.contains(rol)) {
                rolesActuales.add(rol);
            }
        }

        usuario.setRoles(rolesActuales);
        return repository.save(usuario);
    }

    @Override
    public Usuario removeRoles(String idUsuario, List<String> rolesAEliminar) {
        Usuario usuario = repository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<String> rolesActuales = usuario.getRoles();
        if (rolesActuales != null) {
            rolesActuales.removeAll(rolesAEliminar);
        }

        usuario.setRoles(rolesActuales);
        return repository.save(usuario);
    }

    @Override
    public Usuario getUserByUserid(String userid) {
        return repository.findByIdUsuario(userid).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }


}
