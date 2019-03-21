package services;

import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import model.PreferenceModel;
import model.dto.PaymentDTO;

public class PaymentsService {

    public static Payment save(PaymentDTO paymentDTO) throws MPException {
        Payment payment = new Payment();

        payment.setToken(paymentDTO.getToken())
                .setDescription(paymentDTO.getDescription())
                .setInstallments(paymentDTO.getInstallments())
                .setPaymentMethodId(paymentDTO.getPaymentMethodId())
                .setIssuerId(paymentDTO.getIssuerId());

        // Si no llega como parametro (punto 4) deberia buscar la preference??
        // por ahora lo hago a pata...
        if(paymentDTO.getAmount() == null) {
            Double transactionAmount = PreferenceModel.preference.getItems().stream()
                    .mapToDouble(i -> (i.getQuantity() * i.getUnitPrice()))
                    .sum();

            payment.setTransactionAmount(transactionAmount.floatValue());
        } else {
            payment.setTransactionAmount(paymentDTO.getAmount());
        }
        com.mercadopago.resources.datastructures.payment.Payer payer = new com.mercadopago.resources.datastructures.payment.Payer();
        if(paymentDTO.getEmail() == null) {
            payer.setEmail(PreferenceModel.preference.getPayer().getEmail());
        } else {
            payer.setEmail(paymentDTO.getEmail());
        }
        payment.setPayer(payer);

        payment.save();

        return payment;
    }

}
