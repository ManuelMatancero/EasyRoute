package com.matancita.sarante.servicio;


import com.matancita.sarante.domain.Usuario;
import java.util.List;

public interface IUsuarioService {

    public List<Usuario> listAll();
    public Usuario getById(Long id);
    public void insert(Usuario usuario);
    public void update(Usuario usuario);
    public void delete(Usuario usuario);

    public Usuario findByUsuario(String usuario);
}
