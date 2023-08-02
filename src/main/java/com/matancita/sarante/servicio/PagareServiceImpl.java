package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;
import com.matancita.sarante.dao.PagareDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PagareServiceImpl implements PagareService {
    @Autowired
    private UsuarioService userService;

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

    @Override
    @Transactional
    public List<Pagare> GetAllWithFilters(LocalDateTime startDate, LocalDateTime endDate, Long zonaId, Long rutaId,
            Long cobradorId, Long empresaId, Long clienteId, Long prestamoId, boolean pagareConRecibo) {
        List<Pagare> pagares = pagareDao.findAll();
        List<Pagare> pagaresToReturn = new ArrayList<>(pagares); // Create a new list

        Iterator<Pagare> iterator = pagaresToReturn.iterator();
        while (iterator.hasNext()) {
            Pagare pagare = iterator.next();
            if (pagare.getReciboGen() != null) {
                if (startDate != null) {
                    if (pagare.getReciboGen().getFecha().isBefore(startDate)) {
                        iterator.remove();
                        continue;
                    }
                }

                if (endDate != null) {
                    if (pagare.getReciboGen().getFecha().isAfter(endDate)) {
                        iterator.remove();
                        continue;
                    }
                }
            }

            if (prestamoId != null) {
                Prestamo prestamo = pagare.getPrestamo();
                if (prestamo.getIdPrestamo() != prestamoId) {
                    iterator.remove();
                    continue;
                }
            }

            if (clienteId != null) {
                Cliente cliente = pagare.getPrestamo().getCliente();
                if (cliente.getIdCliente() != clienteId) {
                    iterator.remove();
                    continue;
                }
            }

            if (rutaId != null) {
                Ruta ruta = pagare.getPrestamo().getCliente().getRuta();
                if (ruta.getIdRuta() != rutaId) {
                    iterator.remove();
                    continue;
                }
            }

            if (zonaId != null) {
                Zona zona = pagare.getPrestamo().getCliente().getRuta().getZona();
                if (zona.getIdZona() != zonaId) {
                    iterator.remove();
                    continue;
                }
            }

            if (cobradorId != null) {
                Cobrador cobrador = pagare.getPrestamo().getCliente().getRuta().getCobrador();
                if (cobrador.getIdCobrador() != cobradorId) {
                    iterator.remove();
                    continue;
                }
            }

            if (empresaId != null) {
                Usuario usuario = userService.getUsuarioByCobradorId(cobradorId);
                if (empresaId != usuario.getEmpresa().getIdEmpresa()) {
                    iterator.remove();
                    continue;
                }
            }

            if (pagareConRecibo && pagare.getReciboGen() == null) {
                iterator.remove();
            }
        }

        return pagaresToReturn;
    }
}