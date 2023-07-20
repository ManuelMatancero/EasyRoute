package com.matancita.sarante.domain;

import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "pagare")
@Data
public class Pagare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_pagare")
    private Long idPagare;

    @Column(name = "no_pagare")
    private int noPagare;

    private double capital;

    private double interes;

    private double total;

    private LocalDateTime vencimiento;

    @ManyToOne
    @JoinColumn(name = "id_prestamo", referencedColumnName = "id_prestamo")
    private Prestamo prestamo;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_recibo_gen", referencedColumnName = "id_recibos_gen")
    private RecibosGen reciboGen;

}
