package com.rodr1alex.productoservice.service;

import com.rodr1alex.productoservice.dto.ProductoPedido;
import com.rodr1alex.productoservice.dto.RespuestaPedido;
import com.rodr1alex.productoservice.model.Producto;

import java.util.List;

public interface IProductoService {
    public void createProduct(Producto nuevoProducto);
    public Producto findProduct(Long codigo_producto);
    public List<Producto> findProducts();
    public void updateProduct(Producto actualizadoProducto);
    public void deleteProduct(Long codigo_producto);

    //METODOS ESPECIALES
    public RespuestaPedido createSale(List<ProductoPedido> productoPedidoList);
    RespuestaPedido stockOk(List<ProductoPedido> productoPedidoList);
}
