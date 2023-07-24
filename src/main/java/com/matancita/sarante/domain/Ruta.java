package com.matancita.sarante.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name= "ruta")
@Data
//Con esta anotacion evito la recursion infinita al listar con el metodo get
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idRuta")
public class Ruta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_ruta")
    private Long idRuta;

    private String nombre;


    private String dia;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_zona", referencedColumnName = "id_zona")
    private Zona zona;

    @OneToMany(mappedBy ="ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cliente> clientes;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_cobrador", referencedColumnName = "id_cobrador")
    private Cobrador cobrador;


}
