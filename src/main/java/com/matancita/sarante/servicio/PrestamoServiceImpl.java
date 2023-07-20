package com.matancita.sarante.servicio;

import com.matancita.sarante.dao.PrestamoDao;
import com.matancita.sarante.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrestamoServiceImpl implements PrestamoService{

    @Autowired
    PrestamoDao prestamoDao;
    @Override
    @Transactional(readOnly = true)
    public List<Prestamo> listAll() {
        return prestamoDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Prestamo getById(Long id) {
        return prestamoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void insert(Prestamo prestamo) {
        prestamoDao.save(prestamo);
    }

    @Override
    @Transactional
    public void update(Prestamo prestamo) {
        prestamoDao.save(prestamo);
    }

    @Override
    @Transactional
    public void delete(Prestamo prestamo) {
        prestamoDao.delete(prestamo);
    }
}
