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
        Set<ObjectId> objectIds2 = new HashSet<>(user.getObjs());
        Random rand = new Random();

        // Iterate over user categories and process products
        for (String category : user.getCategories()) {
            List<Product> products = productRepository.findBycategory(category);

            // Debugging output to verify fetched products
            System.out.println("Products for category " + category + ": ");
            for (Product product : products) {
                System.out.println("Product ID: " + product.getId().toString());
            }

            // Randomly select a few products from the category (e.g., pick 3 random products)
            int numProductsToSelect = Math.min(3, products.size()); // Adjust the number as needed
            Collections.shuffle(products, rand); // Shuffle the products list

            // Add selected products' ObjectIds to the set
            for (int i = 0; i < numProductsToSelect; i++) {
                ObjectId productId = products.get(i).getId(); // Directly use ObjectId
                objectIds2.add(productId); // Add ObjectId to the set (ensures uniqueness)
            }
        }

        // Save the updated user object with the updated obj set
        user.setObjs(objectIds2); // Ensure the ObjectId set is saved
        userRepo.save(user);

        // Convert ObjectIds to Strings to return just the ID values in the response
        List<String> objectIdList = new ArrayList<>();
        for (ObjectId objectId : objectIds2) {
            objectIdList.add(objectId.toString()); // Convert ObjectId to String
        }

        // Return the list of ObjectIds as Strings
        return new ResponseEntity<>(objectIdList, HttpStatus.OK);
    }
}
