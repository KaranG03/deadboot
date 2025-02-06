package com.deadStock.callFeature.devsoc.controller;


import com.deadStock.callFeature.devsoc.Repos.ProductRepository;
import com.deadStock.callFeature.devsoc.Repos.UserRepo;
import com.deadStock.callFeature.devsoc.entity.Product;
import com.deadStock.callFeature.devsoc.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/sugg")
public class tapOn {

    @Autowired
    private User user;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepo userRepo;

    @PostMapping("/id/{id}")
    private ResponseEntity<String> addCat(@RequestBody Map<String, String> requestBody, @PathVariable ObjectId id) {
        String userIdStr = requestBody.get("userId");

        // Convert userId from String to ObjectId
        ObjectId userId;
        try {
            userId = new ObjectId(userIdStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid userId format");
        }

        // Find user
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = userOptional.get();

        // Fetch product
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        Product product = productOptional.get();

        // Add category if not already present
        String category = product.getCategory();
        if (!user.getCategories().contains(category)) {
            if(!user.getCategories().contains(category)){
                user.getCategories().add(category);
                userRepo.save(user);
            }

        }

        return ResponseEntity.ok("Category added successfully");
    }

}
