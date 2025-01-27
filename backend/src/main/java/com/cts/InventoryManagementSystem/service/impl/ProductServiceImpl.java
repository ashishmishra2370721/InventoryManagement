package com.cts.InventoryManagementSystem.service.impl;

import com.cts.InventoryManagementSystem.dto.ProductDTO;
import com.cts.InventoryManagementSystem.dto.Response;
import com.cts.InventoryManagementSystem.entity.Category;
import com.cts.InventoryManagementSystem.entity.Product;
import com.cts.InventoryManagementSystem.exceptions.NotFoundException;
import com.cts.InventoryManagementSystem.repository.CategoryRepository;
import com.cts.InventoryManagementSystem.repository.ProductRepository;
import com.cts.InventoryManagementSystem.service.ProductService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import jakarta.annotation.PostConstruct; 

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void initDatabase() {
        if (productRepository.count() == 0) {
            log.info("Initializing default products...");
            addDefaultProducts();
        }
    }

    private void addDefaultProducts() {
        // Create a default category
        Category defaultCategory = categoryRepository.save(
                Category.builder().name("Default Category").build()
        );

        // Add default products
        productRepository.saveAll(List.of(
                Product.builder()
                        .name("TurboMax Charger")
                        .sku("PROD001")
                        .price(new BigDecimal("10.50"))
                        .stockQuantity(100)
                        .description("Description for TurboMax Charger")
                        .category(defaultCategory)
                        .build(),
                        Product.builder()
                        .name("EcoPower Piston")
                        .sku("PROD002")
                        .price(new BigDecimal("20.00"))
                        .stockQuantity(50)
                        .description("Description for EcoPower Piston")
                        .category(defaultCategory)
                        .build()
        ));

        log.info("Default products added.");
    }




    
    @Override
    public Response saveProduct(ProductDTO productDTO) {

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()-> new NotFoundException("Category Not Found"));

        //map out product dto to product entity
        Product productToSave = Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .description(productDTO.getDescription())
                .category(category)
                .build();


        //save the product to our database
        productRepository.save(productToSave);
        return Response.builder()
                .status(200)
                .message("Product successfully saved")
                .build();
    }

    @Override
    public Response updateProduct(ProductDTO productDTO) {

        Product existingProduct = productRepository.findById(productDTO.getProductId())
                .orElseThrow(()-> new NotFoundException("Product Not Found"));

        
        //Check if category is to be changed for the product
        if (productDTO.getCategoryId() != null && productDTO.getCategoryId() > 0){

            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(()-> new NotFoundException("Category Not Found"));
            existingProduct.setCategory(category);
        }

        //check and update fiedls

        if (productDTO.getName() !=null && !productDTO.getName().isBlank()){
            existingProduct.setName(productDTO.getName());
        }

        if (productDTO.getSku() !=null && !productDTO.getSku().isBlank()){
            existingProduct.setSku(productDTO.getSku());
        }

        if (productDTO.getDescription() !=null && !productDTO.getDescription().isBlank()){
            existingProduct.setDescription(productDTO.getDescription());
        }

        if (productDTO.getPrice() !=null && productDTO.getPrice().compareTo(BigDecimal.ZERO) >=0){
            existingProduct.setPrice(productDTO.getPrice());
        }

        if (productDTO.getStockQuantity() !=null && productDTO.getStockQuantity() >=0){
            existingProduct.setStockQuantity(productDTO.getStockQuantity());
        }

        //Update the product
        productRepository.save(existingProduct);
        return Response.builder()
                .status(200)
                .message("Product successfully Updated")
                .build();

    }

    @Override
    public Response getAllProducts() {

        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<ProductDTO> productDTOS = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .products(productDTOS)
                .build();
    }

    @Override
    public Response getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));


        return Response.builder()
                .status(200)
                .message("success")
                .product(modelMapper.map(product, ProductDTO.class))
                .build();
    }

    @Override
    public Response deleteProduct(Long id) {

        productRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));

        productRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Product successfully deleted")
                .build();
    }













}
