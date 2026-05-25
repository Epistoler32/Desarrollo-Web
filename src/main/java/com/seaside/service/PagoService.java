package com.seaside.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.seaside.dto.PagoResponseDTO;
import com.seaside.model.Pedido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PagoService {
    
    @Value("${mercadopago.access_token}")
   private String accessToken;

    /*
     Crea una preferencia de pago en MercadoPago para el pedido dado.
     Retorna las URLs de checkout (sandbox para pruebas).
     */

    public PagoResponseDTO crearPreferencia(Pedido pedido) throws MPException, MPApiException {

        MercadoPagoConfig.setAccessToken(accessToken);

        System.out.println("Token usado: " + accessToken);

        //item pedido
        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .id(String.valueOf(pedido.getId()))
                .title("Pedido SesaSide #" + pedido.getId())
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(pedido.getTotal()).setScale(2, java.math.RoundingMode.HALF_UP))
                .currencyId("COP")
                .build();

        //URLs retorno tras el pago
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:4200/mis-pedidos?pago=exitoso")
                .failure("http://localhost:4200/mis-pedidos?pago=fallido")
                .pending("http://localhost:4200/mis-pedidos?pago=pendiente")
                .build();

        //Contruir preferencia
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(List.of(item))
                .backUrls(backUrls)
                .externalReference(String.valueOf(pedido.getId())) // referencia para identificar el pedido
                .build();

        try {
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);
            return new PagoResponseDTO(
                    preference.getInitPoint(),
                    preference.getSandboxInitPoint(),
                    preference.getId()
            );
        } catch (MPApiException e) {
            System.out.println("Status MP: " + e.getStatusCode());
            if (e.getApiResponse() != null) {
                System.out.println("Respuesta MP: " + e.getApiResponse().getContent());
            } else {
                System.out.println("Sin respuesta - causa: " + e.getCause());
            }
            throw e;
        }
    
    }
}