package com.rodr1alex.carritoservice.service;

import com.rodr1alex.carritoservice.dto.Producto;
import com.rodr1alex.carritoservice.model.ProductoPedido;

import java.util.List;

public interface IProductoPedidoService {
    public void createOrderedProduct(ProductoPedido nuevoProductoPedido);
    public ProductoPedido findOrderedProduct(Long id_producto_pedido);
    public List<ProductoPedido> findOrderedProducts();
    public void updateOrderedProduct(ProductoPedido actualizadoProductoPedido);
    public void deleteOrderedProduct(Long id_producto_pedido);


}
