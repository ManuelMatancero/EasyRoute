package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;

import java.util.List;

public interface PagareService {

    public List<Pagare> listAll();
    public Pagare getById(Long id);
    public void insertAll(List<Pagare> pagare);
    public void update(Pagare pagare);
    public void delete(Pagare pagare);
}
