package com.rodr1alex.productoservice.service;

import com.rodr1alex.productoservice.dto.ProductoPedido;
import com.rodr1alex.productoservice.dto.RespuestaPedido;
import com.rodr1alex.productoservice.model.Producto;
import com.rodr1alex.productoservice.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProductoService implements IProductoService{
    @Autowired
    private IProductoRepository productoRepo;
    @Override
    public void createProduct(Producto nuevoProducto) {
        productoRepo.save(nuevoProducto);
    }

    @Override
    public Producto findProduct(Long codigo_producto) {
        return productoRepo.findById(codigo_producto).orElse(null);
    }

    @Override
    public List<Producto> findProducts() {
        return productoRepo.findAll();
    }

    @Override
    public void updateProduct(Producto actualizadoProducto) {
        productoRepo.save(actualizadoProducto);
    }

    @Override
    public void deleteProduct(Long codigo_producto) {
        productoRepo.deleteById(codigo_producto);
    }

    @Override
    public RespuestaPedido createSale(List<ProductoPedido> productoPedidoList) {
        System.out.println("Se recibio una peticion para actualizar inventario");
        RespuestaPedido respuestaPedido = new RespuestaPedido();
        respuestaPedido.setCodigo_resultado(this.correctQuery(productoPedidoList));
        if(respuestaPedido.getCodigo_resultado() == 0){
            this.updateStock(productoPedidoList);
            respuestaPedido.setTotal(this.totalSale(productoPedidoList));
        }else if(respuestaPedido.getCodigo_resultado() == 1){
            respuestaPedido.setCodigo_producto_incorrecto(this.findCodeError(productoPedidoList));
        }else if(respuestaPedido.getCodigo_resultado() == 2){
            respuestaPedido.setCodigo_producto_stockinsuficiente(this.findStockError(productoPedidoList));
        }else if(respuestaPedido.getCodigo_resultado() == 3){
            respuestaPedido.setCodigo_producto_incorrecto(this.findCodeError(productoPedidoList));
            respuestaPedido.setCodigo_producto_stockinsuficiente(this.findStockError(productoPedidoList));
        }
        return  respuestaPedido;
    }
    @Override
    public RespuestaPedido stockOk(List<ProductoPedido> productoPedidoList) {
        System.out.println("Se recibio una peticion para consultar inventario");
        RespuestaPedido respuestaPedido = new RespuestaPedido();
        respuestaPedido.setCodigo_resultado(this.correctQuery(productoPedidoList));
        if(respuestaPedido.getCodigo_resultado() == 0){
            respuestaPedido.setTotal(this.totalSale(productoPedidoList));
        }else if(respuestaPedido.getCodigo_resultado() == 1){
            respuestaPedido.setCodigo_producto_incorrecto(this.findCodeError(productoPedidoList));
        }else if(respuestaPedido.getCodigo_resultado() == 2){
            respuestaPedido.setCodigo_producto_stockinsuficiente(this.findStockError(productoPedidoList));
        }else if(respuestaPedido.getCodigo_resultado() == 3){
            respuestaPedido.setCodigo_producto_incorrecto(this.findCodeError(productoPedidoList));
            respuestaPedido.setCodigo_producto_stockinsuficiente(this.findStockError(productoPedidoList));
        }
        return  respuestaPedido;
    }

    public Integer correctQuery(List<ProductoPedido> productoPedidoList){
        //retorna codigoerror
        // 0:ok, 1:error de codigoproducto, 2:error de stock, 3:error de codigo y stock
        boolean codigoOk = false;
        boolean errorCodigo = false;
        boolean errorStock = false;
        List<Producto> productoList = this.findProducts();
        for(ProductoPedido productoPedido: productoPedidoList){
            for(Producto producto: productoList){
                if(productoPedido.getCodigo_producto().equals(producto.getCodigo_producto())){
                    codigoOk = true;
                    if(producto.getCantidad_disponible() < productoPedido.getCantidad_pedida()){
                        errorStock = true;
                    }
                }
            }
            if(codigoOk == false){
                errorCodigo = true;
            }
            codigoOk = false;
        }

        if(errorCodigo == true && errorStock == true){
            return 3;
        }
        else if(errorStock == true){
            return 2;
        } else if (errorCodigo == true) {
            return 1;
        }else{
            return 0;
        }
    }
    public void updateStock(List<ProductoPedido> productoPedidoList){
        List<Producto> productoList = this.findProducts();
        for(ProductoPedido productoPedido: productoPedidoList){
            for(Producto producto: productoList){
                if(productoPedido.getCodigo_producto().equals(producto.getCodigo_producto())){
                    producto.setCantidad_disponible(producto.getCantidad_disponible() - productoPedido.getCantidad_pedida());
                    this.updateProduct(producto);
                }
            }
        }
    }
    public Double totalSale(List<ProductoPedido> productoPedidoList){
        Double total = 0D;
        List<Producto> productoList = this.findProducts();
        for(ProductoPedido productoPedido: productoPedidoList){
            for(Producto producto: productoList){
                if(productoPedido.getCodigo_producto().equals(producto.getCodigo_producto())){
                    total += productoPedido.getCantidad_pedida() * producto.getPrecio();
                }
            }
        }
        return total;
    }

    public List<Long> findCodeError(List<ProductoPedido> productoPedidoList){
        boolean codigoOk = false;
        List<Long> codigoErroneoList = new ArrayList<>();
        List<Producto> productoList = this.findProducts();
        for(ProductoPedido productoPedido: productoPedidoList){
            for(Producto producto: productoList){
                if(productoPedido.getCodigo_producto().equals(producto.getCodigo_producto())){
                    codigoOk = true;
                }
            }
            if(codigoOk == false){
                codigoErroneoList.add(productoPedido.getCodigo_producto());
            }
            codigoOk = false;
        }
        return codigoErroneoList;
    }

    public  List<Long> findStockError(List<ProductoPedido> productoPedidoList){
        List<Long> stockErroneoList = new ArrayList<>();
        List<Producto> productoList = this.findProducts();
        for(ProductoPedido productoPedido: productoPedidoList){
            for(Producto producto: productoList){
                if(productoPedido.getCodigo_producto().equals(producto.getCodigo_producto())){
                    if(producto.getCantidad_disponible() < productoPedido.getCantidad_pedida()){
                        stockErroneoList.add(producto.getCodigo_producto());
                    }
                }
            }
        }
        return stockErroneoList;
    }
}
