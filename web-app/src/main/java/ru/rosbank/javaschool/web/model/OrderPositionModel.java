package ru.rosbank.javaschool.web.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPositionModel {
    private int id;
    private int orderId;
    private int productId;
    private String productName;
    private int productPrice;
    private int productQuantity;
}
