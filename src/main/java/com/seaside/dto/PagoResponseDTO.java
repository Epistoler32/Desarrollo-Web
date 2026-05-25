package com.seaside.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 DTO que retorna la URL de pago generada por MercadoPago 
 para redirigir al cliente al checkout.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PagoResponseDTO {
    private String initPoint; // URL de pago de real
    private String sandboxURL; //URL de pago en modo de prueba
    private String preferenceID;
}
