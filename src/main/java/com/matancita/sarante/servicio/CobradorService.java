package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;

import java.util.List;

public interface CobradorService {


    public List<Cobrador> listAll();
    public Cobrador getById(Long id);
    public void insert(Cobrador cobrador);
    public void update(Cobrador cobrador);
    public void delete(Cobrador cobrador);
}
