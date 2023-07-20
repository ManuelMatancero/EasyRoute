package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;

import java.util.List;

public interface EmpresaService {

    public List<Empresa> listAll();
    public Empresa getById(Long id);
    public void insert(Empresa empresa);
    public void update(Empresa empresa);
    public void delete(Empresa empresa);
}
