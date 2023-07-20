package com.matancita.sarante.domain;

import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "zona")
@Data
public class Zona implements Serializable {

    private static final long serialversionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_zona")
    private Long idZona;

    private String nombre;

    private String direccion;

    private int estatus;


}
