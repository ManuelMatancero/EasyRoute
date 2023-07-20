package com.matancita.sarante.dao;


import com.matancita.sarante.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioDao extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String usuario);
    
}
