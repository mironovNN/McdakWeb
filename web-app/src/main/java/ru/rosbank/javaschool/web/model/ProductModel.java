package ru.rosbank.javaschool.web.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
    private int id;
    private String name;
    private int price;
    private int quantity;
    private String imageUrl;
}
