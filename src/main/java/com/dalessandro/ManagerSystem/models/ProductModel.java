package com.dalessandro.ManagerSystem.models;

import com.dalessandro.ManagerSystem.models.enums.UnitMeasure;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_products_barcode", columnNames = "barcode")
        }
)
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq")
    @SequenceGenerator(name = "products_seq", sequenceName = "products_seq", allocationSize = 50)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(nullable = false, length = 80)
    private String brand;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "manufacture_date")
    private LocalDate manufactureDate;

    @Column(nullable = false, length = 50)
    private String barcode;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false,
            check = @CheckConstraint(name = "chk_products_updated_after_created", constraint = "updated_at >= created_at")
    )
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_products_category"))
    private CategoryModel category;

    @Lob
    @Column
    private String description;

    @Column(name = "cost_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal costPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_measure", nullable = false, length = 20)
    private UnitMeasure unitMeasure;

    @Column(name = "min_stock_quantity", nullable = false)
    private int minStockQuantity;
}