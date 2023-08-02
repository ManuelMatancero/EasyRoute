package com.matancita.sarante.web;

import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.matancita.sarante.domain.Cliente;
import com.matancita.sarante.domain.Cobrador;
import com.matancita.sarante.domain.Empresa;
import com.matancita.sarante.domain.Pagare;
import com.matancita.sarante.domain.Prestamo;
import com.matancita.sarante.domain.RecibosGen;
import com.matancita.sarante.domain.Ruta;
import com.matancita.sarante.domain.Usuario;
import com.matancita.sarante.domain.Zona;
import com.matancita.sarante.servicio.ClienteService;
import com.matancita.sarante.servicio.CobradorService;
import com.matancita.sarante.servicio.EmpresaService;
import com.matancita.sarante.servicio.PagareService;
import com.matancita.sarante.servicio.PrestamoService;
import com.matancita.sarante.servicio.RutaService;
import com.matancita.sarante.servicio.ZonaService;

@Controller
public class ControladorCuadres {
    @Autowired
    private PagareService pagareService;

    @Autowired
    private ZonaService zonaService;

    @Autowired
    private RutaService rutaService;

    @Autowired
    private CobradorService cobradorService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping("/cuadres")
    public String cuadres(Model model) {
        List<Pagare> pagares = pagareService.listAll();

        double montoTotal = 0;
        int cantidadPagares = 0;
        double montoPendiente = 0;
        int cantidadPagosPendientes = 0;
        int cantidadZonasVisitadas = 0;
        int cantidadRutasVisitadas = 0;
        int cantidadClientesPagaron = 0;
        int cantidadPrestamos = 0;
        List<Long> prestamosList = null;
        List<Long> clientesList = null;
        List<Long> rutasList = null;
        List<Long> zonasList = null;

        for (Pagare pagare : pagares) {
            if (pagare.getReciboGen() != null) {
                RecibosGen recibo = pagare.getReciboGen();
                LocalDateTime fechaRecibo = recibo.getFecha();
                LocalDateTime fechaActual = LocalDateTime.now();

                if (fechaRecibo.getDayOfMonth() == fechaActual.getDayOfMonth()
                        && fechaRecibo.getMonthValue() == fechaActual.getMonthValue()
                        && fechaRecibo.getYear() == fechaActual.getYear()) {
                    montoTotal += pagare.getTotal();
                    cantidadPagares++;

                    Prestamo prestamo = pagare.getPrestamo();
                    if (prestamo != null && prestamosList != null) {
                        if (!prestamosList.contains(prestamo.getIdPrestamo())) {
                            prestamosList.add(prestamo.getIdPrestamo());
                            cantidadPrestamos++;
                        }
                    }

                    Cliente cliente = prestamo.getCliente();
                    if (cliente != null && clientesList != null) {
                        if (!clientesList.contains(cliente.getIdCliente())) {
                            clientesList.add(cliente.getIdCliente());
                            cantidadClientesPagaron++;
                        }
                    }

                    Ruta ruta = cliente.getRuta();
                    if (ruta != null && rutasList != null) {
                        if (!rutasList.contains(ruta.getIdRuta())) {
                            rutasList.add(ruta.getIdRuta());
                            cantidadRutasVisitadas++;
                        }
                    }

                    Zona zona = ruta.getZona();
                    if (zona != null && zonasList != null) {
                        if (!zonasList.contains(zona.getIdZona())) {
                            zonasList.add(zona.getIdZona());
                            cantidadZonasVisitadas++;
                        }
                    }
                }
            } else {
                if (pagare.getVencimiento().isBefore(LocalDateTime.now())) {
                    montoPendiente += pagare.getTotal();
                    cantidadPagosPendientes++;
                }
            }
        }

        DecimalFormat formatoConComas = new DecimalFormat("#,###.00");
        DecimalFormat formatoConComasEnteros = new DecimalFormat("#,###");
        String montoTotalFormat = "";

        if (montoTotal > 0) {
            montoTotalFormat = formatoConComas.format(montoTotal);
        } else {
            montoTotalFormat = "0.00";
        }
        String montoPendienteFormat = "";
        if (montoPendiente > 0) {
            montoPendienteFormat = formatoConComas.format(montoPendiente);
        } else {
            montoPendienteFormat = "0.00";
        }

        // Filter Options
        List<Zona> zonas = zonaService.listAll();
        List<Ruta> rutas = rutaService.listAll();
        List<Cobrador> cobradores = cobradorService.listAll();
        List<Empresa> empresas = empresaService.listAll();
        List<Cliente> clientes = clienteService.listAll();
        List<Prestamo> prestamos = prestamoService.listAll();

        // Dashboard values
        model.addAttribute("montoTotal", montoPendienteFormat);
        model.addAttribute("cantidadPagares", formatoConComasEnteros.format(cantidadPagares));
        model.addAttribute("montoPendiente", montoPendienteFormat);
        model.addAttribute("cantidadPagosPendientes", formatoConComasEnteros.format(cantidadPagosPendientes));
        model.addAttribute("cantidadZonasVisitadas", formatoConComasEnteros.format(cantidadZonasVisitadas));
        model.addAttribute("cantidadRutasVisitadas", formatoConComasEnteros.format(cantidadRutasVisitadas));
        model.addAttribute("cantidadClientesPagaron", formatoConComasEnteros.format(cantidadClientesPagaron));
        model.addAttribute("cantidadPrestamos", formatoConComasEnteros.format(cantidadPrestamos));

        // Filter attributes
        model.addAttribute("zonas", zonas);
        model.addAttribute("rutas", rutas);
        model.addAttribute("cobradores", cobradores);
        model.addAttribute("empresas", empresas);
        model.addAttribute("clientes", clientes);
        model.addAttribute("prestamos", prestamos);

        // Filter default values
        String startDateField = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endDateField = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("startDateField", startDateField);
        model.addAttribute("endDateField", endDateField);

        return "cuadres";
    }

    @PostMapping("/cuadres")
    public String cuadresFiltrados(
            Model model,
            @RequestParam("start-date") String startDateField,
            @RequestParam("end-date") String endDateField,
            @RequestParam("zona") String zonaIdField,
            @RequestParam("ruta") String rutaIdField,
            @RequestParam("cobrador") String cobradorIdField,
            @RequestParam("empresa") String empresaIdField,
            @RequestParam("cliente") String clienteIdField,
            @RequestParam("prestamo") String prestamoIdField,
            @RequestParam("pagare-con-recibo") String pagareConRecibo) {

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        Long zonaId = null;
        Long rutaId = null;
        Long cobradorId = null;
        Long empresaId = null;
        Long clienteId = null;
        Long prestamoId = null;

        if (!startDateField.isEmpty()) {
            startDate = LocalDateTime.parse(startDateField + "T00:00:00");
        }

        if (!endDateField.isEmpty()) {
            endDate = LocalDateTime.parse(endDateField + "T23:59:59");
        }

        if (!zonaIdField.equals("default")) {
            zonaId = Long.parseLong(zonaIdField);
        }

        if (!rutaIdField.equals("default")) {
            rutaId = Long.parseLong(rutaIdField);
        }

        if (!cobradorIdField.equals("default")) {
            cobradorId = Long.parseLong(cobradorIdField);
        }

        if (!empresaIdField.equals("default")) {
            empresaId = Long.parseLong(empresaIdField);
        }

        if (!clienteIdField.equals("default")) {
            clienteId = Long.parseLong(clienteIdField);
        }

        if (!prestamoIdField.equals("default")) {
            prestamoId = Long.parseLong(prestamoIdField);
        }

        boolean soloPagaresConRecibo = true;
        if (pagareConRecibo.equals("false")) {
            soloPagaresConRecibo = false;
        }

        List<Pagare> pagaresFiltrados = pagareService.GetAllWithFilters(startDate, endDate, zonaId, rutaId, cobradorId,
                empresaId, clienteId, prestamoId, soloPagaresConRecibo);

        double montoTotal = 0;
        int cantidadPagares = 0;
        double montoPendiente = 0;
        int cantidadPagosPendientes = 0;
        int cantidadZonasVisitadas = 0;
        int cantidadRutasVisitadas = 0;
        int cantidadClientesPagaron = 0;
        int cantidadPrestamos = 0;
        List<Long> prestamosList = null;
        List<Long> clientesList = null;
        List<Long> rutasList = null;
        List<Long> zonasList = null;

        for (Pagare pagare : pagaresFiltrados) {
            if (pagare.getReciboGen() != null) {
                RecibosGen recibo = pagare.getReciboGen();
                LocalDateTime fechaRecibo = recibo.getFecha();
                LocalDateTime fechaActual = LocalDateTime.now();

                if (fechaRecibo.getDayOfMonth() == fechaActual.getDayOfMonth()
                        && fechaRecibo.getMonthValue() == fechaActual.getMonthValue()
                        && fechaRecibo.getYear() == fechaActual.getYear()) {
                    montoTotal += pagare.getTotal();
                    cantidadPagares++;

                    Prestamo prestamo = pagare.getPrestamo();
                    if (prestamo != null && prestamosList != null) {
                        if (!prestamosList.contains(prestamo.getIdPrestamo())) {
                            prestamosList.add(prestamo.getIdPrestamo());
                            cantidadPrestamos++;
                        }
                    }

                    Cliente cliente = prestamo.getCliente();
                    if (cliente != null && clientesList != null) {
                        if (!clientesList.contains(cliente.getIdCliente())) {
                            clientesList.add(cliente.getIdCliente());
                            cantidadClientesPagaron++;
                        }
                    }

                    Ruta ruta = cliente.getRuta();
                    if (ruta != null && rutasList != null) {
                        if (!rutasList.contains(ruta.getIdRuta())) {
                            rutasList.add(ruta.getIdRuta());
                            cantidadRutasVisitadas++;
                        }
                    }

                    Zona zona = ruta.getZona();
                    if (zona != null && zonasList != null) {
                        if (!zonasList.contains(zona.getIdZona())) {
                            zonasList.add(zona.getIdZona());
                            cantidadZonasVisitadas++;
                        }
                    }
                }
            } else {
                montoPendiente += pagare.getTotal();
                cantidadPagosPendientes++;
            }
        }

        DecimalFormat formatoConComas = new DecimalFormat("#,###.00");
        DecimalFormat formatoConComasEnteros = new DecimalFormat("#,###");
        String montoTotalFormat = "";

        if (montoTotal > 0) {
            montoTotalFormat = formatoConComas.format(montoTotal);
        } else {
            montoTotalFormat = "0.00";
        }
        String montoPendienteFormat = "";
        if (montoPendiente > 0) {
            montoPendienteFormat = formatoConComas.format(montoPendiente);
        } else {
            montoPendienteFormat = "0.00";
        }

        // Filter Options
        List<Zona> zonas = zonaService.listAll();
        List<Ruta> rutas = rutaService.listAll();
        List<Cobrador> cobradores = cobradorService.listAll();
        List<Empresa> empresas = empresaService.listAll();
        List<Cliente> clientes = clienteService.listAll();
        List<Prestamo> prestamos = prestamoService.listAll();

        // Dashboard values
        model.addAttribute("montoTotal", montoPendienteFormat);
        model.addAttribute("cantidadPagares", formatoConComasEnteros.format(cantidadPagares));
        model.addAttribute("montoPendiente", montoPendienteFormat);
        model.addAttribute("cantidadPagosPendientes", formatoConComasEnteros.format(cantidadPagosPendientes));
        model.addAttribute("cantidadZonasVisitadas", formatoConComasEnteros.format(cantidadZonasVisitadas));
        model.addAttribute("cantidadRutasVisitadas", formatoConComasEnteros.format(cantidadRutasVisitadas));
        model.addAttribute("cantidadClientesPagaron", formatoConComasEnteros.format(cantidadClientesPagaron));
        model.addAttribute("cantidadPrestamos", formatoConComasEnteros.format(cantidadPrestamos));

        // Filter values
        model.addAttribute("startDateField", startDateField);
        model.addAttribute("endDateField", endDateField);
        model.addAttribute("zonaField", zonaId);
        model.addAttribute("rutaField", rutaId);
        model.addAttribute("cobradorField", cobradorId);
        model.addAttribute("empresaField", empresaId);
        model.addAttribute("clienteField", clienteId);
        model.addAttribute("prestamoField", prestamoId);
        model.addAttribute("pagareConReciboField", soloPagaresConRecibo);

        // Filter attributes
        model.addAttribute("zonas", zonas);
        model.addAttribute("rutas", rutas);
        model.addAttribute("cobradores", cobradores);
        model.addAttribute("empresas", empresas);
        model.addAttribute("clientes", clientes);
        model.addAttribute("prestamos", prestamos);

        return "cuadres";
    }
}
