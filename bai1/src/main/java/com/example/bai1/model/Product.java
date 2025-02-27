package com.example.bai1.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private String id;
    private String name;
    private String description;
    private int price;
    private String brand;

}
