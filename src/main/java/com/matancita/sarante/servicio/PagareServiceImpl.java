package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;
import com.matancita.sarante.dao.PagareDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PagareServiceImpl implements PagareService{

    @Autowired
    PagareDao pagareDao;
    @Override
    @Transactional(readOnly = true)
    public List<Pagare> listAll() {
        return pagareDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagare getById(Long id) {
        return pagareDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void insertAll(List<Pagare> pagare) {
        pagareDao.saveAll(pagare);
    }

    @Override
    @Transactional
    public void update(Pagare pagare) {
        pagareDao.save(pagare);
    }

    @Override
    @Transactional
    public void delete(Pagare pagare) {
        pagareDao.delete(pagare);
    }
}
