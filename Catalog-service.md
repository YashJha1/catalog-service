**Catalog-service**





Create repo Catalog-service and clone it locally.



Install Java, maven



Go to spring boot initializr,  https://start.spring.io/



| Field        | catalog-service                                                    | inventory-service          |

| ------------ | ------------------------------------------------------------------ | -------------------------- |

| Group        | `com.eci`                                                          | `com.eci`                  |

| Artifact     | `catalog-service`                                                  | `inventory-service`        |

| Package Name | `com.eci.catalogservice`                                           | `com.eci.inventoryservice` |

| Java         | 17                                                                 | 17                         |

| Dependencies | Spring Web, Spring Data JPA, PostgreSQL Driver, DevTools, Actuator | Same                       |





download zip and extract and copy it under catalog-service project folder.





Go to /src/main/java/com/eci/catalogservice and create separate folders **model, service, repository, controller**



create **product.java** file inside **/src/main/java/com/eci/catalogservice/model**



package com.eci.catalogservice.model;



import jakarta.persistence.\*;

import java.math.BigDecimal;



@Entity

@Table(name = "products")

public class Product {



&nbsp;   @Id

&nbsp;   @GeneratedValue(strategy = GenerationType.IDENTITY)

&nbsp;   @Column(name = "product\_id")   // maps to DB column

&nbsp;   private Long productId;



&nbsp;   @Column(nullable = false, unique = true)

&nbsp;   private String sku;



&nbsp;   @Column(nullable = false)

&nbsp;   private String name;



&nbsp;   private String category;



&nbsp;   @Column(nullable = false)

&nbsp;   private BigDecimal price;



&nbsp;   @Column(name = "is\_active")

&nbsp;   private boolean isActive = true;



&nbsp;   // Default constructor

&nbsp;   public Product() {}



&nbsp;   // Getters and setters

&nbsp;   public Long getProductId() {

&nbsp;       return productId;

&nbsp;   }



&nbsp;   public void setProductId(Long productId) {

&nbsp;       this.productId = productId;

&nbsp;   }



&nbsp;   public String getSku() {

&nbsp;       return sku;

&nbsp;   }



&nbsp;   public void setSku(String sku) {

&nbsp;       this.sku = sku;

&nbsp;   }



&nbsp;   public String getName() {

&nbsp;       return name;

&nbsp;   }



&nbsp;   public void setName(String name) {

&nbsp;       this.name = name;

&nbsp;   }



&nbsp;   public String getCategory() {

&nbsp;       return category;

&nbsp;   }



&nbsp;   public void setCategory(String category) {

&nbsp;       this.category = category;

&nbsp;   }



&nbsp;   public BigDecimal getPrice() {

&nbsp;       return price;

&nbsp;   }



&nbsp;   public void setPrice(BigDecimal price) {

&nbsp;       this.price = price;

&nbsp;   }



&nbsp;   public boolean isActive() {

&nbsp;       return isActive;

&nbsp;   }



&nbsp;   public void setActive(boolean active) {

&nbsp;       isActive = active;

&nbsp;   }

}





create **ProductService.java** file inside **/src/main/java/com/eci/catalogservice/service**

package com.eci.catalogservice.service;



import com.eci.catalogservice.model.Product;

import com.eci.catalogservice.repository.ProductRepository;

import org.springframework.stereotype.Service;



import java.util.List;

import java.util.Optional;



@Service

public class ProductService {



&nbsp;   private final ProductRepository repository;



&nbsp;   public ProductService(ProductRepository repository) {

&nbsp;       this.repository = repository;

&nbsp;   }



&nbsp;   public List<Product> getAllProducts() {

&nbsp;       return repository.findAll();

&nbsp;   }



&nbsp;   public Optional<Product> getProductById(Long id) {

&nbsp;       return repository.findById(id);

&nbsp;   }



&nbsp;   public Optional<Product> getProductBySku(String sku) {

&nbsp;       return repository.findBySku(sku);

&nbsp;   }



&nbsp;   public Product createProduct(Product product) {

&nbsp;       return repository.save(product);

&nbsp;   }



&nbsp;   public Product updateProduct(Long id, Product product) {

&nbsp;       return repository.findById(id).map(existing -> {

&nbsp;           existing.setSku(product.getSku());

&nbsp;           existing.setName(product.getName());

&nbsp;           existing.setCategory(product.getCategory());

&nbsp;           existing.setPrice(product.getPrice());

&nbsp;           existing.setActive(product.isActive());

&nbsp;           return repository.save(existing);

&nbsp;       }).orElse(null);

&nbsp;   }



&nbsp;   public void deleteProduct(Long id) {

&nbsp;       repository.deleteById(id);

&nbsp;   }

}







create **ProductRepository.java** file inside **/src/main/java/com/eci/catalogservice/repository**

package com.eci.catalogservice.repository;



import com.eci.catalogservice.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



import java.util.Optional;



@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {

&nbsp;   Optional<Product> findBySku(String sku);

}









create **ProductController.java** file inside **/src/main/java/com/eci/catalogservice/controller**

package com.eci.catalogservice.controller;



import com.eci.catalogservice.model.Product;

import com.eci.catalogservice.service.ProductService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.\*;



import java.util.List;



@RestController

@RequestMapping("/v1/products")

public class ProductController {



&nbsp;   private final ProductService service;



&nbsp;   public ProductController(ProductService service) {

&nbsp;       this.service = service;

&nbsp;   }



&nbsp;   @GetMapping

&nbsp;   public List<Product> getAllProducts() {

&nbsp;       return service.getAllProducts();

&nbsp;   }



&nbsp;   @GetMapping("/{id}")

&nbsp;   public ResponseEntity<Product> getProductById(@PathVariable Long id) {

&nbsp;       return service.getProductById(id)

&nbsp;               .map(ResponseEntity::ok)

&nbsp;               .orElse(ResponseEntity.notFound().build());

&nbsp;   }



&nbsp;   @GetMapping("/sku/{sku}")

&nbsp;   public ResponseEntity<Product> getProductBySku(@PathVariable String sku) {

&nbsp;       return service.getProductBySku(sku)

&nbsp;               .map(ResponseEntity::ok)

&nbsp;               .orElse(ResponseEntity.notFound().build());

&nbsp;   }



&nbsp;   @PostMapping

&nbsp;   public Product createProduct(@RequestBody Product product) {

&nbsp;       return service.createProduct(product);

&nbsp;   }



&nbsp;   @PutMapping("/{id}")

&nbsp;   public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {

&nbsp;       Product updated = service.updateProduct(id, product);

&nbsp;       return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();

&nbsp;   }



&nbsp;   @DeleteMapping("/{id}")

&nbsp;   public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

&nbsp;       service.deleteProduct(id);

&nbsp;       return ResponseEntity.noContent().build();

&nbsp;   }

}





Go to **src/main/resources/application.properties**

\# PostgreSQL database connection

spring.datasource.url=jdbc:postgresql://localhost:5434/catalogdb

spring.datasource.username=yash

spring.datasource.password=yash123

spring.datasource.driver-class-name=org.postgresql.Driver



\# Hibernate \& JPA

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true



\# Actuator / Metrics

management.endpoints.web.exposure.include=health,info,prometheus

management.endpoint.prometheus.enabled=true



\# Server port

server.port=8080







go inside **Infra** folder, create init folder, inside init folder create catalog folder.

Copy eci\_products.csv to infra/init/catalog directory





create **init\_catalog.sql** in infra/init/catalog directory,

-- init\_catalog.sql

CREATE TABLE IF NOT EXISTS products (...);

COPY products(...) FROM '/docker-entrypoint-initdb.d/eci\_products.csv' DELIMITER ',' CSV HEADER;







create **docker-compose.yml** file inside infra folder,

version: "3.8"

services:

&nbsp; # ---------- Catalog ----------

&nbsp; catalog-db:

&nbsp;   image: postgres:15

&nbsp;   container\_name: catalog-db

&nbsp;   restart: always

&nbsp;   environment:

&nbsp;     POSTGRES\_DB: catalogdb

&nbsp;     POSTGRES\_USER: yash

&nbsp;     POSTGRES\_PASSWORD: yash123

&nbsp;   ports:

&nbsp;     - "5434:5432"

&nbsp;   volumes:

&nbsp;     - catalog\_data:/var/lib/postgresql/data

&nbsp;     - ./init/catalog:/docker-entrypoint-initdb.d



&nbsp; catalog-service:

&nbsp;   build:

&nbsp;     context: ../catalog-service

&nbsp;     dockerfile: Dockerfile

&nbsp;   container\_name: catalog-service

&nbsp;   depends\_on:

&nbsp;     - catalog-db

&nbsp;   ports:

&nbsp;     - "8080:8080"

&nbsp;   environment:

&nbsp;     SPRING\_DATASOURCE\_URL: jdbc:postgresql://catalog-db:5432/catalogdb

&nbsp;     SPRING\_DATASOURCE\_USERNAME: yash

&nbsp;     SPRING\_DATASOURCE\_PASSWORD: yash123

&nbsp;     SPRING\_JPA\_HIBERNATE\_DDL\_AUTO: update

&nbsp;     SERVER\_PORT: 8080



&nbsp; # ---------- Inventory ----------

&nbsp; inventory-db:

&nbsp;   image: postgres:15

&nbsp;   container\_name: inventory-db

&nbsp;   restart: always

&nbsp;   environment:

&nbsp;     POSTGRES\_DB: inventorydb

&nbsp;     POSTGRES\_USER: yash

&nbsp;     POSTGRES\_PASSWORD: yash123

&nbsp;   ports:

&nbsp;     - "5433:5432"

&nbsp;   volumes:

&nbsp;     - inventory\_data:/var/lib/postgresql/data

&nbsp;     - ./init/inventory:/docker-entrypoint-initdb.d



&nbsp; inventory-service:

&nbsp;   build:

&nbsp;     context: ../inventory-service

&nbsp;     dockerfile: Dockerfile

&nbsp;   container\_name: inventory-service

&nbsp;   depends\_on:

&nbsp;     - inventory-db

&nbsp;   ports:

&nbsp;     - "8081:8081"

&nbsp;   environment:

&nbsp;     SPRING\_DATASOURCE\_URL: jdbc:postgresql://inventory-db:5432/inventorydb

&nbsp;     SPRING\_DATASOURCE\_USERNAME: yash

&nbsp;     SPRING\_DATASOURCE\_PASSWORD: yash123

&nbsp;     SPRING\_JPA\_HIBERNATE\_DDL\_AUTO: update

&nbsp;     SERVER\_PORT: 8081



volumes:

&nbsp; catalog\_data:

&nbsp; inventory\_data:





Create **Dockerfile** inside the project folder **(catalog-service)**

\# --------------------------------------------

\# Use an official lightweight Java runtime

\# --------------------------------------------

FROM openjdk:17-jdk-slim



\# --------------------------------------------

\# Set working directory inside the container

\# --------------------------------------------

WORKDIR /app



\# --------------------------------------------

\# Copy the JAR file built by Maven into the container

\# --------------------------------------------

\# This assumes your JAR is created inside target/

COPY target/catalog-service-0.0.1-SNAPSHOT.jar app.jar



\# --------------------------------------------

\# Expose the port your Spring Boot app runs on

\# --------------------------------------------

EXPOSE 8080



\# --------------------------------------------

\# Run the application

\# --------------------------------------------

ENTRYPOINT \["java", "-jar", "/app/app.jar"]





| Step                                          | Description                                      |

| --------------------------------------------- | ------------------------------------------------ |

| `FROM openjdk:17-jdk-slim`                    | Uses a small and efficient Java 17 image         |

| `WORKDIR /app`                                | Sets working directory inside container          |

| `COPY target/... app.jar`                     | Copies your built `.jar` file into the container |

| `EXPOSE 8080`                                 | Opens port 8080 for communication                |

| `ENTRYPOINT \["java", "-jar", "/app/app.jar"]` | Launches your Spring Boot app                    |





**Build JAR file,**

cd ~/Microservices/catalog-service

mvn clean package -DskipTests



**Build docker image,**

docker build -t catalog-service:1.0 .



**Verify image,**

docker images

&nbsp;

**start container via docker compose,**

cd ~/Microservices/infra

docker compose up -d



**Confirm database table exist inside the container,**

docker exec -it catalog-db psql -U yash -d catalogdb

\\dt

SELECT COUNT(\*) FROM products;

\\q





**verify API,**

http://localhost:8080/v1/products





**completed a containerized full-stack microservice deployment**





