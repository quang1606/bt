package com.example.baitapthymeleaf.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String id;
    private String fullName;
    private String job;
    private String gender;
    private String city;
    private double salary;
    private String birthday;
}
