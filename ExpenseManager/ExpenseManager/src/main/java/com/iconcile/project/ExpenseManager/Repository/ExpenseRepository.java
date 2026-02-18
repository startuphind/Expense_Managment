package com.iconcile.project.ExpenseManager.Repository;

import com.iconcile.project.ExpenseManager.Entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT COALESCE(AVG(e.amount), 0.0) FROM Expense e WHERE e.category = :category")
    Double getAverageAmountByCategory(@Param("category") String category);

    long countByCategory(String category);
    List<Expense> findByIsAnomalyTrue();
}