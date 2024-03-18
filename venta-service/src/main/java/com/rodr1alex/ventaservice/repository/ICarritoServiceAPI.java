package com.rodr1alex.ventaservice.repository;

import com.rodr1alex.ventaservice.dto.CarritoInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "carrito-service")
public interface ICarritoServiceAPI {
    @GetMapping("/carrito/{id_carrito}")
    public CarritoInfoDTO getCartInfoById(@PathVariable("id_carrito") Long id_carrito);

    @GetMapping("/venta/crear/{id_carrito}")
    public String createSale(@PathVariable("id_carrito") Long id_carrito);

    @PutMapping("/venta/actualizar")
    public String modifySale(@RequestBody CarritoInfoDTO actualizadoCarrito);

}
