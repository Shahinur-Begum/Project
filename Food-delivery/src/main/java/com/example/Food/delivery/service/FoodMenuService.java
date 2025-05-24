package com.example.Food.delivery.service;

import com.example.Food.delivery.dto.FoodMenuDTO;
import com.example.Food.delivery.Entity.FoodMenu;
import com.example.Food.delivery.Entity.User;
import com.example.Food.delivery.repo.FoodMenuRepository;
import com.example.Food.delivery.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodMenuService {

    private final FoodMenuRepository foodMenuRepository;
    private final UserRepository userRepository;

    public FoodMenuService(FoodMenuRepository foodMenuRepository, UserRepository userRepository) {
        this.foodMenuRepository = foodMenuRepository;
        this.userRepository = userRepository;
    }

    public List<FoodMenuDTO> getMenusByVendor(Long vendorId) {
        List<FoodMenu> menus = foodMenuRepository.findByVendor_Id(vendorId);
        return menus.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FoodMenuDTO addMenu(FoodMenuDTO foodMenuDto) {
        User vendor = userRepository.findById(foodMenuDto.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + foodMenuDto.getVendorId()));

        FoodMenu foodMenu = convertToEntity(foodMenuDto);
        foodMenu.setVendor(vendor);

        FoodMenu savedMenu = foodMenuRepository.save(foodMenu);
        return convertToDTO(savedMenu);
    }

    public FoodMenuDTO updateMenu(Long menuId, FoodMenuDTO foodMenuDto) {
        FoodMenu existingMenu = foodMenuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + menuId));

        existingMenu.setMName(foodMenuDto.getMName());
        existingMenu.setDescription(foodMenuDto.getDescription());
        existingMenu.setPrice(foodMenuDto.getPrice());
        existingMenu.setAvailability(foodMenuDto.getAvailability());

        FoodMenu updatedMenu = foodMenuRepository.save(existingMenu);
        return convertToDTO(updatedMenu);
    }

    public void deleteMenu(Long menuId) {
        foodMenuRepository.deleteById(menuId);
    }

    // Helper conversion methods

    private FoodMenuDTO convertToDTO(FoodMenu foodMenu) {
        FoodMenuDTO dto = new FoodMenuDTO();
        dto.setMId(foodMenu.getId());
        dto.setVendorId(foodMenu.getVendor().getId());
        dto.setMName(foodMenu.getMName());
        dto.setDescription(foodMenu.getDescription());
        dto.setPrice(foodMenu.getPrice());
        dto.setAvailability(foodMenu.getAvailability());
        return dto;
    }

    private FoodMenu convertToEntity(FoodMenuDTO dto) {
        FoodMenu entity = new FoodMenu();
        entity.setMName(dto.getMName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setAvailability(dto.getAvailability());
        return entity;
    }
}
