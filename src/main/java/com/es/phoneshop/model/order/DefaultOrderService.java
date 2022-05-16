package com.es.phoneshop.model.order;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DefaultOrderService {
    private static final DefaultOrderService INSTANCE = new DefaultOrderService();
    private final String errorMessage = "Value is required";

    private DefaultOrderService() {
    }

    public static DefaultOrderService getInstance() {
        return INSTANCE;
    }

    protected BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }

    public List<PaymentMethod> getPaymentMethod() {
        return Arrays.asList(PaymentMethod.values());
    }

    public void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors, Consumer<String> consumer) {
        String value = request.getParameter(parameter);

        if (value == null || value.isEmpty()) {
            errors.put(parameter, errorMessage);
        } else {
            consumer.accept(value);
        }
    }

    public void setDeliveryDate(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("deliveryDate");

        if (value == null || value.isEmpty()) {
            errors.put("deliveryDate", errorMessage);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy")
                    .withLocale(request.getLocale());
            order.setDeliveryDate(LocalDate.parse(value, formatter));
        }
    }

    public void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("paymentMethod");

        if (value == null || value.isEmpty()) {
            errors.put("paymentMethod", errorMessage);
        } else {
            order.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }
}
