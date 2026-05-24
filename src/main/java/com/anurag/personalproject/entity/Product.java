package com.anurag.personalproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String category;
    private Double basePrice;
    private Integer stock;
}