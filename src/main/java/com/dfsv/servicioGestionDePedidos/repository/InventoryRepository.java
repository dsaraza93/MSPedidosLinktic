package com.dfsv.servicioGestionDePedidos.repository;

import com.dfsv.servicioGestionDePedidos.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {}
