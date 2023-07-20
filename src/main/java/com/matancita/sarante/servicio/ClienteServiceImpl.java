package com.matancita.sarante.servicio;

import com.matancita.sarante.dao.ClienteDao;
import com.matancita.sarante.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService{

    @Autowired
    ClienteDao clienteDao;
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listAll() {
        return clienteDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente getById(Long id) {
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void insert(Cliente cliente) {
        clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public void update(Cliente cliente) {
        clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public void delete(Cliente cliente) {
        clienteDao.delete(cliente);
    }
}
