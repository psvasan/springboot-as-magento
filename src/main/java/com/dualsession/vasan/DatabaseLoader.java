package com.dualsession.vasan;

import com.dualsession.vasan.catalog.model.Product;
import com.dualsession.vasan.catalog.repository.ProductRepository;
import com.dualsession.vasan.user.model.User;
import com.dualsession.vasan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseLoader implements CommandLineRunner {


    private ProductRepository productRepository;

    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

   @Override
    public void run(String... args) throws Exception {

       System.out.println("Create Product Entity");

//       Product product1 = new Product(
//               "Laptop",
//               "Laptop",
//               new BigDecimal("1200.00"),
//               "C001"
//       );
//       Product product2 = new Product(
//               "Smartphone",
//               "Smartphone",
//               new BigDecimal("800.00"),
//               "C002"
//       );
//
//       // 2. Use the .save() method provided by JpaRepository
//       productRepository.save(product1);
//       productRepository.save(product2);
//
//       System.out.println("Products successfully saved!");
//      productRepository.findAll().forEach(p ->
//              System.out.println("Retrieved Product: " + p.getName() + " - $" + p.getPrice())
//      );
//
//       System.out.println("Create User Entity");
//
//       User user1 = new User();
//       user1.setUsername("user123");
//
//       String password = passwordEncoder.encode("user123");
//       user1.setPassword(password);
//
//       Set<String> roles = new HashSet<>();
//       roles.add("USER");
//       user1.setRoles(roles);
//       user1.setEmail("user123@ya.com");
//       user1.setFirstName("User123");
//       user1.setLastName("Frontend User");
//       userRepository.save(user1);
//
//       User user2 = new User();
//       user2.setUsername("admin123");
//       String password2 = passwordEncoder.encode("admin123");
//       user2.setPassword(password2);
//       Set<String> roles2 = new HashSet<>();
//       roles2.add("ADMIN");
//       user2.setRoles(roles2);
//       user2.setEmail("admin123@ya.com");
//       user2.setFirstName("Admin123");
//       user2.setLastName("Backend User");
//       userRepository.save(user2);


   }

}
