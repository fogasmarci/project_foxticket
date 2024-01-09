package com.greenfoxacademy.springwebapp.integrations;

import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductTypeTests {

    @Autowired
    ProductTypeRepository productTypeRepository;

    @Test
    @Transactional
    public void testLoadProductType() {
        List<ProductType> types = productTypeRepository.findAll();

        for (ProductType type : types) {
            productTypeRepository.delete(type);
            productTypeRepository.flush();
        }

        var a = 5;
    }
}
