package com.target.myRetail.controller;

import com.target.myRetail.model.ProductWithProductPrice;
import com.target.myRetail.errors.MyRetailException;
import com.target.myRetail.service.MyRetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("myRetail")
public class MyRetailController {

    @Autowired
    private MyRetailService myRetailService;

   @GetMapping(path ="/product/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ProductWithProductPrice getProduct(@PathVariable("id") String productId) throws MyRetailException {
       Optional<ProductWithProductPrice> product = myRetailService.getProduct(productId);
       if(product.isPresent()) {
           return product.get();
       }
         throw new MyRetailException("Product Not Found");
   }

}
