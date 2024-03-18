package com.rodr1alex.ventaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarritoInfoDTO {
    private Long id_carrito;
    private Double total_carrito;
    private List<ProductoInfoDTO> listado_productos_pedidos;
}
