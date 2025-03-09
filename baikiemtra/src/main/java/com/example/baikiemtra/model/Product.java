package com.example.baikiemtra.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int id;
    private String name;
    private String description;
    private String thumbnail;
    private int price;
    private double rating;
    private int priceDiscount;

}
