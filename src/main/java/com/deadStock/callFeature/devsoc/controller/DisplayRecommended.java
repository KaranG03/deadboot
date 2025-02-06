package com.deadStock.callFeature.devsoc.controller;


import com.deadStock.callFeature.devsoc.Repos.ProductRepository;
import com.deadStock.callFeature.devsoc.Repos.UserRepo;
import com.deadStock.callFeature.devsoc.entity.Product;
import com.deadStock.callFeature.devsoc.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/show-recommended")
public class DisplayRecommended {

    @Autowired
    private User user;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    private ResponseEntity<?> show(@RequestBody Map<String, String> requestBody) {
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
        List<Product> finList = new ArrayList<>();
        Random rand = new Random();

        for (String i : user.getCategories()) {
            List<Product> l1 = productRepository.findBycategory(i);
            Collections.shuffle(l1, rand); // Shuffle the list to randomize order
            for (int c = 0; c < Math.min(l1.size(), 3); c++) {
                finList.add(l1.get(c));
            }
        }
        Collections.shuffle(finList, rand);
        return new ResponseEntity<>(finList, HttpStatus.OK);
    }

}
