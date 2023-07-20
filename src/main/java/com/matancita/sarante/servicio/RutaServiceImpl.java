package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;
import com.matancita.sarante.dao.RutaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RutaServiceImpl implements RutaService{

    @Autowired
    RutaDao rutaDao;
    @Override
    @Transactional(readOnly = true)
    public List<Ruta> listAll() {
        return rutaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Ruta getById(Long id) {
        return rutaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void insert(Ruta ruta) {
        rutaDao.save(ruta);
    }

    @Override
    @Transactional
    public void update(Ruta ruta) {
        rutaDao.save(ruta);
    }

    @Override
    @Transactional
    public void delete(Ruta ruta) {
        rutaDao.delete(ruta);
    }
}
