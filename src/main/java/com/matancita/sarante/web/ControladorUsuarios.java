package com.matancita.sarante.web;

import com.matancita.sarante.domain.Cobrador;
import com.matancita.sarante.util.EncriptarPassword;
import com.matancita.sarante.domain.Empresa;
import com.matancita.sarante.domain.Rol;
import com.matancita.sarante.domain.Usuario;
import com.matancita.sarante.servicio.CobradorService;
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
        List<Empresa> empresas = empresaService.listAll();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("empresas", empresas);
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
            return "usuarios";
        }

        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        for (Usuario user : usuarios) {
            if (user.getUsername().equals(usuario.getUsername())) {
                ra.addFlashAttribute(
                    "errorMessage",
                    "El nombre de usuario ya existe, por favor utilice otro."
                );
                return "redirect:/usuarios";
            }            
        }

        Cobrador cobrador = new Cobrador();
        cobrador.setNombre(nombreCobrador);
        cobrador.setApellido(apellidoCobrador);
        cobrador.setCedula(cedulaCobrador);
        cobrador.setTelefono(telefonoCobrador);
        cobrador.setDireccion(direccionCobrador);
        cobrador.setFechaIngreso(LocalDateTime.now());

        cobradorService.insert(cobrador);
        cobrador = cobradorService.getById(cobrador.getIdCobrador());

        Empresa empresa = empresaService.getById(idEmpresa);

        usuario.setEmpresa(empresa);
        usuario.setCobrador(cobrador);

        String userPassword = EncriptarPassword.encriptarPassword(usuario.getPassword());
        usuario.setPassword(userPassword);

        usuarioService.guardarUsuario(usuario);
        usuario = usuarioService.getUsuarioById(usuario.getIdUsuario());

        if (rol == 1) {
            rolService.insert(new Rol("ROLE_ADMIN", usuario));
            rolService.insert(new Rol("ROLE_USER", usuario));
        } else if (rol == 2) {
            rolService.insert(new Rol("ROLE_USER", usuario));
        }
        
        ra.addFlashAttribute(
            "successMessage",
            "Usuario creado exitosamente"
        );
        return "redirect:/usuarios";
    }

    @GetMapping("editarUsuario/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long idUsuario, Model model) {
        Usuario usuario = usuarioService.getUsuarioById(idUsuario);

        List<Rol> roles = rolService.listAll();
        String rolUsuario = null;
        for (Rol rol : roles) {
            if (rol != null && rol.getUsuario() != null) {
                if (rol.getUsuario().getIdUsuario() == idUsuario) {
                    if (rol.getIdRol() == 1){
                        rolUsuario = "Admin";
                    } else if (rol.getIdRol() == 2) {
                        if (rolUsuario != null){
                            rolUsuario = "Admin";
                        }
                        else {
                            rolUsuario = "Cobrador";
                        }
                    }
                }  
            }          
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("empresas", empresaService.listAll());
        model.addAttribute("rolUsuario", rolUsuario);
        return "layout/usuarios/editarUsuario";
    }

    @PostMapping("/editarUsuario")
    public String guardarEdicionUsuario(@ModelAttribute("usuario") @Valid Usuario usuario,
                                        @RequestParam("rol") int roUser,
                                        BindingResult result,
                                        RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "layout/usuarios/editarUsuario";
        }
        //Encriptamos el password antes de obtener el usuario
        String userPassword = EncriptarPassword.encriptarPassword(usuario.getPassword());
        //obtenemos el usuario para editarlo
        usuario = usuarioService.getUsuarioById(usuario.getIdUsuario());
        if (roUser == 1) {
            // delete all rows in Rol database with the IdUsuario
            rolService.deleteAllByUsuario(usuario);
            rolService.insert(new Rol("ROLE_ADMIN", usuario));
            rolService.insert(new Rol("ROLE_USER", usuario));
        } else if (roUser == 2) {
            rolService.deleteAllByUsuario(usuario);
            rolService.insert(new Rol("ROLE_USER", usuario));
        }

        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        for (Usuario user : usuarios) {
            if (user.getIdUsuario() != usuario.getIdUsuario() && user.getUsername().equals(usuario.getUsername())) {
                ra.addFlashAttribute(
                    "errorMessage",
                    "El nombre de usuario ya existe, por favor utilice otro."
                );
                // return to the path /editarUsuario/{id}
                return "redirect:/editarUsuario/" + usuario.getIdUsuario(); 
            }            
        }
        usuario.setPassword(userPassword);
        usuarioService.guardarUsuario(usuario);

        ra.addFlashAttribute(
            "successMessage",
            "Usuario editado exitosamente"
        );
        return "redirect:/usuarios";
    }
}