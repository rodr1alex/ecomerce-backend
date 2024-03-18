package com.rodr1alex.ventaservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id_venta;
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    private Long id_carrito;
}
