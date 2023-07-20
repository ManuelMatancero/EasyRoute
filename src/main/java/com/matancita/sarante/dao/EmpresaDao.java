package com.matancita.sarante.dao;


import com.matancita.sarante.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaDao extends JpaRepository<Empresa, Long> {
}
