package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;

import java.util.List;

public interface PrestamoService {
    public List<Prestamo> listAll();
    public Prestamo getById(Long id);
    public void insert(Prestamo prestamo);
    public void update(Prestamo prestamo);
    public void delete(Prestamo prestamo);
}
