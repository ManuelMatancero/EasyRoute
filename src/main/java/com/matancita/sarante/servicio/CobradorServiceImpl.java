package com.matancita.sarante.servicio;

import com.matancita.sarante.dao.CobradorDao;
import com.matancita.sarante.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CobradorServiceImpl implements CobradorService{

    @Autowired
    CobradorDao cobradorDao;
    @Override
    @Transactional(readOnly = true)
    public List<Cobrador> listAll() {
        return cobradorDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cobrador getById(Long id) {
        return cobradorDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Long insert(Cobrador cobrador) {
        return cobradorDao.save(cobrador).getIdCobrador();
    }

    @Override
    @Transactional
    public void update(Cobrador cobrador) {
        cobradorDao.save(cobrador);
    }

    @Override
    @Transactional
    public void delete(Cobrador cobrador) {
        cobradorDao.delete(cobrador);
    }
}
