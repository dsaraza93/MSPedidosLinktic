package com.dfsv.servicioGestionDePedidos.dto;

/** Mapea exactamente la respuesta JSON de Catálogo */
public record ProductDto(Long id,
                         String name,
                         String description,
                         double price,
                         int quantity) {}
