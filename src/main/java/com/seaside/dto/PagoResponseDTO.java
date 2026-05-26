package com.seaside.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 DTO que retorna la URL de pago generada por MercadoPago
 para redirigir al cliente al checkout.
 
 IMPORTANTE: el campo sandboxInitPoint DEBE llamarse así,
 porque el frontend Angular lo lee con esa clave exacta.
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponseDTO {
    private String initPoint;
    private String sandboxInitPoint;   // ← corregido (antes era sandboxURL)
    private String preferenceId;       // ← corregido (antes era preferenceID)
}