package com.rodr1alex.ventaservice.repository;

import com.rodr1alex.ventaservice.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVentaRepository extends JpaRepository<Venta, Long> {


}
