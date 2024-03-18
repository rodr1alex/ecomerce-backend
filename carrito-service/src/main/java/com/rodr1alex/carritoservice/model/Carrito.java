package com.rodr1alex.carritoservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Carrito {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id_carrito;
    private Double total_carrito;
    @OneToMany
    private List<ProductoPedido> listado_productos_pedidos;
}
