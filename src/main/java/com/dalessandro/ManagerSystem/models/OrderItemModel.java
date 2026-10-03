package com.dalessandro.ManagerSystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItemModel {
    private long id;
    private int quantity;
    private BigDecimal unitPrice;
    private OrderModel order;
    private ProductModel product;
}
