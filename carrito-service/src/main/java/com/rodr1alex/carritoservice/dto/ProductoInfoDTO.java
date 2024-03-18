package com.rodr1alex.carritoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoInfoDTO {
    private Long codigo_producto;
    private String nombre;
    private String marca;
    private Double precio;
    private int cantidad_pedida;
    private Double total;
}
