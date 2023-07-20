package com.matancita.sarante.servicio;
import com.matancita.sarante.domain.*;

import java.util.List;

public interface RolService {
    public List<Rol> listAll();
    public Rol getById(Long id);
    public void insert(Rol rol);
    public void update(Rol rol);
    public void delete(Rol rol);
}
