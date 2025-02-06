package com.deadStock.callFeature.devsoc.Repos;

import com.deadStock.callFeature.devsoc.entity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Product> findBynameRegex(String keyword);
    List<Product> findBycategory(String category);



}
