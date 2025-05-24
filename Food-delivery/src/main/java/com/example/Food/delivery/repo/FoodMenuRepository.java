package com.example.Food.delivery.repo;

import com.example.Food.delivery.Entity.FoodMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoodMenuRepository extends JpaRepository<FoodMenu, Long> {
    List<FoodMenu> findByVendor_Id(Long vendorId);
}

