package com.dfsv.servicioGestionDePedidos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor          //  ←  ESTE
public class Inventory {

    @Id
    private Long productId;

    private int quantity;
}
