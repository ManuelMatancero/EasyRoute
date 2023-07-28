package com.matancita.sarante.web;


import com.matancita.sarante.domain.*;
import com.matancita.sarante.servicio.ClienteService;
import com.matancita.sarante.servicio.PrestamoService;
import com.matancita.sarante.servicio.RutaService;
import com.matancita.sarante.specialfunctions.ContratoPDFGenerator;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
public class ControladorClientes {

    @Autowired
    private RutaService rutaService;
    @Autowired
    private  ClienteService clienteService;
    @Autowired
    private PrestamoService prestamoService;

    @GetMapping("/verclientes/{idRuta}")
    public String clientes(Ruta ruta, Model model){
        ruta = rutaService.getById(ruta.getIdRuta());
        List<Cliente> clientes = ruta.getClientes();
        model.addAttribute("clientes", clientes);
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("idRuta", ruta.getIdRuta());
        return "clientes";
    }

    @GetMapping("/verprestamos/{idCliente}")
    public String prestamos (Cliente cliente, Model model){
        cliente = clienteService.getById(cliente.getIdCliente());
        List<Prestamo> prestamos = cliente.getPrestamos();
        //Here I know how much money that customer is pending
        double totalPendiente=0;
        int totalPrestamos=0;
        int pagarespendientes=0;
        double totalPagado=0;
        for(Prestamo prestamo: prestamos){
            totalPrestamos++;
            List<Pagare> pagares = prestamo.getPagares();
            for (Pagare pagare : pagares){
                if (pagare.getReciboGen()==null){
                    totalPendiente += pagare.getTotal();
                    pagarespendientes++;
                }
                if(!(pagare.getReciboGen()==null)){
                    totalPagado +=pagare.getTotal();
                }
            }
        }
        model.addAttribute("totalPrestamos", totalPrestamos);
        model.addAttribute("totalPendiente", totalPendiente);
        model.addAttribute("pagaresPendientes", pagarespendientes);
        model.addAttribute("totalPagado", totalPagado);
        model.addAttribute("cliente", cliente);
        model.addAttribute("prestamos", prestamos);
        model.addAttribute("prestamo", new Prestamo());
        return "perfilCliente";
    }

    @PostMapping("/guardarCliente")
    public String guardarCliente(@Valid Cliente cliente, @RequestParam Long idRuta, @RequestParam String date,Errors errores, RedirectAttributes redirectAttributes){
        if(errores.hasErrors()){
            return "verclientes/"+idRuta;
        }
        cliente.setEstatus(1);
        cliente.setFechaIngreso(LocalDateTime.now());
        Ruta ruta = new Ruta();
        ruta.setIdRuta(idRuta);
        cliente.setRuta(ruta);
        if(date.equalsIgnoreCase("")){
            cliente.setFechaNacimiento(null);
        }else{
            // Create a DateTimeFormatter with the pattern corresponding to the input date format
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // Parse the string into a LocalDate object using the formatter
            LocalDate localDate = LocalDate.parse(date, dateFormatter);
            // Create a LocalDateTime object with the time set to midnight (00:00:00)
            LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
            cliente.setFechaNacimiento(localDateTime);

        }
        //here i get the prestamos of that client if the id is different to null only when updating
        if(!(cliente.getIdCliente()==null)){
            Cliente clienteFromDb = clienteService.getById(cliente.getIdCliente());
            cliente.setPrestamos(clienteFromDb.getPrestamos());
        }
        //Here i insert the cliente
        clienteService.insert(cliente);
        redirectAttributes.addFlashAttribute("successMessage", "Customer saved successfully!");
        return "redirect:/verclientes/"+idRuta;
    }

    @GetMapping("/modificarcliente/{idCliente}")
    public String modificarCliente(Cliente cliente, Model model){
        cliente = clienteService.getById(cliente.getIdCliente());
        model.addAttribute("cliente", cliente);
        return "modificarCliente";
    }

    @GetMapping("paginaimprimir/{idPrestamo}")
    public String pageImprimir(Prestamo prestamo, Model model){
        prestamo = prestamoService.getById(prestamo.getIdPrestamo());
        model.addAttribute("prestamo", prestamo);
        return "imprimir";
    }

    @GetMapping("imprimircontrato/{idPrestamo}")
    public void imprimirContrato(Prestamo prestamo, HttpServletResponse response) throws DocumentException, IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-disposition";
        String headervalue = "inline; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        prestamo = prestamoService.getById(prestamo.getIdPrestamo());
        ContratoPDFGenerator printContrato = new ContratoPDFGenerator();
        printContrato.setPrestamo(prestamo);
        printContrato.generate(response);
    }
    @GetMapping("/eliminarCliente")
    public String eliminarCliente(Cliente cliente, RedirectAttributes redirectAttributes){
        cliente = clienteService.getById(cliente.getIdCliente());
        Long id = cliente.getRuta().getIdRuta();
        clienteService.delete(cliente);
        redirectAttributes.addFlashAttribute("successMessage", "Customer eliminated successfully!");
        return "redirect:/verclientes/"+id;
    }
}
