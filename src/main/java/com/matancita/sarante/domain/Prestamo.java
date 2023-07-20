package com.matancita.sarante.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prestamo")
@Data
//Con esta anotacion evito la recursion infinita al listar con el metodo get
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idPrestamo")
public class Prestamo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_prestamo")
    private Long idPrestamo;

    private LocalDateTime fecha;

    private double monto;

    @Column(name = "tipo_prestamo")
    private String tipoPrestamo;

    private LocalDateTime vencimiento;

    private double interes;

    private int cuotas;

    private int estado;

    @OneToMany
    @JoinColumn(name = "id_prestamo", referencedColumnName = "id_prestamo")
    private List<Pagare> pagares;

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;


}
