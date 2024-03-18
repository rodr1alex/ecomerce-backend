package com.rodr1alex.carritoservice.Repository;

import com.rodr1alex.carritoservice.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICarritoRepository extends JpaRepository<Carrito, Long> {
}
