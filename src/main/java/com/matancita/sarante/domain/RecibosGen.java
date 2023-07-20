package com.matancita.sarante.domain;

import javax.persistence.*;
import lombok.Data;


import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "recibos_gen")
@Data
public class RecibosGen implements Serializable {

    private static final long serialVersionUID =1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recibos_gen")
    private Long idRecibosGen;

    private LocalDateTime fecha;

    private double valor;
}
