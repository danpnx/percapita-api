package br.com.project.utils;

import com.mailgun.api.v4.MailgunEmailVerificationApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.verification.AddressValidationResponse;

public class EmailValidation {
    public static AddressValidationResponse validEmail(String email) {
        MailgunEmailVerificationApi mailgunEmailVerificationApi = MailgunClient.config("4c4811881ea93a213f4bbac0954e842a-2de3d545-f9b9139e")
                .createApi(MailgunEmailVerificationApi.class);

        return mailgunEmailVerificationApi.validateAddress(email);
    }
}
