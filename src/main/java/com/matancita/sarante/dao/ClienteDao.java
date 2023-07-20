package com.matancita.sarante.dao;

import com.matancita.sarante.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ClienteDao extends JpaRepository<Cliente, Long> {


}
