package com.rodr1alex.carritoservice.Repository;

import com.rodr1alex.carritoservice.dto.Producto;
import com.rodr1alex.carritoservice.dto.RespuestaPedido;
import com.rodr1alex.carritoservice.model.ProductoPedido;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "producto-service")
public interface IProductoServiceAPI {
    @GetMapping("/producto/{codigo_producto}")
    public Producto findProductByCode(@PathVariable("codigo_producto") Long codigo_producto);

    @PostMapping("/venta/crear")
    public RespuestaPedido createSale(@RequestBody List<ProductoPedido> productoPedidoList);

    @PostMapping("/venta/consulta_stock")
    public RespuestaPedido stockOk(@RequestBody List<ProductoPedido> productoPedidoList);
}
