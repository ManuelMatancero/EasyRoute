package com.matancita.sarante.web;

import com.matancita.sarante.domain.Cobrador;
import com.matancita.sarante.domain.Empresa;
import com.matancita.sarante.domain.Rol;
import com.matancita.sarante.domain.Usuario;
import com.matancita.sarante.servicio.CobradorService;
import com.matancita.sarante.servicio.CobradorServiceImpl;
import com.matancita.sarante.servicio.EmpresaService;
import com.matancita.sarante.servicio.RolService;
import com.matancita.sarante.servicio.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ControladorUsuarios {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private CobradorService cobradorService;

    @Autowired
    private RolService rolService;

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        List<Empresa> empresas = empresaService.listAll(); // Obtener lista de empresas desde el servicio
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("empresas", empresas); // Enviar lista de empresas a la vista
        model.addAttribute("usuario", new Usuario());
        return "usuarios";
    }

    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@ModelAttribute("usuario") @Valid Usuario usuario,
            @RequestParam("nombreCobrador") String nombreCobrador,
            @RequestParam("apellidoCobrador") String apellidoCobrador,
            @RequestParam("cedulaCobrador") String cedulaCobrador,
            @RequestParam("telefonoCobrador") String telefonoCobrador,
            @RequestParam("direccionCobrador") String direccionCobrador,
            @RequestParam("rol") int rol,
            @RequestParam("idEmpresa") Long idEmpresa,
            Errors errors,
            RedirectAttributes ra) {

        if (errors.hasErrors()) {
            // Si hay errores en la validación, manejarlos como desees (redireccionar o
            // mostrar mensajes)
            return "usuarios";
        }

        Cobrador cobrador = new Cobrador();
        cobrador.setNombre(nombreCobrador);
        cobrador.setApellido(apellidoCobrador);
        cobrador.setCedula(cedulaCobrador);
        cobrador.setTelefono(telefonoCobrador);
        cobrador.setDireccion(direccionCobrador);

        // Guardar el cobrador en la base de datos
        cobradorService.insert(cobrador);

        // Obtener el cobrador recién guardado
        cobrador = cobradorService.getById(cobrador.getIdCobrador());

        Empresa empresa = empresaService.getById(idEmpresa);

        // Configurar el rol del usuario
        if (rol == 1) {
            rolService.insert(new Rol("Admin", usuario));
            rolService.insert(new Rol("Cobrador", usuario));
        } else if (rol == 2) {
            rolService.insert(new Rol("Cobrador", usuario));
        }

        // Guardar el usuario en la base de datos
        usuario.setEmpresa(empresa);
        usuario.setCobrador(cobrador);
        usuarioService.guardarUsuario(usuario);

        ra.addFlashAttribute("mensaje", "Usuario creado exitosamente");
        return "redirect:/usuarios";
    }
}
