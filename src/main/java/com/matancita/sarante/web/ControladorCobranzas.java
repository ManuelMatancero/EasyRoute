package com.matancita.sarante.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.matancita.sarante.domain.Cliente;
import com.matancita.sarante.domain.Cobrador;
import com.matancita.sarante.domain.Pagare;
import com.matancita.sarante.domain.Prestamo;
import com.matancita.sarante.domain.Ruta;
import com.matancita.sarante.domain.Usuario;
import com.matancita.sarante.servicio.CobradorService;
import com.matancita.sarante.servicio.IUsuarioService;
import com.matancita.sarante.servicio.RutaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ControladorCobranzas {

    @Autowired
    private RutaService rutaService;
    @Autowired
    private CobradorService cobradorService;
    @Autowired
    private IUsuarioService usuarioService;
 

    @GetMapping("/vercobranzas")
    public String verCobranzas(@AuthenticationPrincipal User user, Model model) {

        List<Ruta> rutas;
        int pagaresPendientes = 0;
        double porCobrar = 0;
        int cantidadRutas = 0;
        int cantidadPrestamos = 0;
        List<Cliente> clientes = new ArrayList<>();
        List<Prestamo> prestamos = new ArrayList<>();
        List<Pagare> pagares = new ArrayList<>();
        Usuario usuario = usuarioService.findByUsuario(user.getUsername());
        Cobrador cobrador;
        log.info("Ejecutando el controlador Spring MVC");
        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE").withLocale(new Locale("es", "ES"));
        LocalDateTime currentDate = LocalDateTime.now();
        String formattedDate = formatter.format(currentDate).toUpperCase();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            // information collected for user admnin
            rutas = rutaService.listAll();
            for (Ruta ruta : rutas) {
                if (ruta.getDia().equalsIgnoreCase(formattedDate)) {
                    cantidadRutas++;
                    clientes.addAll(ruta.getClientes());
                }
            }
            for (Cliente cliente : clientes) {
                prestamos.addAll(cliente.getPrestamos());
            }
            for (Prestamo prestamo : prestamos) {
                cantidadPrestamos++;
                pagares.addAll(prestamo.getPagares());
            }
            for (Pagare pagare : pagares) {
                if (pagare.getReciboGen() == null) {
                    if (pagare.getVencimiento().isBefore(LocalDateTime.now())
                            || pagare.getVencimiento().isEqual(LocalDateTime.now()) ||
                            pagare.getVencimiento().toLocalDate().equals(LocalDateTime.now().toLocalDate())) {
                        pagaresPendientes++;
                        porCobrar += pagare.getTotal();
                    }
                }
            }
        } else {
            cobrador = usuario.getCobrador();
            rutas = cobrador.getRutas();
            for (Ruta ruta : rutas) {
                if (ruta.getDia().equalsIgnoreCase(formattedDate)) {
                    cantidadRutas++;
                    clientes.addAll(ruta.getClientes());
                }
            }
            for (Cliente cliente : clientes) {
                prestamos.addAll(cliente.getPrestamos());
            }
            for (Prestamo prestamo : prestamos) {
                cantidadPrestamos++;
                pagares.addAll(prestamo.getPagares());
            }
            for (Pagare pagare : pagares) {
                if (pagare.getReciboGen() == null) {
                    if (pagare.getVencimiento().isBefore(LocalDateTime.now())
                            || pagare.getVencimiento().isEqual(LocalDateTime.now())) {
                        pagaresPendientes++;
                        porCobrar += pagare.getTotal();
                    }
                }
            }

        }

        model.addAttribute("pagaresPendientes", pagaresPendientes);
        model.addAttribute("porCobrar", porCobrar);
        model.addAttribute("cantidadRutas", cantidadRutas);
        model.addAttribute("cantidadPrestamos", cantidadPrestamos);
        model.addAttribute("currentDate", formattedDate);
        model.addAttribute("rutas", rutas);
        model.addAttribute("cobranzasPage", true);
        return "cobranzas";
    }

}
