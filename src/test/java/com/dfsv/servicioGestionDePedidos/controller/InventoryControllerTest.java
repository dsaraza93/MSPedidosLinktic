package com.dfsv.servicioGestionDePedidos.controller;

import com.dfsv.servicioGestionDePedidos.model.Inventory;
import com.dfsv.servicioGestionDePedidos.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias (capa Web) para {@link InventoryController}.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService service;

    private final ObjectMapper mapper = new ObjectMapper();

    /*───────────────────────────────────────────────
          GET /api/inventory
     ───────────────────────────────────────────────*/
    @Test
    @DisplayName("Devuelve el listado completo de inventario")
    void getAllReturnsInventoryList() throws Exception {
        Inventory i1 = new Inventory(1L, 10);   // constructor visto en el servicio
        Inventory i2 = new Inventory(2L, 5);

        Mockito.when(service.findAll()).thenReturn(List.of(i1, i2));

        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId", is(1)))
                .andExpect(jsonPath("$[0].quantity",  is(10)));
    }

    /*───────────────────────────────────────────────
          PUT /api/inventory/adjust/{id}
     ───────────────────────────────────────────────*/
    @Test
    @DisplayName("Ajusta la cantidad y devuelve la respuesta con productId y newQuantity")
    void adjustQuantity() throws Exception {
        Long id   = 1L;
        int delta = -3;
        int newQ  = 7;          // valor que el servicio devolverá

        Mockito.when(service.adjustQuantity(eq(id), eq(delta))).thenReturn(newQ);

        String body = """
                      { "delta": %d }
                      """.formatted(delta);

        mockMvc.perform(put("/api/inventory/adjust/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId",  is(1)))
                .andExpect(jsonPath("$.newQuantity", is(newQ)));
    }
}
