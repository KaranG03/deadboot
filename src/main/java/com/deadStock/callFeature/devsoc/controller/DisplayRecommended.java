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

import java.util.*;

@RestController
@RequestMapping("/show-recommended")
public class DisplayRecommended {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/{id0}")
    private ResponseEntity<?> show(@PathVariable String id0) {

        // Convert userId from String to ObjectId
        ObjectId userId;
        try {
            userId = new ObjectId(id0);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid userId format");
        }

        // Find user
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = userOptional.get();

        Set<String> objectIds = new LinkedHashSet<>();
        Random rand = new Random();

        for (String category : user.getCategories()) {
            List<Product> l1 = productRepository.findBycategory(category);

            // Debugging output to verify fetched products
            System.out.println("Products for category " + category + ": ");
            for (Product product : l1) {
                System.out.println("Product ID: " + product.getId().toString());
            }

            for (int c = 0; c < Math.min(l1.size(), 3); c++) {
                String productId = l1.get(c).getId().toString(); // Convert ObjectId to String

                // Check if ObjectId is already in the set to ensure uniqueness
                if (!objectIds.contains(productId)) {
                    System.out.println("Adding ObjectId: " + productId);
                    objectIds.add(productId); // Store only the String representation of ObjectId to ensure uniqueness
                } else {
                    System.out.println("Duplicate ObjectId detected: " + productId);
                }
            }
        }

// Retrieve products using the stored ObjectIds (now in String form)
        List<Product> toAdd = new ArrayList<>();
        for (String objectIdStr : objectIds) {
            ObjectId objectId = new ObjectId(objectIdStr); // Convert String back to ObjectId
            Optional<Product> productOptional = productRepository.findById(objectId);
            productOptional.ifPresent(toAdd::add); // Add product to the list if it exists
        }

// Shuffle the products list
        Collections.shuffle(toAdd, rand);

// Add shuffled products to user's featured products list
        user.getFeaturedProd().addAll(toAdd);
        userRepo.save(user); // Save the updated user object

// Return the updated list of featured products
        return new ResponseEntity<>(user.getFeaturedProd(), HttpStatus.OK);

    }
}
