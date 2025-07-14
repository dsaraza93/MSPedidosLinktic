package com.dfsv.servicioGestionDePedidos.dto;

/** Respuesta: { "productId": 1, "newQuantity": 97 } */
public record QuantityChangeResponse(Long productId, int newQuantity) {}
