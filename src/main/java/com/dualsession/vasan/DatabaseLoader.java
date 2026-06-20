package com.dualsession.vasan;

import com.dualsession.vasan.catalog.model.Product;
import com.dualsession.vasan.catalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
   private ProductRepository productRepository;


   @Override
    public void run(String... args) throws Exception {

// 1. Create instances of your Entity
       Product product1 = new Product(
               "Laptop",
               "Laptop",
               new BigDecimal("1200.00"),
               "C001"
       );
       Product product2 = new Product(
               "Smartphone",
               "Smartphone",
               new BigDecimal("800.00"),
               "C002"
       );

       // 2. Use the .save() method provided by JpaRepository
       productRepository.save(product1);
       productRepository.save(product2);

       System.out.println("Entities successfully saved!");
      productRepository.findAll().forEach(p ->
              System.out.println("Retrieved Product: " + p.getName() + " - $" + p.getPrice())
      );

   }

}
