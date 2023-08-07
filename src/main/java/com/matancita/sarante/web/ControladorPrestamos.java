/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.matancita.sarante.web;

import com.matancita.sarante.domain.Cliente;
import com.matancita.sarante.domain.Pagare;
import com.matancita.sarante.domain.Prestamo;
import com.matancita.sarante.servicio.PagareService;
import com.matancita.sarante.servicio.PrestamoService;
import com.matancita.sarante.specialfunctions.WeeklyDatesCalculator;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author manue
 */
@Controller
@Slf4j
public class ControladorPrestamos {

    @Autowired
    private PrestamoService prestamoService;
    
    @Autowired
    private PagareService pagareService;

   

    @PostMapping("/guardarPrestamo")
    public String guardarZona(@Valid Prestamo prestamo, @RequestParam Long idCliente, Errors errores, RedirectAttributes redirectAttributes) {
        if (errores.hasErrors()) {
            return "verrutas";
        }
        //Agregamos el cliente correspondiente
        Cliente cliente = new Cliente();
        cliente.setIdCliente(idCliente);
        prestamo.setCliente(cliente);
        int cuotas = prestamo.getCuotas();
        double monto = prestamo.getMonto();
        //Calculamos el interes que sera de un 30% por el momento
        double interes = prestamo.getMonto() * 0.30;
        prestamo.setInteres(interes);
        //Calculamos la fecha de vencimiento
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime dateInWeeks = currentTime.plusWeeks(cuotas);
        prestamo.setFecha(currentTime);
        //Setting the prestamo type
        if (prestamo.getCuotas() == 10) {
            prestamo.setTipoPrestamo("SEMANAL_10");
        } else {
            prestamo.setTipoPrestamo("SEMANAL_13");
        }
        //Vencimiento
        prestamo.setVencimiento(dateInWeeks);
        prestamo.setEstado(1);
        //insertamos el prestamo
        prestamoService.insert(prestamo);
        //Generamos la lista de pagares asociados a ese prestamo
        //Here i creates the pagares asociated with that prestamo
        List<Pagare> pagares = new WeeklyDatesCalculator().weeklyIterator(
                prestamo.getFecha(),
                prestamo.getIdPrestamo(),
                prestamo.getCuotas(),
                prestamo.getMonto(),
                interes);
        //Insertamos la lista de pagares
        pagareService.insertAll(pagares);
        redirectAttributes.addFlashAttribute("successMessage", "Prestamo saved successfully!");
        return "redirect:/verprestamos/" + idCliente;
    }

    @GetMapping("/archivarprestamo/{idPrestamo}")
    public String archivarprestamo(Prestamo prestamo, RedirectAttributes redirectAttributes){
        prestamo = prestamoService.getById(prestamo.getIdPrestamo());
        Long idCliente = prestamo.getCliente().getIdCliente();
        prestamo.setEstado(2);
        prestamoService.insert(prestamo);
        redirectAttributes.addFlashAttribute("successMessage", "Prestamo archivado correctamente!");
        return "redirect:/verprestamos/" + idCliente;

    }

}
