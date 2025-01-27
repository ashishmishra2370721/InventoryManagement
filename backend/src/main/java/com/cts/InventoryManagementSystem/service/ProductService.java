package com.cts.InventoryManagementSystem.service;

import com.cts.InventoryManagementSystem.dto.ProductDTO;
import com.cts.InventoryManagementSystem.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Response saveProduct(ProductDTO productDTO);
    Response updateProduct(ProductDTO productDTO);
    Response getAllProducts();
    Response getProductById(Long id);
    Response deleteProduct(Long id);
}
