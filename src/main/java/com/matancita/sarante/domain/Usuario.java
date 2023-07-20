package com.matancita.sarante.domain;

import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Entity
@Data
@Table(name="usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuario")
    private Long idUsuario;

    private String username;

    private String password;

    private int estatus;

    @OneToMany
    @JoinColumn(name="id_usuario")
    private List<Rol> roles;

    @ManyToOne
    @JoinColumn(name="id_empresa", referencedColumnName = "id_empresa")
    private Empresa empresa;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cobrador", referencedColumnName = "id_cobrador")
    private Cobrador cobrador;


}
