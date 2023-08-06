package com.matancita.sarante.web;

import com.matancita.sarante.domain.*;
import com.matancita.sarante.servicio.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private RutaService rutaService;
    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ZonaService zonaService;
    @Autowired
    private PrestamoService prestamoService;
    @Autowired
    private PagareService pagareService;
    @Autowired
    private CobradorService cobradorService;

    @GetMapping("/")
    public String inicio(@AuthenticationPrincipal User user, Model model) {
        List<Ruta> rutas = rutaService.listAll();
        List<Cobrador> cobradores = cobradorService.listAll();
        List<Cliente> clientes = new ArrayList<>();
        List<Cliente> lastTenAdded = new ArrayList<>();
        List<Cliente> lastTenClientesAdded =new ArrayList<>();
        List<Prestamo> prestamos = new ArrayList<>();
        List<Pagare> pagares = new ArrayList<>();
        int cantidadRutas = 0;
        int cantidadCobradores = 0;
        double invertido = 0;
        double cobrado = 0;
        int cantidadPrestamos = 0;
        double ganancias = 0;
        int atrasos = 0;
        int cantidadClientes = 0;
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
        // Sort the list based on the LocalDateTime attribute in descending order
        lastTenAdded = clienteService.listAll();
        lastTenAdded.sort(Comparator.comparing(Cliente::getFechaIngreso).reversed());
        // Select the last 10 added items
        lastTenClientesAdded = lastTenAdded.stream().limit(10).collect(Collectors.toList());
            rutas = rutaService.listAll();
            // Calcular cantidad de rutas
            for (Ruta ruta : rutas) {
                cantidadRutas++;
                clientes = ruta.getClientes();
                for (Cliente cliente : clientes) {
                    cantidadClientes++;
                    prestamos = cliente.getPrestamos();
                    for (Prestamo prestamo : prestamos) {
                        cantidadPrestamos++;
                        invertido += prestamo.getMonto();
                        pagares = prestamo.getPagares();
                        for (Pagare pagare : pagares) {
                            if (!(pagare.getReciboGen() == null)) {
                                ganancias += pagare.getInteres();
                                cobrado += pagare.getTotal();
                            }
                            if (pagare.getVencimiento().isBefore(LocalDateTime.now())
                                    && pagare.getReciboGen() == null) {
                                atrasos++;
                            }
                        }
                    }
                }
            }
            // calcula la cantidad de cobradores
            for (Cobrador cobrador : cobradores) {
                cantidadCobradores++;
            }
        } else {
            Usuario usuario = usuarioService.findByUsuario(user.getUsername());
            rutas = usuario.getCobrador().getRutas();
            // Calcular cantidad de rutas
            for (Ruta ruta : rutas) {
                cantidadRutas++;
                clientes= ruta.getClientes();
                lastTenAdded.addAll(clientes);
                lastTenAdded.sort(Comparator.comparing(Cliente::getFechaIngreso).reversed());
                 // Select the last 10 added items
                lastTenClientesAdded = lastTenAdded.stream().limit(10).collect(Collectors.toList());
                for (Cliente cliente : clientes) {
                    prestamos=cliente.getPrestamos();
                    for (Prestamo prestamo : prestamos) {
                        cantidadPrestamos++;
                        invertido += prestamo.getMonto();
                        pagares = prestamo.getPagares();
                        for (Pagare pagare : pagares) {
                            if (!(pagare.getReciboGen() == null)) {
                                ganancias += pagare.getInteres();
                                cobrado += pagare.getTotal();
                            }
                            if (pagare.getVencimiento().isBefore(LocalDateTime.now())
                                    && pagare.getReciboGen() == null) {
                                atrasos++;
                            }
                        }
                    }
                    cantidadClientes++;
                }
            }
        }

        log.info("ejecutando el controlador Spring MVC");
        log.info("usuario que hizo login:" + user);
        model.addAttribute("cantidadRutas", cantidadRutas);
        model.addAttribute("invertido", invertido);
        model.addAttribute("cantidadCobradores", cantidadCobradores);
        model.addAttribute("cantidadClientes", cantidadClientes);
        model.addAttribute("ganancias", ganancias);
        model.addAttribute("cobrado", cobrado);
        model.addAttribute("cantidadPrestamos", cantidadPrestamos);
        model.addAttribute("atrasos", atrasos);
        model.addAttribute("lastTenClientesAdded", lastTenClientesAdded);
        return "index";
    }

    @GetMapping("/verrutas")
    public String rutas(@AuthenticationPrincipal User user, Model model) {
        List<Zona> zonas = zonaService.listAll();
        List<Ruta> rutas;
        Cobrador cobrador;
        Usuario usuario;
        // Here i check if the user has admin or user privileges
        if (user.getAuthorities()
                .contains(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"))) {
            rutas = rutaService.listAll();
        } else {
            usuario = usuarioService.findByUsuario(user.getUsername());
            rutas = usuario.getCobrador().getRutas();
        }
        List<Cobrador> cobradores = cobradorService.listAll();
        // Numero total de rutas
        int totalRutas = rutas.size();
        int totalClientes = 0;
        int totalPrestamos = 0;
        // Dinero en la ruta
        double dineroPorCobrar = 0;
        for (Ruta ruta : rutas) {
            List<Cliente> clientes = ruta.getClientes();
            for (Cliente cliente : clientes) {
                List<Prestamo> prestamos = cliente.getPrestamos();
                for (Prestamo prestamo : prestamos) {
                    List<Pagare> pagares = prestamo.getPagares();
                    for (Pagare pagare : pagares) {
                        if (pagare.getReciboGen() == null) {
                            dineroPorCobrar += pagare.getTotal();
                        }
                    }
                    totalPrestamos++;
                }
                totalClientes++;
            }
        }
        model.addAttribute("dineroPorCobrar", dineroPorCobrar);
        model.addAttribute("totalRutas", totalRutas);
        model.addAttribute("rutas", rutas);
        model.addAttribute("zonas", zonas);
        model.addAttribute("cobradores", cobradores);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalPrestamos", totalPrestamos);
        // These are the objects to be filled in the form
        model.addAttribute("ruta", new Ruta());
        model.addAttribute("zona", new Zona());
        return "rutas";
    }

    @GetMapping("/verclientes")
    public String clientes(@AuthenticationPrincipal User user, Model model) {
        List<Cliente> clientes = new ArrayList<>();
        Usuario usuario;
        List<Ruta> rutas;
        // Here i check if the user has admin or user privileges
        if (user.getAuthorities()
                .contains(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"))) {
            clientes = clienteService.listAll();
        } else {
            usuario = usuarioService.findByUsuario(user.getUsername());
            rutas = usuario.getCobrador().getRutas();
            for (Ruta ruta : rutas) {
                clientes.addAll(ruta.getClientes());
            }
        }
        model.addAttribute("clientes", clientes);
        model.addAttribute("cliente", new Cliente());
        return "clientesGeneral";
    }

    @GetMapping("/verpagares")
    public String pagares(@AuthenticationPrincipal User user, Model model) {
        List<Pagare> pagares = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        // here i get all the data for pagares for Admin
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            pagares = pagareService.listAll();
        } else {
            Usuario usuario = usuarioService.findByUsuario(user.getUsername());
            List<Ruta> rutas = usuario.getCobrador().getRutas();
            List<Cliente> clientes = new ArrayList<>();
            for (Ruta ruta : rutas) {
                clientes.addAll(ruta.getClientes());
            }
            List<Prestamo> prestamos = new ArrayList<>();
            for (Cliente cliente : clientes) {
                prestamos.addAll(cliente.getPrestamos());
            }
            for (Prestamo prestamo : prestamos) {
                pagares.addAll(prestamo.getPagares());
            }
        }
        model.addAttribute("currentTime", currentTime);
        model.addAttribute("pagares", pagares);
        return "pagares";
    }

    @GetMapping("/verprestamos")
    public String prestamos(@AuthenticationPrincipal User user, Model model) {
        List<Cliente> clientes = new ArrayList<>();
        Usuario usuario;
        List<Ruta> rutas;
        List<Prestamo> prestamos = new ArrayList<>();
        List<Pagare> pagares = new ArrayList<>();
        double totalPendiente = 0;
        int totalPrestamos = 0;
        int pagarespendientes = 0;
        double totalPagado = 0;
        // Here i check if the user has admin or user privileges
        if (user.getAuthorities()
                .contains(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"))) {
            prestamos = prestamoService.listAll();
            for (Prestamo prestamo : prestamos) {
                pagares = prestamo.getPagares();
                for (Pagare pagare : pagares) {
                    if (pagare.getReciboGen() == null) {
                        totalPendiente += pagare.getTotal();
                        pagarespendientes++;
                    } else {
                        totalPagado += pagare.getTotal();
                    }
                }
                totalPrestamos++;
            }
        } else {
            usuario = usuarioService.findByUsuario(user.getUsername());
            rutas = usuario.getCobrador().getRutas();
            for (Ruta ruta : rutas) {
                clientes.addAll(ruta.getClientes());
            }
            for (Cliente c : clientes) {
                prestamos.addAll(c.getPrestamos());
            }
            for (Prestamo p : prestamos) {
                pagares.addAll(p.getPagares());
                totalPrestamos++;
            }
            for (Pagare p : pagares) {
                if (p.getReciboGen() == null) {
                    totalPendiente += p.getTotal();
                    pagarespendientes++;
                } else {
                    totalPagado += p.getTotal();
                }
            }
        }
        model.addAttribute("totalPendiente", totalPendiente);
        model.addAttribute("totalPrestamos", totalPrestamos);
        model.addAttribute("pagaresPendientes", pagarespendientes);
        model.addAttribute("totalPagado", totalPagado);
        model.addAttribute("prestamos", prestamos);
        return "prestamosGeneral";
    }
}
