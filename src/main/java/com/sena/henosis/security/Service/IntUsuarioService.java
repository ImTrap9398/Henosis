package com.sena.henosis.security.Service;

import com.sena.henosis.security.Model.Usuario;
import java.util.List;
import java.util.Optional;

public interface IntUsuarioService {

    public List<Usuario> Listar();
    public Optional<Usuario> ListarId(String id);
    public int save (Usuario p);
    public void delete(Usuario id);

}