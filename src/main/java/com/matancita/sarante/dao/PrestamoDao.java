package com.matancita.sarante.dao;


import com.matancita.sarante.domain.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoDao extends JpaRepository<Prestamo, Long> {
}
