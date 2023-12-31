package com.matancita.sarante.web;

import com.matancita.sarante.domain.Cobrador;
import com.matancita.sarante.domain.Ruta;
import com.matancita.sarante.domain.Zona;
import com.matancita.sarante.servicio.CobradorService;
import com.matancita.sarante.servicio.RutaService;
import com.matancita.sarante.servicio.ZonaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class ControladorRuta {

    @Autowired
    private RutaService rutaService;
    @Autowired
    private ZonaService zonaService;
    @Autowired
    private CobradorService cobradorService;

  

    @PostMapping("/guardarZona")
    public String guardarZona(@Valid Zona zona, Errors errores, RedirectAttributes redirectAttributes) {
        if (errores.hasErrors()) {
            return "verrutas";
        }
        zona.setEstatus(1);
        zonaService.insert(zona);
        redirectAttributes.addFlashAttribute("successMessage", "Zone saved successfully!");
        return "redirect:/verrutas";
    }

    @PostMapping("/guardarRuta")
    public String guardarRuta(@Valid Ruta ruta, @RequestParam Long idCobrador, @RequestParam Long idZona,
            Errors errores, RedirectAttributes redirectAttributes) {
        if (errores.hasErrors()) {
            return "modificarRuta";
        }
        log.info("nombre= " + ruta.getNombre() + "\ndia= " + ruta.getDia());

        Zona zona;
        zona = zonaService.getById(idZona);
        Cobrador cobrador;
        cobrador = cobradorService.getById(idCobrador);
        // This operation will run if the idRuta is null when updating
        if (!(ruta.getIdRuta() == null)) {
            Ruta rutaFromDb = rutaService.getById(ruta.getIdRuta());
            ruta.setClientes(rutaFromDb.getClientes());
        }
        ruta.setZona(zona);
        ruta.setCobrador(cobrador);
        rutaService.insert(ruta);
        redirectAttributes.addFlashAttribute("successMessage", "Route saved successfully!");
        return "redirect:/verrutas";
    }

    @GetMapping("/modificarruta/{idRuta}")
    public String modificarRuta(Ruta ruta, Model model) {
        List<Cobrador> cobradores = cobradorService.listAll();
        List<Zona> zonas = zonaService.listAll();
        ruta = rutaService.getById(ruta.getIdRuta());
        model.addAttribute("ruta", ruta);
        model.addAttribute("cobradores", cobradores);
        model.addAttribute("zonas", zonas);
        model.addAttribute("routesPage", true);
        return "modificarRuta";
    }

    @GetMapping("/eliminarRuta")
    public String eliminarRuta(Ruta ruta, RedirectAttributes redirectAttributes) {
        ruta = rutaService.getById(ruta.getIdRuta());
        rutaService.delete(ruta);
        redirectAttributes.addFlashAttribute("successMessage", "Route eliminated successfully!");
        return "redirect:/verrutas";
    }
}
