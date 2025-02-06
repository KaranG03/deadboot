package com.deadStock.callFeature.devsoc.service;

import com.deadStock.callFeature.devsoc.Repos.ProductRepository;
import com.deadStock.callFeature.devsoc.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    public ProductService(ProductRepository productRepository, CloudinaryService cloudinaryService) {
        this.productRepository = productRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public Product saveProduct(Product product, List<MultipartFile> images) throws IOException {
        // Upload images to Cloudinary
        if (images != null && !images.isEmpty()) {
            List<String> uploadedImageUrls = cloudinaryService.uploadFiles(images);

            // Map Cloudinary URLs to Product's Image objects
            for (String url : uploadedImageUrls) {
                Product.Image image = new Product.Image();
                image.setUrl(url);
                // Optionally, set a publicId if needed
                product.getImages().add(image);
            }
        }

        return productRepository.save(product);
    }
}
