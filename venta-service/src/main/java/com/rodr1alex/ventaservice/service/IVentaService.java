package com.rodr1alex.ventaservice.service;

import com.rodr1alex.ventaservice.dto.VentaInfoDTO;
import com.rodr1alex.ventaservice.model.Venta;

import java.util.List;

public interface IVentaService {
    public String createSale(Venta nuevaVenta);
    public VentaInfoDTO findSale(Long id_venta);
    public List<VentaInfoDTO> findSales();
    public String updateSale(VentaInfoDTO actualizadaVenta);
    public void deleteSale(Long id_venta);
}
