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

        // Get existing set of ObjectIds
        Set<ObjectId> objectIds2 = user.getObjs();
        Random rand = new Random();

        // Iterate over user categories and process products
        for (String category : user.getCategories()) {
            List<Product> l1 = productRepository.findBycategory(category);

            // Debugging output to verify fetched products
            System.out.println("Products for category " + category + ": ");
            for (Product product : l1) {
                System.out.println("Product ID: " + product.getId().toString());
            }

            // Process each product in the list
            for (Product product : l1) {
                ObjectId productId = product.getId(); // Directly use ObjectId

                // Add product's ObjectId to the set if it's not already present in the user's existing set
                if (!objectIds2.contains(productId)) {
                    System.out.println("Adding ObjectId: " + productId);
                    objectIds2.add(productId); // Add ObjectId directly to the set
                } else {
                    System.out.println("Duplicate ObjectId detected: " + productId);
                }
            }
        }

        // Fetch products using the stored ObjectIds (in the user's objs set)
        List<Product> products = new ArrayList<>();
        for (ObjectId objectId : objectIds2) {
            Optional<Product> productOptional = productRepository.findById(objectId);
            productOptional.ifPresent(products::add); // Add product to list if it exists
        }

        // Shuffle the products list (optional)
        Collections.shuffle(products, rand);

        // Save the updated user object with the updated obj set
        user.setObjs(objectIds2); // Ensure the ObjectId set is saved
        userRepo.save(user);

        // Return the updated list of products
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
