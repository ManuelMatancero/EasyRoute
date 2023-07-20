package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;

import java.util.List;

public interface ZonaService {

    public List<Zona> listAll();
    public Zona getById(Long id);
    public void insert(Zona zona);
    public void update(Zona zona);
    public void delete(Zona zona);
}
