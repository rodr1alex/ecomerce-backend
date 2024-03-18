package com.rodr1alex.carritoservice.service;

import com.rodr1alex.carritoservice.dto.CarritoInfoDTO;
import com.rodr1alex.carritoservice.model.Carrito;
import com.rodr1alex.carritoservice.model.ProductoPedido;

import java.util.List;

public interface ICarritoService {
    public String createCart(List<ProductoPedido> productoPedidoList);
    public CarritoInfoDTO findCart(Long id_carrito);
    public List<CarritoInfoDTO> findCarts();
    public String updateCart(CarritoInfoDTO actualizadoCarrito);
    public void deleteCart(Long id_carrito);

    //METODOS ESPECIALES
    public String createSale(Long id_carrito);
    public String modifySale(CarritoInfoDTO actualizadoCarrito);
}
