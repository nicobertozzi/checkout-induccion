package services;

import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import model.PreferenceModel;
import model.dto.PaymentDTO;

public class PaymentsService {

    /**
     * Create the payment
     *
     * @param paymentDTO PaymentDTO payment
     * @return payment
     * @throws MPException If the MercadoPago API call fails
     */
    public Payment save(PaymentDTO paymentDTO) throws MPException {
        Payment payment = new Payment();

        payment.setToken(paymentDTO.getToken())
                .setDescription(paymentDTO.getDescription())
                .setInstallments(paymentDTO.getInstallments())
                .setPaymentMethodId(paymentDTO.getPaymentMethodId())
                .setIssuerId(paymentDTO.getIssuerId());

        // Si no llega como parametro (V2) deberia buscar la preference??
        // por ahora lo calculo con la preference en memoria...
        if(paymentDTO.getAmount() == null) {
            Double transactionAmount = PreferenceModel.preference.getItems().stream()
                    .mapToDouble(i -> (i.getQuantity() * i.getUnitPrice()))
                    .sum();

            payment.setTransactionAmount(transactionAmount.floatValue());
        } else {
            payment.setTransactionAmount(paymentDTO.getAmount());
        }

        com.mercadopago.resources.datastructures.payment.Payer payer = new com.mercadopago.resources.datastructures.payment.Payer();
        String email = "test_user_19562067@testuser.com";
        // priorizo el email del pago, sino el de la preference, y sino uno por default...
        if(paymentDTO.getEmail() != null && !paymentDTO.getEmail().isEmpty()) {
            email = paymentDTO.getEmail();
        } else if(PreferenceModel.preference.getPayer().getEmail() != null && !PreferenceModel.preference.getPayer().getEmail().isEmpty()) {
            email = PreferenceModel.preference.getPayer().getEmail();
        }
        payer.setEmail(email);

        payment.setPayer(payer);

        payment.save();

        return payment;
    }

}
