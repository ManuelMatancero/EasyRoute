package com.matancita.sarante.servicio;

import com.matancita.sarante.dao.UsuarioDao;
import com.matancita.sarante.domain.Pagare;
import com.matancita.sarante.domain.Rol;
import com.matancita.sarante.domain.Usuario;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Slf4j
public class UsuarioService implements UserDetailsService{

    @Autowired
    private UsuarioDao usuarioDao;
    
    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);
        
        if(usuario == null){
            throw new UsernameNotFoundException(username);
        }
        
        var roles = new ArrayList<GrantedAuthority>();
        
        for(Rol rol: usuario.getRoles()){
            roles.add(new SimpleGrantedAuthority(rol.getNombre()));
        }
        
        return new User(usuario.getUsername(), usuario.getPassword(), roles);
    }

    @Transactional
    public void guardarUsuario(Usuario usuario) {
        usuarioDao.save(usuario);
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> getAllUsuarios() {
        return usuarioDao.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioById(Long idUsuario) {
        return usuarioDao.findById(idUsuario).orElse(null);
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioByCobradorId(Long cobradorId) {
        List<Usuario> usuarios = usuarioDao.findAll();
        Usuario selectedUser = null;
        
        for (Usuario usuario : usuarios) {
            if(usuario.getCobrador().getIdCobrador() == cobradorId){
                selectedUser = usuario;
            }
        }

        return selectedUser;
    }
    
}
