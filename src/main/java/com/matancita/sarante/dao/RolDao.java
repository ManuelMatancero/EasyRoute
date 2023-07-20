package com.matancita.sarante.dao;


import com.matancita.sarante.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolDao extends JpaRepository<Rol, Long> {
}
