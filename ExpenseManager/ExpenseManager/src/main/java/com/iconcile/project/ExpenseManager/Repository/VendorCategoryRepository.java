package com.iconcile.project.ExpenseManager.Repository;


import com.iconcile.project.ExpenseManager.Entity.VendorCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VendorCategoryRepository extends JpaRepository<VendorCategory, Long> {

    Optional<VendorCategory> findByVendorNameIgnoreCase(@Param("name") String name);
}
