package com.matancita.sarante.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name="cobrador")
//Con esta anotacion evito la recursion infinita al listar con el metodo get
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idCobrador")
public class Cobrador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_cobrador")
    private Long idCobrador;

    private String nombre;

    private String  apellido;

    private String cedula;

    private String direccion;

    private String  telefono;

    @Column(name="fecha_ingreso")
    private LocalDateTime fechaIngreso;

    @OneToMany
    @JoinColumn(name = "id_cobrador")
    private List<Ruta> rutas;
}
