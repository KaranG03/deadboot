package com.deadStock.callFeature.devsoc.controller;


import com.deadStock.callFeature.devsoc.Repos.ProductRepository;
import com.deadStock.callFeature.devsoc.Repos.UserRepo;
import com.deadStock.callFeature.devsoc.entity.Product;
import com.deadStock.callFeature.devsoc.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/vote")
public class CrowdSourced {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepo userRepo;


    @PostMapping
    public ResponseEntity<?> voteP(@RequestBody Map<String, String> requestBody){
        String prodIdStr = requestBody.get("id");
        String userIdStr = requestBody.get("userId");
        ObjectId userId;
        ObjectId prodId;
        try {
            prodId = new ObjectId(prodIdStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid userId format");
        }

        try {
          userId = new ObjectId(userIdStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid userId format");
        }
        Optional<User> user = userRepo.findById(userId);
        if(!user.get().getVotedProd().contains(prodId)){
            Optional<Product> curr = productRepository.findById(prodId);
            curr.ifPresent(product -> product.setVotes(product.getVotes() + 1));
            productRepository.save(curr.get());
            user.get().getVotedProd().add(prodId);
            userRepo.save(user.get());
            return new ResponseEntity<>("Thanks for voting" , HttpStatus.OK);
        }

        return new ResponseEntity<>("Already voted !",HttpStatus.UNAUTHORIZED);
    }
}
