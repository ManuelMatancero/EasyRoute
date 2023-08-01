package com.matancita.sarante.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.matancita.sarante.domain.Empresa;
import com.matancita.sarante.servicio.EmpresaService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ControladorEmpresa {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping("/verempresa")
    public String verEmpresa(Model model) {
        log.info("Ejecutando el controlador Spring MVC");
        Empresa empresa = empresaService.listAll().get(0);

        model.addAttribute("empresa", empresa);
        return "empresa";
    }

    @PostMapping("/editarEmpresa")
    public String editarEmpresa(Empresa empresa, Model model, RedirectAttributes redirectAttributes) {
        log.info("Ejecutando el controlador Spring MVC");
        empresaService.insert(empresa);
        redirectAttributes.addFlashAttribute("successMessage", "Empresa edited successfully!");
        return "redirect:/verempresa";
    }
    
}
