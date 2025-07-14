package com.dfsv.servicioGestionDePedidos.dto;

/** Body que llega desde el front: { "delta": -3 } */
public record QuantityChangeRequest(int delta) {}
