package com.deadStock.callFeature.devsoc.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {




    @Id
    private ObjectId id;
    private String name;
    private String description;
    private double price;
    private double ratings = 0;
    private List<Image> images;
    private String category;
    private int stock = 1;
    private int numOfReviews = 0;

    private ObjectId user;
    private int votes;
    private Date createdAt = new Date();

    public static class Image {
        private String publicId;
        private String url;

        public String getPublicId() { return publicId; }
        public void setPublicId(String publicId) { this.publicId = publicId; }

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }

    @JsonProperty("id")
    public String getIdAsString() {
        return id != null ? id.toHexString() : null;  // Convert ObjectId to hex string
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    }
