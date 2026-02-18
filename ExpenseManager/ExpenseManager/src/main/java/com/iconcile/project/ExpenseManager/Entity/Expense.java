package com.iconcile.project.ExpenseManager.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    @Positive
    private Double amount;
    @Column(unique = true)
    private String vendorName;
    private String description;
    private String category;
    private Boolean isAnomaly = false;
}
