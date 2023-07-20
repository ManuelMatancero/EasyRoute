package com.matancita.sarante.dao;


import com.matancita.sarante.domain.Pagare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagareDao extends JpaRepository<Pagare, Long> {
}
