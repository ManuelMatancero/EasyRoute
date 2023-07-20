package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;

import java.util.List;

public interface RecibosGenService {

    public List<RecibosGen> listAll();
    public RecibosGen getById(Long id);
    public void insert(RecibosGen recibosGen);
    public void update(RecibosGen recibosGen);
    public void delete(RecibosGen recibosGen);
}
