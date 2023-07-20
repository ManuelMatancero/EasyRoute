package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;
import com.matancita.sarante.dao.RolDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolServiceImpl implements RolService {
    @Autowired
    RolDao rolDao;
    @Override
    @Transactional(readOnly = true)
    public List<Rol> listAll() {
        return rolDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Rol getById(Long id) {
        return rolDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void insert(Rol rol) {
        rolDao.save(rol);
    }

    @Override
    @Transactional
    public void update(Rol rol) {
        rolDao.save(rol);
    }

    @Override
    @Transactional
    public void delete(Rol rol) {
        rolDao.delete(rol);
    }
}
