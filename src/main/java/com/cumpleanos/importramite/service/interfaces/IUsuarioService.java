package com.cumpleanos.importramite.service.interfaces;

import com.cumpleanos.importramite.persistence.model.Usuario;

import java.util.List;

public interface IUsuarioService extends IGenericService<Usuario,String> {

    Usuario upsertUsuario(Usuario usuario);

    Usuario addRoles(String idUsario, List<String> roles);

    Usuario removeRoles(String idUsario, List<String> roles);

    Usuario getUserByUserid(String userid);

}
