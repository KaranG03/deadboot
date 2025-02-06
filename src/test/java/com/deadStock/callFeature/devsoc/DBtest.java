package com.deadStock.callFeature.devsoc;


import com.deadStock.callFeature.devsoc.Repos.ProductRepository;
import com.deadStock.callFeature.devsoc.Repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DBtest {


    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepo userRepo;
    @Test
    public void dbCheck(){
        System.out.println(userRepo.findByname("dv"));
    }
}
