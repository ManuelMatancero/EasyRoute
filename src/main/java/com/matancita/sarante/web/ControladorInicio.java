package com.matancita.sarante.web;

import com.matancita.sarante.domain.Cliente;
import com.matancita.sarante.domain.Prestamo;
import com.matancita.sarante.domain.Ruta;
import com.matancita.sarante.servicio.ClienteService;
import com.matancita.sarante.servicio.RutaService;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private ClienteService clienteService;
    
    @GetMapping("/")
    public String inicio(Model model, @AuthenticationPrincipal User user){
        var rutas = rutaService.listAll();
        log.info("ejecutando el controlador Spring MVC");
        log.info("usuario que hizo login:" + user);
        var saldoTotal = 0D;
        model.addAttribute("rutas", rutas);
        model.addAttribute("saldoTotal", saldoTotal);
        model.addAttribute("totalRutas", rutas.size());
        return "index";
    }
    
    @GetMapping("/verclientes/{idRuta}")
    public String clientes (Ruta ruta, Model model){
        ruta = rutaService.getById(ruta.getIdRuta());
        List<Cliente> clientes = ruta.getClientes();
        model.addAttribute("clientes", clientes);
        return "clientes";      
    }
    
    @GetMapping("/verprestamos/{idCliente}")
    public String prestamos (Cliente cliente, Model model){
        cliente = clienteService.getById(cliente.getIdCliente());
        List<Prestamo> prestamos = cliente.getPrestamos();
        model.addAttribute("prestamos", prestamos);
        return "prestamos";
    }
    
//    @GetMapping("/agregar")
//    public String agregar(Persona persona){
//        return "modificar";
//    }
//    
//    @PostMapping("/guardar")
//    public String guardar(@Valid Persona persona, Errors errores){
//        if(errores.hasErrors()){
//            return "modificar";
//        }
//        personaService.guardar(persona);
//        return "redirect:/";
//    }
//    
//    @GetMapping("/editar/{idPersona}")
//    public String editar(Persona persona, Model model){
//        persona = personaService.encontrarPersona(persona);
//        model.addAttribute("persona", persona);
//        return "modificar";
//    }
//    
//    @GetMapping("/eliminar")
//    public String eliminar(Persona persona){
//        personaService.eliminar(persona);
//        return "redirect:/";
//    }
}
