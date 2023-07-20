package com.matancita.sarante.servicio;

import com.matancita.sarante.dao.RecibosGenDao;
import com.matancita.sarante.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecibosGenServiceImpl implements RecibosGenService{

    @Autowired
    RecibosGenDao recibosGenDao;
    @Override
    @Transactional(readOnly = true)
    public List<RecibosGen> listAll() {
        return recibosGenDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public RecibosGen getById(Long id) {
        return recibosGenDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void insert(RecibosGen recibosGen) {
        recibosGenDao.save(recibosGen);
    }

    @Override
    @Transactional
    public void update(RecibosGen recibosGen) {
        recibosGenDao.save(recibosGen);
    }

    @Override
    @Transactional
    public void delete(RecibosGen recibosGen) {
        recibosGenDao.delete(recibosGen);
    }
}
