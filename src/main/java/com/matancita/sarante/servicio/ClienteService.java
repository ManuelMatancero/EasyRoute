package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;


import java.util.List;

public interface ClienteService {

    public List<Cliente> listAll();
    public Cliente getById(Long id);
    public void insert(Cliente cliente);
    public void update(Cliente cliente);
    public void delete(Cliente cliente);
}
