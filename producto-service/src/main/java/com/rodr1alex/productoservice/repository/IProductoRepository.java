package com.rodr1alex.productoservice.repository;

import com.rodr1alex.productoservice.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
}
