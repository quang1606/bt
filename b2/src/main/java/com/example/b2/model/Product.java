package com.example.b2.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    String id;
    String name;
    String description;
    int price;
    String brand;
}

