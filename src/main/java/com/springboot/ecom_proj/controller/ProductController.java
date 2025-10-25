package com.springboot.ecom_proj.controller;

import com.springboot.ecom_proj.model.Product;
import com.springboot.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @RequestMapping("/")
    public String greet(){
        return "**** Hi ****";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getListOfProducts(){
        return new ResponseEntity<>(service.getListOfProducts() , HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){

        Product product = service.getProductById(id);

        if(product != null)
            return new ResponseEntity<>(service.getProductById(id), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        try {
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int id){
        Product product = service.getProductById(id);
        byte[] imageFile = product.getImageDate();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProductById(@PathVariable int id,
                                                    @RequestPart Product product,
                                                    @RequestPart MultipartFile imageFile) {
        try {
            Product product1 = service.updateProductById(id, product, imageFile);
            if (product1 != null) {
                return new ResponseEntity<>("Product updated Successfully!", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Product not found!", HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>("Cannot update the Product", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable int id){
        Product product = service.getProductById(id);
        if(product != null){
            service.deleteProductById(id);
            return new ResponseEntity<>("Product Deleted!", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Product Not Found!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        System.out.println("Keyword : "+keyword);
        List<Product> products = service.searchProducts(keyword);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
