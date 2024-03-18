package com.rodr1alex.carritoservice.controller;

import com.rodr1alex.carritoservice.dto.CarritoInfoDTO;
import com.rodr1alex.carritoservice.dto.Producto;
import com.rodr1alex.carritoservice.model.ProductoPedido;
import com.rodr1alex.carritoservice.service.ICarritoService;
import com.rodr1alex.carritoservice.service.IProductoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    private ICarritoService carritoServ;
    @Autowired
    private IProductoPedidoService productoPedidoServ;

    @PostMapping("/carrito/crear")
    public String createSale(@RequestBody List<ProductoPedido> productoPedidoList){
        return carritoServ.createCart(productoPedidoList);
    }
    @GetMapping("/carrito/{id_carrito}")
    public CarritoInfoDTO findCart(@PathVariable Long id_carrito){
        return carritoServ.findCart(id_carrito);
    }
    @GetMapping("/carrito")
    public List<CarritoInfoDTO> findCarts(){
        return carritoServ.findCarts();
    }
    @PutMapping("/carrito/actualizar")
    public String updateCart(@RequestBody CarritoInfoDTO actualizadoCarrito){
        return carritoServ.updateCart(actualizadoCarrito);
    }

    //-----------------ENDPOINTS PARA EL SERVICIO VENTA ----------------------------
    @GetMapping("/venta/crear/{id_carrito}")
    public String createSale(@PathVariable Long id_carrito){
        return carritoServ.createSale(id_carrito);
    }
    @PutMapping("/venta/actualizar")
    public String modifySale(@RequestBody CarritoInfoDTO actualizadoCarrito){
        return carritoServ.modifySale(actualizadoCarrito);
    }
}
