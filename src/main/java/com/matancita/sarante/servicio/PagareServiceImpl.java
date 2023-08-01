package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;
import com.matancita.sarante.dao.PagareDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagareServiceImpl implements PagareService{
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
    public List<Pagare> GetAllWithFilters(LocalDateTime startDate, LocalDateTime endDate, Long zonaId, Long rutaId, Long cobradorId, Long empresaId, Long clienteId, Long prestamoId, boolean pagareConRecibo){
        List<Pagare> pagares = pagareDao.findAll();
        List<Pagare> pagaresToReturn = pagares;

        for (Pagare pagare : pagares) {
            if(pagare.getReciboGen() != null){
                if (startDate != null) {
                    if (pagare.getReciboGen().getFecha().isBefore(startDate)) {
                        pagaresToReturn.remove(pagare);
                    }
                }

                if (endDate != null){
                    if (pagare.getReciboGen().getFecha().isAfter(endDate)) {
                        pagaresToReturn.remove(pagare);
                    }
                }
            }
                
            if (prestamoId != null){
                Prestamo prestamo = pagare.getPrestamo();
                if (prestamo.getIdPrestamo() != prestamoId) {
                    pagaresToReturn.remove(pagare);
                }
            }

            if (clienteId != null){
                Cliente cliente = pagare.getPrestamo().getCliente();
                if (cliente.getIdCliente() != clienteId) {
                    pagaresToReturn.remove(pagare);
                }
            }

            if (rutaId != null){
                Ruta ruta = pagare.getPrestamo().getCliente().getRuta();
                if (ruta.getIdRuta() != rutaId) {
                    pagaresToReturn.remove(pagare);
                }
            }

            if (zonaId != null){
                Zona zona = pagare.getPrestamo().getCliente().getRuta().getZona();
                if (zona.getIdZona() != zonaId) {
                    pagaresToReturn.remove(pagare);
                }
            }

            if (zonaId != null){
                Zona zona = pagare.getPrestamo().getCliente().getRuta().getZona();
                if (zona.getIdZona() != zonaId) {
                    pagaresToReturn.remove(pagare);
                }
            }

            if (cobradorId != null){
                Cobrador cobrador = pagare.getPrestamo().getCliente().getRuta().getCobrador();
                if (cobrador.getIdCobrador() != cobradorId) {
                    pagaresToReturn.remove(pagare);
                }
            }

            if (empresaId != null){
                Usuario usuario = userService.getUsuarioByCobradorId(cobradorId);
                if (empresaId != usuario.getEmpresa().getIdEmpresa()) {
                    pagaresToReturn.remove(pagare);
                }
            }

            if (pagareConRecibo && pagare.getReciboGen() == null){
                pagaresToReturn.remove(pagare);
            }
        }

        return pagaresToReturn;
    }
}
