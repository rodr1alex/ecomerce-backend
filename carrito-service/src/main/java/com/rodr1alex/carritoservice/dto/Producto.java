package com.rodr1alex.carritoservice.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    private Long codigo_producto;
    private String nombre;
    private String marca;
    private Double precio;
    private int cantidad_disponible;
}
