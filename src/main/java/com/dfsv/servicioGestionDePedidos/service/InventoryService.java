package com.dfsv.servicioGestionDePedidos.service;
import com.dfsv.servicioGestionDePedidos.client.CatalogClient;
import com.dfsv.servicioGestionDePedidos.dto.ProductDto;
import com.dfsv.servicioGestionDePedidos.model.Inventory;
import com.dfsv.servicioGestionDePedidos.repository.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository repo;
    private final CatalogClient catalog;
    public List<Inventory> findAll() {
        return repo.findAll();
    }
    /* ------------- AJUSTAR EN Δ ------------- */
    @Transactional
    public int adjustQuantity(Long id, int delta) {

        /* 1) Obtener el producto actual de Catálogo */
        ProductDto prod = catalog.getProduct(id);
        if (prod == null)
            throw new EntityNotFoundException("Producto " + id + " no existe en Catálogo");

        int newQty = prod.quantity() + delta;

        /* 2) Actualizar Inventario local (opcional) */
        Inventory inv = repo.findById(id).orElse(new Inventory(id, 0));
        inv.setQuantity(newQty);
        repo.save(inv);

        /* 3) Enviar el producto con quantity actualizado a Catálogo */
        ProductDto actualizado = new ProductDto(
                prod.id(),
                prod.name(),
                prod.description(),
                prod.price(),
                newQty
        );
        catalog.putProduct(actualizado);

        /* 4) Log */
        System.out.printf("📦 INVENTARIO :: Producto %d ⇒ %d unidades (Δ %d)%n",
                id, newQty, delta);

        return newQty;
    }

    /* ---------- SOLO CONSULTAR ---------- */
    public int getQuantity(Long id) {
        return repo.findById(id)
                .orElse(new Inventory(id, 0))
                .getQuantity();
    }
}