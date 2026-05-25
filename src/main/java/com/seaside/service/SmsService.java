package com.seaside.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.from-number}")
    private String fromNumber;

    public void enviarSms(String telefono, String mensaje) {
        Twilio.init(accountSid, authToken);

        // Asegura formato internacional colombiano
        String numeroDestino = telefono.replaceAll("\\D", "");
        if (!numeroDestino.startsWith("+")) {
            numeroDestino = "+57" + numeroDestino;
        }

        Message.creator(
            new PhoneNumber(numeroDestino),
            new PhoneNumber(fromNumber),
            mensaje
        ).create();
    }
}