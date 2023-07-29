package com.matancita.sarante.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.matancita.sarante.servicio.PagareService;

@Controller
public class ControladorCuadres {
    @Autowired
    private PagareService pagareService;

    @GetMapping("/cuadres")
    public String cuadres(Model model) {
        return "cuadres";
    }
}
