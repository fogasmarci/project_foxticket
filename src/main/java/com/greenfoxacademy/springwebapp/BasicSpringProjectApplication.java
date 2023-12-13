package com.greenfoxacademy.springwebapp;

import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BasicSpringProjectApplication implements CommandLineRunner {
  ProductTypeRepository productTypeRepository;

  @Autowired
  public BasicSpringProjectApplication(ProductTypeRepository productTypeRepository) {
    this.productTypeRepository = productTypeRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(BasicSpringProjectApplication.class, args);
  }


  @Override
  public void run(String... args) throws Exception {
    ProductType productType1 = new ProductType("jegy");
    ProductType productType2 = new ProductType("bérlet");

    Product product1 = new Product("vonaljegy", 480, 90, "teszt1");
    Product product2 = new Product("havi diák bérlet", 4000, 9000, "teszt2");
    Product product3 = new Product("havi bérlet", 9500, 9000, "teszt3");

    productType1.addProduct(product1);
    productType2.addProduct(product2);
    productType2.addProduct(product3);

    productTypeRepository.save(productType1);
    productTypeRepository.save(productType2);
  }
}
