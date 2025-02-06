package com.deadStock.callFeature.devsoc.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.*;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class User {
    private String name;
    private String email;
    @Id
    private ObjectId id;
    private String password;

    private Avatar avatar;

    private String role = "user";  // Default role

    private Date createdAt = new Date();  // Default value

    private String resetPasswordToken;

    private Date resetPasswordExpire;
    //private List<Product> featuredProd = new ArrayList<>();
    private Set<ObjectId> votedProd = new HashSet<>();
    private List<String> categories = new ArrayList<>();
    private Set<ObjectId> objs = new LinkedHashSet<>();

    private int primeStatus;
}
