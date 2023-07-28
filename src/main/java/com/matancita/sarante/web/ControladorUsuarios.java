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
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
        cobrador.setFechaIngreso(LocalDateTime.now());

        // Guardar el cobrador en la base de datos
        cobradorService.insert(cobrador);

        // Obtener el cobrador recién guardado
        cobrador = cobradorService.getById(cobrador.getIdCobrador());

        Empresa empresa = empresaService.getById(idEmpresa);

        
        // Guardar el usuario en la base de datos
        usuario.setEmpresa(empresa);
        usuario.setCobrador(cobrador);
        usuarioService.guardarUsuario(usuario);
        usuario = usuarioService.getUsuarioById(usuario.getIdUsuario());

        // Configurar el rol del usuario
        if (rol == 1) {
            rolService.insert(new Rol("Admin", usuario));
            rolService.insert(new Rol("Cobrador", usuario));
        } else if (rol == 2) {
            rolService.insert(new Rol("Cobrador", usuario));
        }
        
        ra.addFlashAttribute("mensaje", "Usuario creado exitosamente");
        return "redirect:/usuarios";
    }

    @GetMapping("editarUsuario/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long idUsuario, Model model) {
        Usuario usuario = usuarioService.getUsuarioById(idUsuario);
        List<Rol> roles = rolService.listAll();
        String rolUsuario = "";
        for (Rol rol : roles) {
            if (rol.getUsuario().getIdUsuario() == usuario.getIdUsuario()) {
                if (rol.getIdRol() == 1){
                    rolUsuario = "Admin";
                } else if (rol.getIdRol() == 2) {
                    rolUsuario = "Cobrador";
                }
            }            
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("empresas", empresaService.listAll());
        model.addAttribute(rolUsuario, rolUsuario);
        return "layout/usuarios/editarUsuario";
    }

    @PostMapping("/editarUsuario")
    public String guardarEdicionUsuario(@ModelAttribute("usuario") @Valid Usuario usuario,
                                        BindingResult result,
                                        RedirectAttributes ra) {
        if (result.hasErrors()) {
            // Si hay errores en la validación, manejarlos como desees (redireccionar o mostrar mensajes)
            return "layout/usuarios/editarUsuario";
        }

        // Actualizar los datos del usuario en la base de datos
        usuarioService.guardarUsuario(usuario);

        ra.addFlashAttribute("mensaje", "Usuario editado exitosamente");
        return "redirect:/usuarios";
    }
}
