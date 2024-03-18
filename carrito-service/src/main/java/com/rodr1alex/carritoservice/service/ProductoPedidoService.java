package com.rodr1alex.carritoservice.service;

import com.rodr1alex.carritoservice.Repository.ICarritoRepository;
import com.rodr1alex.carritoservice.Repository.IProductoPedidoRepository;
import com.rodr1alex.carritoservice.Repository.IProductoServiceAPI;
import com.rodr1alex.carritoservice.dto.Producto;
import com.rodr1alex.carritoservice.dto.RespuestaPedido;
import com.rodr1alex.carritoservice.model.Carrito;
import com.rodr1alex.carritoservice.model.ProductoPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoPedidoService implements IProductoPedidoService{
    @Autowired
    private IProductoPedidoRepository productoPedidoRepo;
    @Autowired
    private IProductoServiceAPI productoServiceAPI;
    @Autowired
    private ICarritoRepository carritoRepo;

    @Override
    public void createOrderedProduct(ProductoPedido nuevoProductoPedido) {
        productoPedidoRepo.save(nuevoProductoPedido);
    }
    @Override
    public ProductoPedido findOrderedProduct(Long id_producto_pedido) {
        return productoPedidoRepo.findById(id_producto_pedido).orElse(null);
    }
    @Override
    public List<ProductoPedido> findOrderedProducts() {
        return productoPedidoRepo.findAll();
    }
    @Override
    public void updateOrderedProduct(ProductoPedido actualizadoProductoPedido) {
        productoPedidoRepo.save(actualizadoProductoPedido);
    }
    @Override
    public void deleteOrderedProduct(Long id_producto_pedido) {
        productoPedidoRepo.deleteById(id_producto_pedido);
    }
}