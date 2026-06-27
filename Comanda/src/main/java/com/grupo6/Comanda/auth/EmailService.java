package com.grupo6.Comanda.auth;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.*;
import com.sendgrid.helpers.mail.objects.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${comanda.mail.from:noreply@comanda.pe}")
    private String mailFrom;

    public void sendEmail(String to, String subject, String body) throws Exception {
        Email from      = new Email(mailFrom);
        Email toEmail   = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail       = new Mail(from, subject, toEmail, content);

        SendGrid sg     = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);

        if (response.getStatusCode() >= 400) {
            throw new RuntimeException("SendGrid error " + response.getStatusCode()
                    + ": " + response.getBody());
        }
    }
}