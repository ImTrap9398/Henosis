package com.sena.henosis.security.Repository;

import com.sena.henosis.security.Model.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Usuario, String> {

    Usuario findByUsername(String username);

    Usuario findByCorreo(String correo);

    Usuario findByRecoveryToken(String recoveryToken);

    boolean existsByUsername(String username); // Agrega este método para verificar si un usuario con el mismo nombre de usuario ya existe

    boolean existsByCorreo(String correo); // Verificar si un correo electrónico ya está en uso

    boolean existsByNumeroIdentificacionUsuario(int numeroIdentificacionUsuario); // Verificar si un correo electrónico ya está en uso
}
