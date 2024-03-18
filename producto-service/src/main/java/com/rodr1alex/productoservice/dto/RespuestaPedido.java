package com.rodr1alex.productoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaPedido {
    private int codigo_resultado; // 0:"Pedido OK", 1:"Error de codigo", 2:"Stock insuficiente" 3:"Error de codigo y de stock"
    private List<Long> codigo_producto_incorrecto;
    private List<Long> codigo_producto_stockinsuficiente;
    private Double total;
}
