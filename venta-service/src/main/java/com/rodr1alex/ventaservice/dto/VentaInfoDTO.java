package com.rodr1alex.ventaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaInfoDTO {
    private Long id_venta;
    private LocalDate fecha;
    private CarritoInfoDTO carrito;
}
