package com.dfsv.servicioGestionDePedidos.controller;

import com.dfsv.servicioGestionDePedidos.dto.QuantityChangeRequest;
import com.dfsv.servicioGestionDePedidos.dto.QuantityChangeResponse;
import com.dfsv.servicioGestionDePedidos.model.Inventory;
import com.dfsv.servicioGestionDePedidos.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @Operation(summary = "Consultar stock completo del inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario consultado exitosamente")
    })
    @GetMapping
    public List<Inventory> getAll() {
        return service.findAll();
    }

    @Operation(summary = "Ajustar cantidad de producto en inventario mediante Delta (Δ)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad ajustada correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta (bad request)")
    })
    @PutMapping("/adjust/{id}")
    public ResponseEntity<QuantityChangeResponse> adjust(
            @PathVariable Long id,
            @RequestBody(description = "Objeto JSON con el valor delta para ajustar cantidad")
            @org.springframework.web.bind.annotation.RequestBody QuantityChangeRequest req) {

        int newQty = service.adjustQuantity(id, req.delta());
        return ResponseEntity.ok(new QuantityChangeResponse(id, newQty));
    }
}
