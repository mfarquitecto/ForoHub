package com.forohub.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Page<Usuario> findAllByActivoTrue(Pageable paginacion);

    UserDetails findByLogin(String login);

    boolean existsByLogin(String login);
    boolean existsByEmail(String email);

}