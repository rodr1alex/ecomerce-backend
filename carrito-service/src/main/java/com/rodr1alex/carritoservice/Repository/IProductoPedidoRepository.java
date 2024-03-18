package com.rodr1alex.carritoservice.Repository;

import com.rodr1alex.carritoservice.model.ProductoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductoPedidoRepository extends JpaRepository<ProductoPedido, Long> {
}
