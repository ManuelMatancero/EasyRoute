package com.matancita.sarante.servicio;
import com.matancita.sarante.domain.*;

import java.util.List;

public interface RutaService {
    public List<Ruta> listAll();
    public Ruta getById(Long id);
    public void insert(Ruta ruta);
    public void update(Ruta ruta);
    public void delete(Ruta ruta);
}
