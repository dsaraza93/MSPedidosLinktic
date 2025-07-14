package com.dfsv.servicioGestionDePedidos.client;

import com.dfsv.servicioGestionDePedidos.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class CatalogClient {

    private final RestClient rest;

    public CatalogClient(@Value("${catalogo.base-url}") String baseUrl,
                         @Value("${catalogo.api-key}")  String apiKey) {

        /* Construimos RestClient con la cabecera X-API-KEY por defecto  */
        this.rest = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-API-KEY", apiKey)
                .build();
    }


    public ProductDto getProduct(Long id) throws RestClientException {
        return rest.get()
                .uri("/api/products/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(ProductDto.class);
    }


    public void putProduct(ProductDto dto) throws RestClientException {
        rest.put()
                .uri("/api/products/{id}", dto.id())
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }


    public void updateQuantity(Long id, int newQty) {
        ProductDto current = getProduct(id);          // 1) leer
        ProductDto updated = new ProductDto(
                current.id(),
                current.name(),
                current.description(),
                current.price(),
                newQty);                              // 2) cambiar qty
        putProduct(updated);                          // 3) enviar
    }


    public boolean exists(Long id) {
        try {
            HttpStatusCode code = rest.head()
                    .uri("/api/products/{id}", id)
                    .retrieve()
                    .toBodilessEntity()
                    .getStatusCode();
            return code.is2xxSuccessful();
        } catch (RestClientException ex) {
            return false;
        }
    }
}
