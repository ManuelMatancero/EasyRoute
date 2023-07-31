package com.matancita.sarante.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ControladorCobranzas {

    @GetMapping("/vercobranzas")
    public String verCobranzas(Model model) {
        log.info("Ejecutando el controlador Spring MVC");
         // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd");
        LocalDateTime currentDate = LocalDateTime.now();
        String formattedDate = formatter.format(currentDate);
        model.addAttribute("currentDate", formattedDate);
        return "cobranzas";
    }

    
}
