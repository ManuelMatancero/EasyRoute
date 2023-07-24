package com.matancita.sarante.web;

import com.matancita.sarante.domain.*;
import com.matancita.sarante.servicio.*;

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
    public String inicio(@AuthenticationPrincipal User user){
        log.info("ejecutando el controlador Spring MVC");
        log.info("usuario que hizo login:" + user);
        return "index";
    }
    @GetMapping("/verrutas")
    public String rutas (Model model){
        List<Ruta> rutas = rutaService.listAll();
        List<Zona> zonas = zonaService.listAll();
        List<Cobrador> cobradores = cobradorService.listAll();
        //Numero total de rutas
        int totalRutas = rutas.size();
        //Dinero en la ruta
        double dineroPorCobrar = 0;
        for(Ruta ruta: rutas){
            List<Cliente> clientes = ruta.getClientes();
            for (Cliente cliente: clientes){
                List<Prestamo> prestamos = cliente.getPrestamos();
                for (Prestamo prestamo: prestamos){
                    List<Pagare> pagares = prestamo.getPagares();
                    for (Pagare pagare: pagares){
                        if(pagare.getReciboGen()==null){
                            dineroPorCobrar += pagare.getTotal();
                        }
                    }
                }
            }
        }
        model.addAttribute("dineroPorCobrar", dineroPorCobrar);
        model.addAttribute("totalRutas", totalRutas);
        model.addAttribute("rutas", rutas);
        model.addAttribute("zonas", zonas);
        model.addAttribute("cobradores", cobradores);
        //These are the objects to be filled in the form
        model.addAttribute("ruta", new Ruta());
        model.addAttribute("zona", new Zona());
        return "rutas";
    }
/*
    @GetMapping("/verclientes")
    public String clientes (Model model){
        List<Cliente> clientes = clienteService.listAll();
        model.addAttribute("clientes", clientes);
        model.addAttribute("cliente", new Cliente());
        return "clientes";
    }*/

    @GetMapping("/verpagares")
    public String pagares (Model model){
        List<Pagare> pagares = pagareService.listAll();
        model.addAttribute("pagares", pagares);
        return "pagares";
    }

    
   /* @GetMapping("/verprestamos/{idCliente}")
    public String prestamos (Cliente cliente, Model model){
        cliente = clienteService.getById(cliente.getIdCliente());
        List<Prestamo> prestamos = cliente.getPrestamos();
        model.addAttribute("prestamos", prestamos);
        return "prestamos";
    }*/
    
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
