package onlineStore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    private String id;
    private User user;
    private Product product;
    private Date date;
    private double price;
    OrderStatus orderStatus;
    private int qty;
    PaymentMethod paymentMethod;
}
