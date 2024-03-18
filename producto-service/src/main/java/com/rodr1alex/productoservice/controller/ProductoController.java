package com.rodr1alex.productoservice.controller;

import com.rodr1alex.productoservice.dto.ProductoPedido;
import com.rodr1alex.productoservice.dto.RespuestaPedido;
import com.rodr1alex.productoservice.model.Producto;
import com.rodr1alex.productoservice.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductoController {
    @Autowired
    private IProductoService productoServ;
    @Value("${server.port}")
    private int serverPort;

    @PostMapping("/producto/crear")
    public String createProduct(@RequestBody Producto nuevoProducto){
        System.out.println("Estoy en el puerto: " + serverPort);
        productoServ.createProduct(nuevoProducto);
        return "Producto creado con exito";
    }
    @GetMapping("/producto/{codigo_producto}")
    public Producto findProduct(@PathVariable Long codigo_producto){
        System.out.println("Estoy en el puerto: " + serverPort);
        return productoServ.findProduct(codigo_producto);
    }
    @GetMapping("/producto")
    public List<Producto> findProducts(){
        System.out.println("Estoy en el puerto: " + serverPort);
        return productoServ.findProducts();
    }
    @PutMapping("/producto/actualizar")
    public String updateProduct(@RequestBody Producto actualizadoProducto){
        System.out.println("Estoy en el puerto: " + serverPort);
        productoServ.updateProduct(actualizadoProducto);
        return "Producto actualizado con exito";
    }
    @DeleteMapping("/producto/borrar/{codigo_producto}")
    public String deleteProduct(Long codigo_producto){
        System.out.println("Estoy en el puerto: " + serverPort);
        productoServ.deleteProduct(codigo_producto);
        return "Producto eliminado con exito";
    }

    // -----------------ENDPOINTS PARA EL SERVICIO CARRITO ----------------------------
    @PostMapping("/venta/crear")
    public RespuestaPedido createSale(@RequestBody List<ProductoPedido> productoPedidoList){
        System.out.println("Estoy en el puerto: " + serverPort);
        return productoServ.createSale(productoPedidoList);
    }
    @PostMapping("/venta/consulta_stock")
    public RespuestaPedido stockOk(@RequestBody List<ProductoPedido> productoPedidoList){
        System.out.println("Estoy en el puerto: " + serverPort);
        return productoServ.stockOk(productoPedidoList);
    }

}
