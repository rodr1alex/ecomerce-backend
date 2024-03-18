package com.rodr1alex.ventaservice.controller;

import com.rodr1alex.ventaservice.dto.VentaInfoDTO;
import com.rodr1alex.ventaservice.model.Venta;
import com.rodr1alex.ventaservice.service.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VentaController {
    @Autowired
    private IVentaService ventaService;

    @PostMapping("venta/crear")
    public String createSale(@RequestBody Venta nuevaVenta){
        return ventaService.createSale(nuevaVenta);
    }

    @GetMapping("venta/{id_venta}")
    public VentaInfoDTO findSale(@PathVariable Long id_venta){
        return ventaService.findSale(id_venta);
    }

    @GetMapping("/venta")
    public List<VentaInfoDTO> findSales(){
        return  ventaService.findSales();
    }

    @PutMapping("/venta/modificar")
    public String modifySale(@RequestBody VentaInfoDTO modificadaVenta){
        return ventaService.updateSale(modificadaVenta);
    }
}
