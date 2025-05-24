package com.example.Food.delivery.controller;

import com.example.Food.delivery.dto.FoodMenuDTO;
import com.example.Food.delivery.service.FoodMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foodmenus")
public class FoodMenuController {

    private final FoodMenuService foodMenuService;

    public FoodMenuController(FoodMenuService foodMenuService) {
        this.foodMenuService = foodMenuService;
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<FoodMenuDTO>> getMenusByVendor(@PathVariable Long vendorId) {
        List<FoodMenuDTO> menus = foodMenuService.getMenusByVendor(vendorId);
        return ResponseEntity.ok(menus);
    }

    @PostMapping
    public ResponseEntity<FoodMenuDTO> addMenu(@RequestBody FoodMenuDTO foodMenuDto) {
        FoodMenuDTO savedMenu = foodMenuService.addMenu(foodMenuDto);
        return ResponseEntity.status(201).body(savedMenu);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodMenuDTO> updateMenu(@PathVariable Long id, @RequestBody FoodMenuDTO foodMenuDto) {
        FoodMenuDTO updatedMenu = foodMenuService.updateMenu(id, foodMenuDto);
        return ResponseEntity.ok(updatedMenu);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        foodMenuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}
