package com.dfsv.servicioGestionDePedidos.service;

import com.dfsv.servicioGestionDePedidos.client.CatalogClient;
import com.dfsv.servicioGestionDePedidos.dto.ProductDto;
import com.dfsv.servicioGestionDePedidos.model.Inventory;
import com.dfsv.servicioGestionDePedidos.repository.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link InventoryService}.
 */
@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock  private InventoryRepository repo;
    @Mock  private CatalogClient       catalog;

    @InjectMocks
    private InventoryService service;

    /*──────────────────────────────
         adjustQuantity – flujo feliz
     ──────────────────────────────*/
    @Test
    @DisplayName("Ajusta la cantidad, actualiza repositorio y Catálogo, y devuelve newQty")
    void adjustQuantityHappyPath() {
        Long id   = 1L;
        int delta = -3;
        int start = 10;
        int expected = 7;                // 10 + (-3)

        ProductDto prod = new ProductDto(id, "Laptop", "Gaming", 1500.0, start);
        when(catalog.getProduct(id)).thenReturn(prod);

        Inventory existing = new Inventory(id, start);
        when(repo.findById(id)).thenReturn(Optional.of(existing));

        // repo.save(...) devuelve el mismo objeto modificado
        when(repo.save(any(Inventory.class))).thenAnswer(inv -> inv.getArgument(0));

        int result = service.adjustQuantity(id, delta);

        assertEquals(expected, result);

        /* Verifica que se guardó inventario con la nueva cantidad */
        ArgumentCaptor<Inventory> invCaptor = ArgumentCaptor.forClass(Inventory.class);
        verify(repo).save(invCaptor.capture());
        assertEquals(expected, invCaptor.getValue().getQuantity());

        /* Verifica que se envió a Catálogo el producto con cantidad nueva */
        ArgumentCaptor<ProductDto> dtoCaptor = ArgumentCaptor.forClass(ProductDto.class);
        verify(catalog).putProduct(dtoCaptor.capture());
        assertEquals(expected, dtoCaptor.getValue().quantity());
    }

    /*──────────────────────────────
         adjustQuantity – producto no existe
     ──────────────────────────────*/
    @Test
    @DisplayName("Lanza EntityNotFoundException cuando Catálogo devuelve null")
    void adjustQuantityProductNotFound() {
        when(catalog.getProduct(42L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class,
                () -> service.adjustQuantity(42L, 5));
    }

    /*──────────────────────────────
         getQuantity – inventario encontrado
     ──────────────────────────────*/
    @Test
    @DisplayName("getQuantity devuelve la cantidad almacenada")
    void getQuantityExisting() {
        Inventory inv = new Inventory(5L, 12);
        when(repo.findById(5L)).thenReturn(Optional.of(inv));

        int qty = service.getQuantity(5L);

        assertEquals(12, qty);
    }

    /*──────────────────────────────
         getQuantity – inventario inexistente
     ──────────────────────────────*/
    @Test
    @DisplayName("getQuantity devuelve 0 cuando no hay registro")
    void getQuantityMissing() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        int qty = service.getQuantity(99L);

        assertEquals(0, qty);
    }
}
