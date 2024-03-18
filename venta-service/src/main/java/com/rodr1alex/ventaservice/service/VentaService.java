package com.rodr1alex.ventaservice.service;

import com.rodr1alex.ventaservice.dto.CarritoInfoDTO;
import com.rodr1alex.ventaservice.dto.VentaInfoDTO;
import com.rodr1alex.ventaservice.model.Venta;
import com.rodr1alex.ventaservice.repository.ICarritoServiceAPI;
import com.rodr1alex.ventaservice.repository.IVentaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class VentaService implements IVentaService{
    @Autowired
    private IVentaRepository ventaRepo;
    @Autowired
    private ICarritoServiceAPI carritoServiceAPI;

    @Override
    @CircuitBreaker(name = "carrito-service", fallbackMethod = "fallbackCreateSale")
    @Retry(name = "carrito-service")
    //listo
    public String createSale(Venta nuevaVenta) {
        String mensaje = carritoServiceAPI.createSale(nuevaVenta.getId_carrito());
        ventaRepo.save(nuevaVenta);
        return mensaje;
    }
    @Override
    @CircuitBreaker(name = "carrito-service", fallbackMethod = "fallbackFindSale")
    @Retry(name = "carrito-service")
    //listo
    public VentaInfoDTO findSale(Long id_venta) {
        Venta venta = ventaRepo.findById(id_venta).orElse(null);
        CarritoInfoDTO carritoInfoDTO = carritoServiceAPI.getCartInfoById(venta.getId_carrito());
        VentaInfoDTO ventaInfoDTO = new VentaInfoDTO();
        ventaInfoDTO.setId_venta(venta.getId_venta());
        ventaInfoDTO.setFecha(venta.getFecha());
        ventaInfoDTO.setCarrito(carritoInfoDTO);
        return ventaInfoDTO;
    }
    @Override
    public List<VentaInfoDTO> findSales() {
        List<Venta> ventaList = ventaRepo.findAll();
        List<VentaInfoDTO> ventaInfoDTOList = new ArrayList<>();
        for(Venta venta: ventaList){
            VentaInfoDTO ventaInfoDTO = this.findSale(venta.getId_venta());
            ventaInfoDTOList.add(ventaInfoDTO);
        }
        return ventaInfoDTOList;
    }
    @Override
    @CircuitBreaker(name = "carrito-service", fallbackMethod = "fallbackUpdateSale")
    @Retry(name = "carrito-service")
    //listo
    public String updateSale(VentaInfoDTO actualizadaVenta) {
        return carritoServiceAPI.modifySale(actualizadaVenta.getCarrito());
    }
    @Override
    public void deleteSale(Long id_venta) {
        ventaRepo.deleteById(id_venta);
    }

    //Metodos especiales
    public String fallbackCreateSale(Throwable throwable) {
        return "Carrito fuera de servicio, no se puede crear la venta";
    }
    public VentaInfoDTO fallbackFindSale(Throwable throwable) {
        LocalDate date = LocalDate.parse("9999-01-01");
        CarritoInfoDTO carritoInfoDTO = new CarritoInfoDTO(99999L,9999999D,new ArrayList<>());
        VentaInfoDTO ventaInfoDTO = new VentaInfoDTO(9999999L,date , carritoInfoDTO);
        return ventaInfoDTO;
    }
    public String fallbackUpdateSale(Throwable throwable) {
        return "Carrito fuera de servicio, no se puede actualizar la venta";
    }

}
