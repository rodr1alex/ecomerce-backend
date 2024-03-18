package com.rodr1alex.carritoservice.service;

import com.rodr1alex.carritoservice.Repository.ICarritoRepository;
import com.rodr1alex.carritoservice.Repository.IProductoPedidoRepository;
import com.rodr1alex.carritoservice.Repository.IProductoServiceAPI;
import com.rodr1alex.carritoservice.dto.CarritoInfoDTO;
import com.rodr1alex.carritoservice.dto.Producto;
import com.rodr1alex.carritoservice.dto.ProductoInfoDTO;
import com.rodr1alex.carritoservice.dto.RespuestaPedido;
import com.rodr1alex.carritoservice.model.Carrito;
import com.rodr1alex.carritoservice.model.ProductoPedido;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CarritoService implements ICarritoService{
    @Autowired
    private ICarritoRepository carritoRepo;
    @Autowired
    private IProductoServiceAPI productoServiceAPI;
    @Autowired
    private IProductoPedidoRepository productoPedidoRepo;

    @Override
    @CircuitBreaker(name = "producto-service", fallbackMethod = "fallbackCreateCart")
    @Retry(name = "producto-service")
    //listo
    public String createCart(List<ProductoPedido> productoPedidoList) {
        RespuestaPedido respuestaPedido = productoServiceAPI.stockOk(productoPedidoList);
        if(respuestaPedido.getCodigo_resultado() == 0 ){
            for(ProductoPedido productoPedido: productoPedidoList){
                productoPedidoRepo.save(productoPedido);
            }
            Carrito carrito = new Carrito();
            carrito.setTotal_carrito(respuestaPedido.getTotal());
            carrito.setListado_productos_pedidos(productoPedidoList);
            carritoRepo.save(carrito);
            return "Carrito creado con exito, \nTOTAL CARRITO: " + carrito.getTotal_carrito() + "\n ID CARRITO: " + carrito.getId_carrito();
        }else if(respuestaPedido.getCodigo_resultado() == 1){
            return "Error de codigo, los siguientes productos no existen " + respuestaPedido.getCodigo_producto_incorrecto().toString();
        } else if (respuestaPedido.getCodigo_resultado()== 2) {
            return "No hay stock suficiente de los siguientes productos" + respuestaPedido.getCodigo_producto_stockinsuficiente().toString();
        } else if (respuestaPedido.getCodigo_resultado() == 3) {
            return "Error de codigo, los siguientes productos no existen " + respuestaPedido.getCodigo_producto_incorrecto().toString()
                    + "\n" + "No hay stock suficiente de los siguientes productos" + respuestaPedido.getCodigo_producto_stockinsuficiente().toString();
        }else{
            return "Error desconocido";
        }
    }

    @Override
    @CircuitBreaker(name = "producto-service", fallbackMethod = "fallbackFindCart")
    @Retry(name = "producto-service")
    //listo
    public CarritoInfoDTO findCart(Long id_carrito) {
        Carrito carrito = carritoRepo.findById(id_carrito).orElse(null);
        CarritoInfoDTO carritoInfoDTO = new CarritoInfoDTO();
        List<ProductoPedido> productoPedidoList = carrito.getListado_productos_pedidos();
        List<ProductoInfoDTO> productoInfoDTOList = new ArrayList<>();
        for(ProductoPedido productoPedido: productoPedidoList){
            Producto producto = productoServiceAPI.findProductByCode(productoPedido.getCodigo_producto());
            ProductoInfoDTO productoInfoDTO = new ProductoInfoDTO();
            productoInfoDTO.setCodigo_producto(producto.getCodigo_producto());
            productoInfoDTO.setNombre(producto.getNombre());
            productoInfoDTO.setMarca(producto.getMarca());
            productoInfoDTO.setPrecio(producto.getPrecio());
            productoInfoDTO.setCantidad_pedida(productoPedido.getCantidad_pedida());
            Double total = productoPedido.getCantidad_pedida() * producto.getPrecio();
            productoInfoDTO.setTotal(total);
            productoInfoDTOList.add(productoInfoDTO);
        }
        carritoInfoDTO.setId_carrito(carrito.getId_carrito());
        carritoInfoDTO.setTotal_carrito(carrito.getTotal_carrito());
        carritoInfoDTO.setListado_productos_pedidos(productoInfoDTOList);
        //createException();
        return carritoInfoDTO;
    }
    @Override
    public List<CarritoInfoDTO> findCarts() {
        List<Carrito>  carritoList= carritoRepo.findAll();
        List<CarritoInfoDTO> carritoInfoDTOList = new ArrayList<>();
        for(Carrito carrito: carritoList){
            CarritoInfoDTO  carritoInfoDTO = this.findCart(carrito.getId_carrito());
            carritoInfoDTOList.add(carritoInfoDTO);
        }
        return carritoInfoDTOList;
    }
    @Override
    @CircuitBreaker(name = "producto-service", fallbackMethod = "fallbackUpdateCart")
    @Retry(name = "producto-service")
    //listo
    public String updateCart(CarritoInfoDTO actualizadoCarrito) {
        List<ProductoPedido> productoPedidoListNew = this.listadoActualizadoParaGuardar(actualizadoCarrito.getListado_productos_pedidos(),actualizadoCarrito.getId_carrito());
        List<ProductoPedido> productoPedidoListSend = this.listadoDiferencialParaEnviar(actualizadoCarrito.getListado_productos_pedidos(), actualizadoCarrito.getId_carrito());
        System.out.println("Listado enviado: ");
        for(ProductoPedido productoPedido: productoPedidoListSend){
            System.out.println("Codigo: " + productoPedido.getCodigo_producto());
            System.out.println("Cantidad diferencial: " + productoPedido.getCantidad_pedida());
        }
        RespuestaPedido respuestaPedido = productoServiceAPI.stockOk(productoPedidoListSend);
        if(respuestaPedido.getCodigo_resultado() == 0 ){
            for(ProductoPedido productoPedido :productoPedidoListNew){
                productoPedidoRepo.save(productoPedido);
            }
            Carrito carrito = carritoRepo.findById(actualizadoCarrito.getId_carrito()).orElse(null);
            carrito.setTotal_carrito(carrito.getTotal_carrito() + respuestaPedido.getTotal());
            carrito.setListado_productos_pedidos(productoPedidoListNew);
            carritoRepo.save(carrito);
            return "Carrito actualizado con exito";
        }else if(respuestaPedido.getCodigo_resultado() == 1){
            return "Error de codigo, los siguientes productos no existen " + respuestaPedido.getCodigo_producto_incorrecto().toString();
        } else if (respuestaPedido.getCodigo_resultado()== 2) {
            return "No hay stock suficiente de los siguientes productos" + respuestaPedido.getCodigo_producto_stockinsuficiente().toString();
        } else if (respuestaPedido.getCodigo_resultado() == 3) {
            return "Error de codigo, los siguientes productos no existen " + respuestaPedido.getCodigo_producto_incorrecto().toString()
                    + "\n" + "No hay stock suficiente de los siguientes productos" + respuestaPedido.getCodigo_producto_stockinsuficiente().toString();
        }else{
            return "Error desconocido";
        }
    }
    @Override
    public void deleteCart(Long id_carrito) {
        carritoRepo.deleteById(id_carrito);
    }

    @Override
    @CircuitBreaker(name = "producto-service", fallbackMethod = "fallbackCreateSale")
    @Retry(name = "producto-service")
    //listo
    public String createSale(Long id_carrito) {
        Carrito carrito = carritoRepo.findById(id_carrito).orElse(null);
        RespuestaPedido respuestaPedido = productoServiceAPI.createSale(carrito.getListado_productos_pedidos());
        if(respuestaPedido.getCodigo_resultado() == 0){
            return "Venta creada con exito! total venta: " + respuestaPedido.getTotal();
        }else if(respuestaPedido.getCodigo_resultado() == 1){
            return "Error de codigo, los siguientes productos no existen " + respuestaPedido.getCodigo_producto_incorrecto().toString();
        } else if (respuestaPedido.getCodigo_resultado()== 2) {
            return "No hay stock suficiente de los siguientes productos" + respuestaPedido.getCodigo_producto_stockinsuficiente().toString();
        } else if (respuestaPedido.getCodigo_resultado() == 3) {
            return "Error de codigo, los siguientes productos no existen " + respuestaPedido.getCodigo_producto_incorrecto().toString()
                    + "\n" + "No hay stock suficiente de los siguientes productos" + respuestaPedido.getCodigo_producto_stockinsuficiente().toString();
        }else{
            return "Error desconocido";
        }
    }

    @Override
    @CircuitBreaker(name = "producto-service", fallbackMethod = "fallbackModifySale")
    @Retry(name = "producto-service")
    //listo
    public String modifySale(CarritoInfoDTO actualizadoCarrito) {
        List<ProductoPedido> productoPedidoListNew = this.listadoActualizadoParaGuardar(actualizadoCarrito.getListado_productos_pedidos(),actualizadoCarrito.getId_carrito());
        List<ProductoPedido> productoPedidoListSend = this.listadoDiferencialParaEnviar(actualizadoCarrito.getListado_productos_pedidos(), actualizadoCarrito.getId_carrito());
        System.out.println("Listado enviado: ");
        for(ProductoPedido productoPedido: productoPedidoListSend){
            System.out.println("Codigo: " + productoPedido.getCodigo_producto());
            System.out.println("Cantidad diferencial: " + productoPedido.getCantidad_pedida());
        }
        RespuestaPedido respuestaPedido = productoServiceAPI.createSale(productoPedidoListSend);
        if(respuestaPedido.getCodigo_resultado() == 0 ){
            for(ProductoPedido productoPedido :productoPedidoListNew){
                productoPedidoRepo.save(productoPedido);
            }
            Carrito carrito = carritoRepo.findById(actualizadoCarrito.getId_carrito()).orElse(null);
            carrito.setTotal_carrito(carrito.getTotal_carrito() + respuestaPedido.getTotal());
            carrito.setListado_productos_pedidos(productoPedidoListNew);
            carritoRepo.save(carrito);
            return "Venta actualizada con exito";
        }else if(respuestaPedido.getCodigo_resultado() == 1){
            return "Error de codigo, los siguientes productos no existen " + respuestaPedido.getCodigo_producto_incorrecto().toString();
        } else if (respuestaPedido.getCodigo_resultado()== 2) {
            return "No hay stock suficiente de los siguientes productos" + respuestaPedido.getCodigo_producto_stockinsuficiente().toString();
        } else if (respuestaPedido.getCodigo_resultado() == 3) {
            return "Error de codigo, los siguientes productos no existen " + respuestaPedido.getCodigo_producto_incorrecto().toString()
                    + "\n" + "No hay stock suficiente de los siguientes productos" + respuestaPedido.getCodigo_producto_stockinsuficiente().toString();
        }else{
            return "Error desconocido";
        }
    }

    // -------------------------------METODOS ESPECIALES------------------------------------------
    public List<ProductoPedido> listadoActualizadoParaGuardar(List<ProductoInfoDTO> productoInfoDTOListNew, Long id_carrito_modificar){
        List<ProductoPedido> productoPedidoList = new ArrayList<>();
        List<ProductoInfoDTO> productoInfoDTOListOld = this.findCart(id_carrito_modificar).getListado_productos_pedidos();
        for(ProductoInfoDTO productoInfoDTONew: productoInfoDTOListNew){
            ProductoPedido productoPedido = new ProductoPedido();
            productoPedido.setCodigo_producto(productoInfoDTONew.getCodigo_producto());
            productoPedido.setCantidad_pedida(productoInfoDTONew.getCantidad_pedida());
            productoPedidoList.add(productoPedido);
            for(ProductoInfoDTO productoInfoDTOOld: productoInfoDTOListOld){
                if(productoInfoDTONew.getCodigo_producto().equals(productoInfoDTOOld.getCodigo_producto())){
                    productoInfoDTOOld.setCantidad_pedida(-1);
                }
            }
        }
        for(ProductoInfoDTO productoInfoDTOOld: productoInfoDTOListOld){
            if(productoInfoDTOOld.getCantidad_pedida() != -1){
                ProductoPedido productoPedido = new ProductoPedido();
                productoPedido.setCodigo_producto(productoInfoDTOOld.getCodigo_producto());
                productoPedido.setCantidad_pedida(0);
                productoPedidoList.add(productoPedido);
            }
        }
        return  productoPedidoList;
    }

    public List<ProductoPedido> listadoDiferencialParaEnviar (List<ProductoInfoDTO> productoInfoDTOListNew, Long id_carrito_modificar){
        boolean codigoOk = false;
        List<ProductoPedido> productoPedidoList = new ArrayList<>();
        List<ProductoInfoDTO> productoInfoDTOListOld = this.findCart(id_carrito_modificar).getListado_productos_pedidos();
        for(ProductoInfoDTO productoInfoDTONew: productoInfoDTOListNew){
            for(ProductoInfoDTO productoInfoDTOOld: productoInfoDTOListOld){
                if(productoInfoDTONew.getCodigo_producto().equals(productoInfoDTOOld.getCodigo_producto())){
                    codigoOk = true;
                    ProductoPedido productoPedidoSend = new ProductoPedido();
                    productoPedidoSend.setCodigo_producto(productoInfoDTONew.getCodigo_producto());
                    productoPedidoSend.setCantidad_pedida(productoInfoDTONew.getCantidad_pedida() - productoInfoDTOOld.getCantidad_pedida());
                    productoPedidoList.add(productoPedidoSend);
                    productoInfoDTOOld.setCantidad_pedida(-1);
                }
                if(codigoOk == false){
                    ProductoPedido productoPedidoSend2 = new ProductoPedido();
                    productoPedidoSend2.setCodigo_producto(productoInfoDTONew.getCodigo_producto());
                    productoPedidoSend2.setCantidad_pedida(productoInfoDTONew.getCantidad_pedida());
                    productoPedidoList.add(productoPedidoSend2);
                }
            }
        }
        for(ProductoInfoDTO productoInfoDTOOld: productoInfoDTOListOld){
            if(productoInfoDTOOld.getCantidad_pedida() != -1){
                ProductoPedido productoPedidoSend = new ProductoPedido();
                productoPedidoSend.setCodigo_producto(productoInfoDTOOld.getCodigo_producto());
                productoPedidoSend.setCantidad_pedida(productoInfoDTOOld.getCantidad_pedida() * -1);
                productoPedidoList.add(productoPedidoSend);
            }
        }
        return productoPedidoList;
    }

    public CarritoInfoDTO fallbackFindCart(Throwable throwable) {
        List<ProductoInfoDTO> productoInfoDTOList = new ArrayList<>();
        ProductoInfoDTO productoInfoDTO = new ProductoInfoDTO(99999999L,"Error","Error",9999999D,0,0D);
        productoInfoDTOList.add(productoInfoDTO);
        return new CarritoInfoDTO(99999999L,999999999D,productoInfoDTOList);
    }
    public void createException(){
        throw new IllegalArgumentException("Prueba de Resilience y Circuit breaker");
    }

    public String fallbackUpdateCart(Throwable throwable) {
        return "El servicio Productos esta momentaneamente fuera de servicio";
    }
    public String fallbackCreateSale(Throwable throwable) {
        return "El servicio Productos esta momentaneamente fuera de servicio";
    }
    public String fallbackModifySale(Throwable throwable) {
        return "El servicio Productos esta momentaneamente fuera de servicio";
    }
    public String fallbackCreateCart(Throwable throwable) {
        return "El servicio Productos esta momentaneamente fuera de servicio";
    }


}