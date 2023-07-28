package com.matancita.sarante.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Table(name = "rol")
public class Rol implements Serializable {
    public Rol() {
    }

    public Rol(String nombre, Usuario usuario) {
        this.nombre = nombre;
        this.usuario = usuario;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @NotEmpty
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
