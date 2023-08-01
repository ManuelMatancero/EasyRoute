package com.matancita.sarante.servicio;

import com.matancita.sarante.domain.*;

import java.time.LocalDateTime;
import java.util.List;

public interface PagareService {

    public List<Pagare> listAll();
    public Pagare getById(Long id);
    public void insertAll(List<Pagare> pagare);
    public void update(Pagare pagare);
    public void delete(Pagare pagare);
    public List<Pagare> GetAllWithFilters(LocalDateTime startDate, LocalDateTime endDate, Long zonaId, Long rutaId, Long cobradorId, Long empresaId, Long clienteId, Long prestamoId, boolean pagareConRecibo);
}
