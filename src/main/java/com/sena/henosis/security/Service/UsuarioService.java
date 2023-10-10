package com.sena.henosis.security.Service;


import com.sena.henosis.security.Repository.UserRepository;
import com.sena.henosis.security.Model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IntUsuarioService {

    @Autowired
    private UserRepository data;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public List<Usuario> Listar() {
        return (List<Usuario>)data.findAll();
    }

    @Override
    public Optional<Usuario> ListarId(String id) {
        return data.findById(id);
    }

    @Override
    public int save(Usuario p) {
        // Codificar la contraseña antes de guardarla en la base de datos
        p.setPassword(passwordEncoder.encode(p.getPassword()));

        int res = 0;
        Usuario usuario = data.save(p);
        if (usuario == null) {
            res = 1;
        }
        return res;
    }

    @Override
    public void delete(Usuario id) {

    }

}