Hệ thống thông tin quản lý bán hàng (MIS)
Java Swing MVC + Hibernate/JPA + MySQL
Bảng dữ liệu chính: Customer, Product, Order, OrderDetail
Để project IntelliJ + Maven chạy gọn với Java 17, một cấu hình thực dụng là Hibernate ORM 7.3.0.Final; theo tài liệu Hibernate, dòng này tương thích Java 17/21 và Jakarta Persistence 3.2. Với MySQL, artifact Maven JDBC chính thức là com.mysql:mysql-connector-j; tài liệu MySQL cho biết Connector/J 9.6 hỗ trợ MySQL 8.0+ và JRE 8+. (Hibernate Documentation)
Thiết kế:
•	Table: customers, products, orders, order_details
•	Entity Java:
o	Customer
o	Product
o	SalesOrder (map tới table orders)
o	OrderDetail
Dùng SalesOrder thay vì class tên Order để tránh trùng với từ khóa SQL.
1. Cấu trúc project IntelliJ + Maven
sales-mis/
├── pom.xml
├── database/
│   ├── 01_create_database.sql
│   ├── 02_create_tables.sql
│   └── 03_insert_sample_data.sql
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/salesmis/
│       │       ├── AppLauncher.java
│       │       ├── config/
│       │       │   └── JpaUtil.java
│       │       ├── model/
│       │       │   ├── entity/
│       │       │   │   ├── Customer.java
│       │       │   │   ├── Product.java
│       │       │   │   ├── SalesOrder.java
│       │       │   │   └── OrderDetail.java
│       │       │   ├── enumtype/
│       │       │   │   └── OrderStatus.java
│       │       │   └── dto/
│       │       │       ├── OrderLineInput.java
│       │       │       ├── CustomerRevenueDTO.java
│       │       │       ├── ProductSalesDTO.java
│       │       │       ├── MonthlyRevenueDTO.java
│       │       │       └── StatusCountDTO.java
│       │       ├── dao/
│       │       │   ├── CustomerDAO.java
│       │       │   ├── ProductDAO.java
│       │       │   ├── SalesOrderDAO.java
│       │       │   ├── OrderDetailDAO.java
│       │       │   ├── ReportDAO.java
│       │       │   └── impl/
│       │       │       ├── CustomerDAOImpl.java
│       │       │       ├── ProductDAOImpl.java
│       │       │       ├── SalesOrderDAOImpl.java
│       │       │       ├── OrderDetailDAOImpl.java
│       │       │       └── ReportDAOImpl.java
│       │       ├── service/
│       │       │   ├── OrderService.java
│       │       │   ├── LookupService.java
│       │       │   ├── ReportService.java
│       │       │   └── impl/
│       │       │       ├── OrderServiceImpl.java
│       │       │       ├── LookupServiceImpl.java
│       │       │       └── ReportServiceImpl.java
│       │       ├── controller/
│       │       │   ├── OrderController.java
│       │       │   └── ReportController.java
│       │       └── view/
│       │           ├── MainFrame.java
│       │           └── OrderManagementPanel.java
│       └── resources/
│           └── META-INF/
│               └── persistence.xml
2. MySQL – tạo database và dữ liệu mẫu
2.1. database/01_create_database.sql
DROP DATABASE IF EXISTS sales_mis;
CREATE DATABASE sales_mis
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE sales_mis;
2.2. database/02_create_tables.sql
USE sales_mis;

CREATE TABLE customers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    customer_code VARCHAR(20) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(150),
    address VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uk_customers_code UNIQUE (customer_code),
    CONSTRAINT uk_customers_email UNIQUE (email)
) ENGINE=InnoDB;

CREATE TABLE products (
    id BIGINT NOT NULL AUTO_INCREMENT,
    sku VARCHAR(30) NOT NULL,
    product_name VARCHAR(150) NOT NULL,
    category VARCHAR(100),
    unit_price DECIMAL(12,2) NOT NULL DEFAULT 0,
    stock_qty INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uk_products_sku UNIQUE (sku),
    CONSTRAINT ck_products_price CHECK (unit_price >= 0),
    CONSTRAINT ck_products_stock CHECK (stock_qty >= 0)
) ENGINE=InnoDB;

CREATE TABLE orders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_no VARCHAR(20) NOT NULL,
    order_date DATE NOT NULL,
    customer_id BIGINT NOT NULL,
    total_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    note VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT uk_orders_order_no UNIQUE (order_no),
    CONSTRAINT ck_orders_status CHECK (status IN ('NEW','CONFIRMED','COMPLETED','CANCELLED')),
    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id) REFERENCES customers(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE order_details (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    line_total DECIMAL(14,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT ck_order_details_qty CHECK (quantity > 0),
    CONSTRAINT ck_order_details_unit_price CHECK (unit_price >= 0),
    CONSTRAINT ck_order_details_line_total CHECK (line_total >= 0),
    CONSTRAINT fk_order_details_order
        FOREIGN KEY (order_id) REFERENCES orders(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_order_details_product
        FOREIGN KEY (product_id) REFERENCES products(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX idx_customers_name ON customers(full_name);
CREATE INDEX idx_products_name ON products(product_name);
CREATE INDEX idx_orders_date ON orders(order_date);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_order_details_order ON order_details(order_id);
CREATE INDEX idx_order_details_product ON order_details(product_id);
2.3. database/03_insert_sample_data.sql
USE sales_mis;

INSERT INTO customers(customer_code, full_name, phone, email, address, active) VALUES
('C001', 'Nguyen Van A', '0901000001', 'a.customer@example.com', 'Ho Chi Minh City', TRUE),
('C002', 'Tran Thi B', '0901000002', 'b.customer@example.com', 'Da Nang', TRUE),
('C003', 'Le Van C', '0901000003', 'c.customer@example.com', 'Ha Noi', TRUE),
('C004', 'Pham Thi D', '0901000004', 'd.customer@example.com', 'Can Tho', FALSE);

INSERT INTO products(sku, product_name, category, unit_price, stock_qty, active) VALUES
('P001', 'Laptop Dell Inspiron', 'Laptop', 15000000, 12, TRUE),
('P002', 'Wireless Mouse Logitech', 'Accessory', 350000, 50, TRUE),
('P003', 'Mechanical Keyboard', 'Accessory', 1200000, 25, TRUE),
('P004', '27-inch Monitor', 'Monitor', 4200000, 18, TRUE),
('P005', 'USB-C Hub', 'Accessory', 650000, 40, TRUE),
('P006', 'Office Chair', 'Furniture', 2800000, 10, TRUE);

INSERT INTO orders(order_no, order_date, customer_id, total_amount, status, note) VALUES
('SO001', '2026-03-01', 1, 15700000, 'CONFIRMED', 'First order'),
('SO002', '2026-03-05', 2, 5400000, 'COMPLETED', 'Office setup'),
('SO003', '2026-03-08', 1, 1850000, 'NEW', 'Accessories order');

INSERT INTO order_details(order_id, product_id, quantity, unit_price, line_total) VALUES
(1, 1, 1, 15000000, 15000000),
(1, 2, 2, 350000, 700000),
(2, 4, 1, 4200000, 4200000),
(2, 5, 1, 650000, 650000),
(2, 2, 1, 350000, 350000),
(2, 3, 1, 1200000, 1200000),
(3, 2, 1, 350000, 350000),
(3, 3, 1, 1200000, 1200000),
(3, 5, 1, 650000, 650000);
3. pom.xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="
           http://maven.apache.org/POM/4.0.0
           http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>sales-mis</artifactId>
    <version>1.0.0</version>

    <properties>
        <maven.compiler.release>17</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>7.3.0.Final</version>
        </dependency>

        <dependency>
            <groupId>jakarta.persistence</groupId>
            <artifactId>jakarta.persistence-api</artifactId>
            <version>3.2.0</version>
        </dependency>

        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>9.6.0</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>2.0.17</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.15.0</version>
                <configuration>
                    <release>17</release>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
4. persistence.xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             version="3.0"
             xsi:schemaLocation="
               https://jakarta.ee/xml/ns/persistence
               https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd">

    <persistence-unit name="salesPU" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

        <class>com.example.salesmis.model.entity.Customer</class>
        <class>com.example.salesmis.model.entity.Product</class>
        <class>com.example.salesmis.model.entity.SalesOrder</class>
        <class>com.example.salesmis.model.entity.OrderDetail</class>

        <properties>
            <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="jakarta.persistence.jdbc.url"
                      value="jdbc:mysql://localhost:3306/sales_mis?useSSL=false&amp;serverTimezone=Asia/Ho_Chi_Minh&amp;allowPublicKeyRetrieval=true"/>
            <property name="jakarta.persistence.jdbc.user" value="root"/>
            <property name="jakarta.persistence.jdbc.password" value="YOUR_MYSQL_PASSWORD"/>

            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect"/>
            <property name="hibernate.hbm2ddl.auto" value="validate"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
5. Utility cấu hình JPA
JpaUtil.java
package com.example.salesmis.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JpaUtil {
    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("salesPU");

    private JpaUtil() {
    }

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }

    public static void shutdown() {
        if (EMF.isOpen()) {
            EMF.close();
        }
    }
}
6. Enum và DTO
OrderStatus.java
package com.example.salesmis.model.enumtype;

public enum OrderStatus {
    NEW,
    CONFIRMED,
    COMPLETED,
    CANCELLED
}
OrderLineInput.java
package com.example.salesmis.model.dto;

import java.math.BigDecimal;

public class OrderLineInput {
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderLineInput() {
    }

    public OrderLineInput(Long productId, int quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }

    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
Các DTO báo cáo
package com.example.salesmis.model.dto;

import java.math.BigDecimal;

public record CustomerRevenueDTO(String customerCode, String customerName, BigDecimal revenue) {}
package com.example.salesmis.model.dto;

import java.math.BigDecimal;

public record ProductSalesDTO(String sku, String productName, Long totalQty, BigDecimal revenue) {}
package com.example.salesmis.model.dto;

import java.math.BigDecimal;

public record MonthlyRevenueDTO(Integer year, Integer month, BigDecimal revenue) {}
package com.example.salesmis.model.dto;

import com.example.salesmis.model.enumtype.OrderStatus;

public record StatusCountDTO(OrderStatus status, Long totalOrders) {}
7. Entity classes
Customer.java
package com.example.salesmis.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_code", nullable = false, unique = true, length = 20)
    private String customerCode;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(unique = true, length = 150)
    private String email;

    @Column(length = 255)
    private String address;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<SalesOrder> orders = new ArrayList<>();

    public Customer() {
    }

    public Customer(String customerCode, String fullName) {
        this.customerCode = customerCode;
        this.fullName = fullName;
    }

    public Long getId() { return id; }
    public String getCustomerCode() { return customerCode; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public Boolean getActive() { return active; }
    public List<SalesOrder> getOrders() { return orders; }

    public void setId(Long id) { this.id = id; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setActive(Boolean active) { this.active = active; }
    public void setOrders(List<SalesOrder> orders) { this.orders = orders; }

    @Override
    public String toString() {
        return customerCode + " - " + fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
Product.java
package com.example.salesmis.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String sku;

    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    @Column(length = 100)
    private String category;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "stock_qty", nullable = false)
    private Integer stockQty = 0;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Product() {
    }

    public Product(String sku, String productName, BigDecimal unitPrice) {
        this.sku = sku;
        this.productName = productName;
        this.unitPrice = unitPrice;
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public Integer getStockQty() { return stockQty; }
    public Boolean getActive() { return active; }
    public List<OrderDetail> getOrderDetails() { return orderDetails; }

    public void setId(Long id) { this.id = id; }
    public void setSku(String sku) { this.sku = sku; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setCategory(String category) { this.category = category; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }
    public void setActive(Boolean active) { this.active = active; }
    public void setOrderDetails(List<OrderDetail> orderDetails) { this.orderDetails = orderDetails; }

    @Override
    public String toString() {
        return sku + " - " + productName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
SalesOrder.java
package com.example.salesmis.model.entity;

import com.example.salesmis.model.enumtype.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class SalesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true, length = 20)
    private String orderNo;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.NEW;

    @Column(length = 255)
    private String note;

    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void addDetail(OrderDetail detail) {
        orderDetails.add(detail);
        detail.setSalesOrder(this);
    }

    public void clearDetails() {
        orderDetails.forEach(d -> d.setSalesOrder(null));
        orderDetails.clear();
    }

    public Long getId() { return id; }
    public String getOrderNo() { return orderNo; }
    public LocalDate getOrderDate() { return orderDate; }
    public Customer getCustomer() { return customer; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public String getNote() { return note; }
    public List<OrderDetail> getOrderDetails() { return orderDetails; }

    public void setId(Long id) { this.id = id; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setNote(String note) { this.note = note; }
    public void setOrderDetails(List<OrderDetail> orderDetails) { this.orderDetails = orderDetails; }

    @Override
    public String toString() {
        return orderNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesOrder that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
OrderDetail.java
package com.example.salesmis.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private SalesOrder salesOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal lineTotal;

    public Long getId() { return id; }
    public SalesOrder getSalesOrder() { return salesOrder; }
    public Product getProduct() { return product; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getLineTotal() { return lineTotal; }

    public void setId(Long id) { this.id = id; }
    public void setSalesOrder(SalesOrder salesOrder) { this.salesOrder = salesOrder; }
    public void setProduct(Product product) { this.product = product; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetail that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
8. DAO layer
8.1. CustomerDAO.java
package com.example.salesmis.dao;

import com.example.salesmis.model.entity.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> findAll();
    Optional<Customer> findById(Long id);
}
8.2. ProductDAO.java
package com.example.salesmis.dao;

import com.example.salesmis.model.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    List<Product> findAll();
    Optional<Product> findById(Long id);
}
8.3. SalesOrderDAO.java
package com.example.salesmis.dao;

import com.example.salesmis.model.entity.SalesOrder;

import java.util.List;
import java.util.Optional;

public interface SalesOrderDAO {
    List<SalesOrder> findAll();
    Optional<SalesOrder> findById(Long id);
    Optional<SalesOrder> findByOrderNo(String orderNo);
    List<SalesOrder> searchByKeyword(String keyword);
    SalesOrder save(SalesOrder order);
    SalesOrder update(SalesOrder order);
    void deleteById(Long id);
}
8.4. OrderDetailDAO.java
package com.example.salesmis.dao;

import com.example.salesmis.model.entity.OrderDetail;
import java.util.List;

public interface OrderDetailDAO {
    List<OrderDetail> findByOrderId(Long orderId);
}
DAO implementations
CustomerDAOImpl.java
package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.model.entity.Customer;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public List<Customer> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Customer c ORDER BY c.customerCode", Customer.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Customer.class, id));
        } finally {
            em.close();
        }
    }
}
ProductDAOImpl.java
package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.ProductDAO;
import com.example.salesmis.model.entity.Product;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {
    @Override
    public List<Product> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Product p WHERE p.active = true ORDER BY p.sku", Product.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Product.class, id));
        } finally {
            em.close();
        }
    }
}
SalesOrderDAOImpl.java
package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.SalesOrderDAO;
import com.example.salesmis.model.entity.SalesOrder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class SalesOrderDAOImpl implements SalesOrderDAO {
    @Override
    public List<SalesOrder> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT DISTINCT o
                    FROM SalesOrder o
                    LEFT JOIN FETCH o.customer
                    LEFT JOIN FETCH o.orderDetails d
                    LEFT JOIN FETCH d.product
                    ORDER BY o.id DESC
                    """, SalesOrder.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<SalesOrder> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<SalesOrder> list = em.createQuery("""
                    SELECT DISTINCT o
                    FROM SalesOrder o
                    LEFT JOIN FETCH o.customer
                    LEFT JOIN FETCH o.orderDetails d
                    LEFT JOIN FETCH d.product
                    WHERE o.id = :id
                    """, SalesOrder.class)
                    .setParameter("id", id)
                    .getResultList();
            return list.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<SalesOrder> findByOrderNo(String orderNo) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<SalesOrder> list = em.createQuery("""
                    SELECT o FROM SalesOrder o
                    WHERE LOWER(o.orderNo) = LOWER(:orderNo)
                    """, SalesOrder.class)
                    .setParameter("orderNo", orderNo)
                    .getResultList();
            return list.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> searchByKeyword(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT DISTINCT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer c
                    LEFT JOIN FETCH o.orderDetails d
                    LEFT JOIN FETCH d.product
                    WHERE LOWER(o.orderNo) LIKE LOWER(:kw)
                       OR LOWER(c.fullName) LIKE LOWER(:kw)
                    ORDER BY o.id DESC
                    """, SalesOrder.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public SalesOrder save(SalesOrder order) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(order);
            tx.commit();
            return order;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public SalesOrder update(SalesOrder order) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SalesOrder merged = em.merge(order);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SalesOrder order = em.find(SalesOrder.class, id);
            if (order != null) em.remove(order);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
OrderDetailDAOImpl.java
package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.OrderDetailDAO;
import com.example.salesmis.model.entity.OrderDetail;
import jakarta.persistence.EntityManager;

import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT d
                    FROM OrderDetail d
                    JOIN FETCH d.product
                    WHERE d.salesOrder.id = :orderId
                    ORDER BY d.id
                    """, OrderDetail.class)
                    .setParameter("orderId", orderId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
9. Service layer
9.1. LookupService.java
package com.example.salesmis.service;

import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.Product;

import java.util.List;

public interface LookupService {
    List<Customer> getAllCustomers();
    List<Product> getAllProducts();
}
LookupServiceImpl.java
package com.example.salesmis.service.impl;

import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.dao.ProductDAO;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.service.LookupService;

import java.util.List;

public class LookupServiceImpl implements LookupService {
    private final CustomerDAO customerDAO;
    private final ProductDAO productDAO;

    public LookupServiceImpl(CustomerDAO customerDAO, ProductDAO productDAO) {
        this.customerDAO = customerDAO;
        this.productDAO = productDAO;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    @Override
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }
}
9.2. OrderService.java
package com.example.salesmis.service;

import com.example.salesmis.model.dto.OrderLineInput;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.enumtype.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<SalesOrder> getAllOrders();
    List<SalesOrder> searchOrders(String keyword);
    SalesOrder getOrderById(Long id);

    SalesOrder createOrder(String orderNo,
                           LocalDate orderDate,
                           Long customerId,
                           OrderStatus status,
                           String note,
                           List<OrderLineInput> lines);

    SalesOrder updateOrder(Long id,
                           String orderNo,
                           LocalDate orderDate,
                           Long customerId,
                           OrderStatus status,
                           String note,
                           List<OrderLineInput> lines);

    void deleteOrder(Long id);
}
OrderServiceImpl.java
package com.example.salesmis.service.impl;

import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.dao.ProductDAO;
import com.example.salesmis.dao.SalesOrderDAO;
import com.example.salesmis.model.dto.OrderLineInput;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.OrderDetail;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.enumtype.OrderStatus;
import com.example.salesmis.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final SalesOrderDAO salesOrderDAO;
    private final CustomerDAO customerDAO;
    private final ProductDAO productDAO;

    public OrderServiceImpl(SalesOrderDAO salesOrderDAO, CustomerDAO customerDAO, ProductDAO productDAO) {
        this.salesOrderDAO = salesOrderDAO;
        this.customerDAO = customerDAO;
        this.productDAO = productDAO;
    }

    @Override
    public List<SalesOrder> getAllOrders() {
        return salesOrderDAO.findAll();
    }

    @Override
    public List<SalesOrder> searchOrders(String keyword) {
        if (keyword == null || keyword.isBlank()) return salesOrderDAO.findAll();
        return salesOrderDAO.searchByKeyword(keyword.trim());
    }

    @Override
    public SalesOrder getOrderById(Long id) {
        return salesOrderDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng id = " + id));
    }

    @Override
    public SalesOrder createOrder(String orderNo, LocalDate orderDate, Long customerId,
                                  OrderStatus status, String note, List<OrderLineInput> lines) {
        validate(orderNo, orderDate, customerId, lines);

        if (salesOrderDAO.findByOrderNo(orderNo.trim()).isPresent()) {
            throw new IllegalArgumentException("Mã đơn hàng đã tồn tại.");
        }

        Customer customer = customerDAO.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng."));

        SalesOrder order = new SalesOrder();
        order.setOrderNo(orderNo.trim());
        order.setOrderDate(orderDate);
        order.setCustomer(customer);
        order.setStatus(status);
        order.setNote(note);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderLineInput line : lines) {
            Product product = productDAO.findById(line.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id = " + line.getProductId()));

            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(line.getQuantity());
            detail.setUnitPrice(line.getUnitPrice());
            detail.setLineTotal(line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())));

            total = total.add(detail.getLineTotal());
            order.addDetail(detail);
        }

        order.setTotalAmount(total);
        return salesOrderDAO.save(order);
    }

    @Override
    public SalesOrder updateOrder(Long id, String orderNo, LocalDate orderDate, Long customerId,
                                  OrderStatus status, String note, List<OrderLineInput> lines) {
        validate(orderNo, orderDate, customerId, lines);

        SalesOrder existing = getOrderById(id);
        Customer customer = customerDAO.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng."));

        salesOrderDAO.findByOrderNo(orderNo.trim()).ifPresent(found -> {
            if (!found.getId().equals(id)) {
                throw new IllegalArgumentException("Mã đơn hàng đã được dùng cho đơn khác.");
            }
        });

        existing.setOrderNo(orderNo.trim());
        existing.setOrderDate(orderDate);
        existing.setCustomer(customer);
        existing.setStatus(status);
        existing.setNote(note);

        existing.clearDetails();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderLineInput line : lines) {
            Product product = productDAO.findById(line.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id = " + line.getProductId()));

            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(line.getQuantity());
            detail.setUnitPrice(line.getUnitPrice());
            detail.setLineTotal(line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())));

            total = total.add(detail.getLineTotal());
            existing.addDetail(detail);
        }

        existing.setTotalAmount(total);
        return salesOrderDAO.update(existing);
    }

    @Override
    public void deleteOrder(Long id) {
        getOrderById(id);
        salesOrderDAO.deleteById(id);
    }

    private void validate(String orderNo, LocalDate orderDate, Long customerId, List<OrderLineInput> lines) {
        if (orderNo == null || orderNo.isBlank()) throw new IllegalArgumentException("Order No không được trống.");
        if (orderDate == null) throw new IllegalArgumentException("Ngày đơn hàng không được trống.");
        if (customerId == null) throw new IllegalArgumentException("Khách hàng không được trống.");
        if (lines == null || lines.isEmpty()) throw new IllegalArgumentException("Đơn hàng phải có ít nhất 1 dòng chi tiết.");
        for (OrderLineInput line : lines) {
            if (line.getProductId() == null) throw new IllegalArgumentException("Thiếu sản phẩm.");
            if (line.getQuantity() <= 0) throw new IllegalArgumentException("Số lượng phải > 0.");
            if (line.getUnitPrice() == null || line.getUnitPrice().signum() < 0) {
                throw new IllegalArgumentException("Đơn giá không hợp lệ.");
            }
        }
    }
}
10. Controller
OrderController.java
package com.example.salesmis.controller;

import com.example.salesmis.model.dto.OrderLineInput;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.enumtype.OrderStatus;
import com.example.salesmis.service.LookupService;
import com.example.salesmis.service.OrderService;

import java.time.LocalDate;
import java.util.List;

public class OrderController {
    private final OrderService orderService;
    private final LookupService lookupService;

    public OrderController(OrderService orderService, LookupService lookupService) {
        this.orderService = orderService;
        this.lookupService = lookupService;
    }

    public List<SalesOrder> getAllOrders() {
        return orderService.getAllOrders();
    }

    public List<SalesOrder> searchOrders(String keyword) {
        return orderService.searchOrders(keyword);
    }

    public SalesOrder getOrderById(Long id) {
        return orderService.getOrderById(id);
    }

    public List<Customer> getAllCustomers() {
        return lookupService.getAllCustomers();
    }

    public List<Product> getAllProducts() {
        return lookupService.getAllProducts();
    }

    public SalesOrder createOrder(String orderNo, String orderDateText, Long customerId,
                                  String statusText, String note, List<OrderLineInput> lines) {
        return orderService.createOrder(
                orderNo,
                LocalDate.parse(orderDateText),
                customerId,
                OrderStatus.valueOf(statusText),
                note,
                lines
        );
    }

    public SalesOrder updateOrder(Long id, String orderNo, String orderDateText, Long customerId,
                                  String statusText, String note, List<OrderLineInput> lines) {
        return orderService.updateOrder(
                id,
                orderNo,
                LocalDate.parse(orderDateText),
                customerId,
                OrderStatus.valueOf(statusText),
                note,
                lines
        );
    }

    public void deleteOrder(Long id) {
        orderService.deleteOrder(id);
    }
}
11. 15 câu truy vấn JPQL
Mình thiết kế riêng một DAO báo cáo để phần bài giảng rõ ràng.
ReportDAO.java
package com.example.salesmis.dao;

import com.example.salesmis.model.dto.CustomerRevenueDTO;
import com.example.salesmis.model.dto.MonthlyRevenueDTO;
import com.example.salesmis.model.dto.ProductSalesDTO;
import com.example.salesmis.model.dto.StatusCountDTO;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.enumtype.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReportDAO {
    List<SalesOrder> q01_findAllOrdersWithCustomer();
    SalesOrder q02_findOrderByOrderNo(String orderNo);
    List<SalesOrder> q03_findOrdersByCustomerKeyword(String keyword);
    List<SalesOrder> q04_findOrdersBetween(LocalDate from, LocalDate to);
    List<SalesOrder> q05_findOrdersByStatus(OrderStatus status);
    List<Product> q06_findLowStockProducts(int threshold);
    List<ProductSalesDTO> q07_findTopSellingProducts();
    List<CustomerRevenueDTO> q08_findRevenueByCustomer();
    List<MonthlyRevenueDTO> q09_findRevenueByMonth();
    List<StatusCountDTO> q10_countOrdersByStatus();
    List<Customer> q11_findCustomersWithoutOrders();
    List<SalesOrder> q12_findOrdersContainingProduct(Long productId);
    BigDecimal q13_findAverageOrderValue();
    BigDecimal q14_findMaxOrderValue();
    BigDecimal q15_findMinOrderValue();
}
ReportDAOImpl.java
package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.ReportDAO;
import com.example.salesmis.model.dto.CustomerRevenueDTO;
import com.example.salesmis.model.dto.MonthlyRevenueDTO;
import com.example.salesmis.model.dto.ProductSalesDTO;
import com.example.salesmis.model.dto.StatusCountDTO;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.enumtype.OrderStatus;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public List<SalesOrder> q01_findAllOrdersWithCustomer() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer
                    ORDER BY o.orderDate DESC, o.id DESC
                    """, SalesOrder.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public SalesOrder q02_findOrderByOrderNo(String orderNo) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer
                    WHERE o.orderNo = :orderNo
                    """, SalesOrder.class)
                    .setParameter("orderNo", orderNo)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> q03_findOrdersByCustomerKeyword(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer c
                    WHERE LOWER(c.fullName) LIKE LOWER(:kw)
                    ORDER BY o.orderDate DESC
                    """, SalesOrder.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> q04_findOrdersBetween(LocalDate from, LocalDate to) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer
                    WHERE o.orderDate BETWEEN :fromDate AND :toDate
                    ORDER BY o.orderDate
                    """, SalesOrder.class)
                    .setParameter("fromDate", from)
                    .setParameter("toDate", to)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> q05_findOrdersByStatus(OrderStatus status) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer
                    WHERE o.status = :status
                    ORDER BY o.orderDate DESC
                    """, SalesOrder.class)
                    .setParameter("status", status)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> q06_findLowStockProducts(int threshold) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT p
                    FROM Product p
                    WHERE p.stockQty <= :threshold
                    ORDER BY p.stockQty ASC, p.productName ASC
                    """, Product.class)
                    .setParameter("threshold", threshold)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<ProductSalesDTO> q07_findTopSellingProducts() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT new com.example.salesmis.model.dto.ProductSalesDTO(
                        p.sku,
                        p.productName,
                        SUM(d.quantity),
                        SUM(d.lineTotal)
                    )
                    FROM OrderDetail d
                    JOIN d.product p
                    GROUP BY p.sku, p.productName
                    ORDER BY SUM(d.quantity) DESC, SUM(d.lineTotal) DESC
                    """, ProductSalesDTO.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<CustomerRevenueDTO> q08_findRevenueByCustomer() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT new com.example.salesmis.model.dto.CustomerRevenueDTO(
                        c.customerCode,
                        c.fullName,
                        COALESCE(SUM(o.totalAmount), 0)
                    )
                    FROM Customer c
                    LEFT JOIN c.orders o
                    GROUP BY c.customerCode, c.fullName
                    ORDER BY COALESCE(SUM(o.totalAmount), 0) DESC
                    """, CustomerRevenueDTO.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<MonthlyRevenueDTO> q09_findRevenueByMonth() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT new com.example.salesmis.model.dto.MonthlyRevenueDTO(
                        year(o.orderDate),
                        month(o.orderDate),
                        SUM(o.totalAmount)
                    )
                    FROM SalesOrder o
                    GROUP BY year(o.orderDate), month(o.orderDate)
                    ORDER BY year(o.orderDate), month(o.orderDate)
                    """, MonthlyRevenueDTO.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<StatusCountDTO> q10_countOrdersByStatus() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT new com.example.salesmis.model.dto.StatusCountDTO(
                        o.status,
                        COUNT(o)
                    )
                    FROM SalesOrder o
                    GROUP BY o.status
                    ORDER BY o.status
                    """, StatusCountDTO.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Customer> q11_findCustomersWithoutOrders() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT c
                    FROM Customer c
                    WHERE c.orders IS EMPTY
                    ORDER BY c.customerCode
                    """, Customer.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> q12_findOrdersContainingProduct(Long productId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT DISTINCT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer
                    JOIN o.orderDetails d
                    WHERE d.product.id = :productId
                    ORDER BY o.orderDate DESC
                    """, SalesOrder.class)
                    .setParameter("productId", productId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public BigDecimal q13_findAverageOrderValue() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Double avg = em.createQuery("""
                    SELECT AVG(o.totalAmount)
                    FROM SalesOrder o
                    """, Double.class).getSingleResult();
            return avg == null ? BigDecimal.ZERO : BigDecimal.valueOf(avg);
        } finally {
            em.close();
        }
    }

    @Override
    public BigDecimal q14_findMaxOrderValue() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            BigDecimal max = em.createQuery("""
                    SELECT MAX(o.totalAmount)
                    FROM SalesOrder o
                    """, BigDecimal.class).getSingleResult();
            return max == null ? BigDecimal.ZERO : max;
        } finally {
            em.close();
        }
    }

    @Override
    public BigDecimal q15_findMinOrderValue() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            BigDecimal min = em.createQuery("""
                    SELECT MIN(o.totalAmount)
                    FROM SalesOrder o
                    """, BigDecimal.class).getSingleResult();
            return min == null ? BigDecimal.ZERO : min;
        } finally {
            em.close();
        }
    }
}
12. View – CRUD đơn hàng và chi tiết đơn hàng
Đây là phần trọng tâm bài giảng. Thiết kế một panel chính quản lý SalesOrder và OrderDetail.
MainFrame.java
package com.example.salesmis.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(OrderManagementPanel panel) {
        setTitle("MIS - Sales Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 760);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }
}
OrderManagementPanel.java
package com.example.salesmis.view;

import com.example.salesmis.controller.OrderController;
import com.example.salesmis.model.dto.OrderLineInput;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.enumtype.OrderStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderManagementPanel extends JPanel {
    private final OrderController orderController;

    private JTextField txtOrderId;
    private JTextField txtOrderNo;
    private JTextField txtOrderDate;
    private JTextField txtSearch;
    private JTextField txtNote;

    private JComboBox<Customer> cboCustomer;
    private JComboBox<String> cboStatus;

    private JComboBox<Product> cboProduct;
    private JTextField txtQty;
    private JTextField txtUnitPrice;

    private JTable tblOrders;
    private JTable tblDetails;
    private DefaultTableModel orderTableModel;
    private DefaultTableModel detailTableModel;

    private Long selectedOrderId;

    public OrderManagementPanel(OrderController orderController) {
        this.orderController = orderController;
        initComponents();
        loadCustomers();
        loadProducts();
        loadOrders();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.add(buildHeaderForm(), BorderLayout.NORTH);
        top.add(buildDetailEntryPanel(), BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildOrderTablePanel(), buildDetailTablePanel());
        splitPane.setResizeWeight(0.55);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel buildHeaderForm() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(3, 4, 8, 8));

        txtOrderId = new JTextField();
        txtOrderId.setEditable(false);

        txtOrderNo = new JTextField();
        txtOrderDate = new JTextField("2026-03-24");
        txtSearch = new JTextField();
        txtNote = new JTextField();

        cboCustomer = new JComboBox<>();
        cboStatus = new JComboBox<>(new String[]{
                OrderStatus.NEW.name(),
                OrderStatus.CONFIRMED.name(),
                OrderStatus.COMPLETED.name(),
                OrderStatus.CANCELLED.name()
        });

        form.add(new JLabel("Order ID"));
        form.add(txtOrderId);
        form.add(new JLabel("Order No"));
        form.add(txtOrderNo);

        form.add(new JLabel("Order Date (yyyy-MM-dd)"));
        form.add(txtOrderDate);
        form.add(new JLabel("Customer"));
        form.add(cboCustomer);

        form.add(new JLabel("Status"));
        form.add(cboStatus);
        form.add(new JLabel("Note"));
        form.add(txtNote);

        panel.add(form, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNew = new JButton("New");
        JButton btnSave = new JButton("Save");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnSearch = new JButton("Search");

        buttons.add(btnNew);
        buttons.add(btnSave);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(new JLabel("Keyword"));
        buttons.add(txtSearch);
        buttons.add(btnSearch);

        btnNew.addActionListener(e -> clearForm());
        btnSave.addActionListener(e -> saveOrder());
        btnUpdate.addActionListener(e -> updateOrder());
        btnDelete.addActionListener(e -> deleteOrder());
        btnSearch.addActionListener(e -> searchOrders());

        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildDetailEntryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        cboProduct = new JComboBox<>();
        txtQty = new JTextField(6);
        txtUnitPrice = new JTextField(10);
        txtUnitPrice.setEditable(false);

        JButton btnAddLine = new JButton("Add Line");
        JButton btnRemoveLine = new JButton("Remove Selected Line");

        cboProduct.addActionListener(e -> {
            Product p = (Product) cboProduct.getSelectedItem();
            if (p != null) {
                txtUnitPrice.setText(p.getUnitPrice().toPlainString());
            }
        });

        btnAddLine.addActionListener(e -> addLine());
        btnRemoveLine.addActionListener(e -> removeSelectedLine());

        panel.add(new JLabel("Product"));
        panel.add(cboProduct);
        panel.add(new JLabel("Qty"));
        panel.add(txtQty);
        panel.add(new JLabel("Unit Price"));
        panel.add(txtUnitPrice);
        panel.add(btnAddLine);
        panel.add(btnRemoveLine);

        return panel;
    }

    private JScrollPane buildOrderTablePanel() {
        orderTableModel = new DefaultTableModel(
                new String[]{"ID", "Order No", "Date", "Customer", "Status", "Total", "Note"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tblOrders = new JTable(orderTableModel);
        tblOrders.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblOrders.getSelectedRow() != -1) {
                loadOrderToForm();
            }
        });

        return new JScrollPane(tblOrders);
    }

    private JScrollPane buildDetailTablePanel() {
        detailTableModel = new DefaultTableModel(
                new String[]{"Product ID", "SKU", "Product", "Qty", "Unit Price", "Line Total"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tblDetails = new JTable(detailTableModel);
        return new JScrollPane(tblDetails);
    }

    private void loadCustomers() {
        cboCustomer.removeAllItems();
        for (Customer c : orderController.getAllCustomers()) {
            cboCustomer.addItem(c);
        }
    }

    private void loadProducts() {
        cboProduct.removeAllItems();
        for (Product p : orderController.getAllProducts()) {
            cboProduct.addItem(p);
        }
        if (cboProduct.getItemCount() > 0) {
            Product p = (Product) cboProduct.getItemAt(0);
            txtUnitPrice.setText(p.getUnitPrice().toPlainString());
        }
    }

    private void loadOrders() {
        renderOrders(orderController.getAllOrders());
    }

    private void renderOrders(List<SalesOrder> orders) {
        orderTableModel.setRowCount(0);
        for (SalesOrder o : orders) {
            orderTableModel.addRow(new Object[]{
                    o.getId(),
                    o.getOrderNo(),
                    o.getOrderDate(),
                    o.getCustomer() != null ? o.getCustomer().getFullName() : "",
                    o.getStatus(),
                    o.getTotalAmount(),
                    o.getNote()
            });
        }
    }

    private void addLine() {
        try {
            Product p = (Product) cboProduct.getSelectedItem();
            if (p == null) throw new IllegalArgumentException("Chưa chọn sản phẩm.");

            int qty = Integer.parseInt(txtQty.getText().trim());
            if (qty <= 0) throw new IllegalArgumentException("Số lượng phải > 0.");

            BigDecimal price = new BigDecimal(txtUnitPrice.getText().trim());
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(qty));

            detailTableModel.addRow(new Object[]{
                    p.getId(),
                    p.getSku(),
                    p.getProductName(),
                    qty,
                    price,
                    lineTotal
            });

            txtQty.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeSelectedLine() {
        int row = tblDetails.getSelectedRow();
        if (row != -1) detailTableModel.removeRow(row);
    }

    private List<OrderLineInput> buildLines() {
        List<OrderLineInput> lines = new ArrayList<>();
        for (int i = 0; i < detailTableModel.getRowCount(); i++) {
            Long productId = Long.valueOf(detailTableModel.getValueAt(i, 0).toString());
            int qty = Integer.parseInt(detailTableModel.getValueAt(i, 3).toString());
            BigDecimal price = new BigDecimal(detailTableModel.getValueAt(i, 4).toString());
            lines.add(new OrderLineInput(productId, qty, price));
        }
        return lines;
    }

    private void saveOrder() {
        try {
            Customer customer = (Customer) cboCustomer.getSelectedItem();
            if (customer == null) throw new IllegalArgumentException("Chưa chọn khách hàng.");

            SalesOrder saved = orderController.createOrder(
                    txtOrderNo.getText(),
                    txtOrderDate.getText(),
                    customer.getId(),
                    cboStatus.getSelectedItem().toString(),
                    txtNote.getText(),
                    buildLines()
            );

            JOptionPane.showMessageDialog(this, "Lưu đơn hàng thành công: " + saved.getOrderNo());
            clearForm();
            loadOrders();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi lưu đơn hàng", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateOrder() {
        try {
            if (selectedOrderId == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng cần cập nhật.");
                return;
            }

            Customer customer = (Customer) cboCustomer.getSelectedItem();
            SalesOrder updated = orderController.updateOrder(
                    selectedOrderId,
                    txtOrderNo.getText(),
                    txtOrderDate.getText(),
                    customer.getId(),
                    cboStatus.getSelectedItem().toString(),
                    txtNote.getText(),
                    buildLines()
            );

            JOptionPane.showMessageDialog(this, "Cập nhật thành công: " + updated.getOrderNo());
            clearForm();
            loadOrders();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi cập nhật", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteOrder() {
        try {
            if (selectedOrderId == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng cần xóa.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Xóa đơn hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                orderController.deleteOrder(selectedOrderId);
                clearForm();
                loadOrders();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi xóa", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchOrders() {
        try {
            renderOrders(orderController.searchOrders(txtSearch.getText()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi tìm kiếm", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadOrderToForm() {
        int row = tblOrders.getSelectedRow();
        if (row == -1) return;

        selectedOrderId = Long.valueOf(tblOrders.getValueAt(row, 0).toString());
        SalesOrder order = orderController.getOrderById(selectedOrderId);

        txtOrderId.setText(order.getId().toString());
        txtOrderNo.setText(order.getOrderNo());
        txtOrderDate.setText(order.getOrderDate().toString());
        txtNote.setText(order.getNote() == null ? "" : order.getNote());
        cboStatus.setSelectedItem(order.getStatus().name());

        for (int i = 0; i < cboCustomer.getItemCount(); i++) {
            Customer c = cboCustomer.getItemAt(i);
            if (c.getId().equals(order.getCustomer().getId())) {
                cboCustomer.setSelectedIndex(i);
                break;
            }
        }

        detailTableModel.setRowCount(0);
        order.getOrderDetails().forEach(d -> detailTableModel.addRow(new Object[]{
                d.getProduct().getId(),
                d.getProduct().getSku(),
                d.getProduct().getProductName(),
                d.getQuantity(),
                d.getUnitPrice(),
                d.getLineTotal()
        }));
    }

    private void clearForm() {
        selectedOrderId = null;
        txtOrderId.setText("");
        txtOrderNo.setText("");
        txtOrderDate.setText("2026-03-24");
        txtNote.setText("");
        txtSearch.setText("");
        detailTableModel.setRowCount(0);
        tblOrders.clearSelection();
    }
}
13. Launcher chạy project
AppLauncher.java
package com.example.salesmis;

import com.example.salesmis.controller.OrderController;
import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.dao.ProductDAO;
import com.example.salesmis.dao.SalesOrderDAO;
import com.example.salesmis.dao.impl.CustomerDAOImpl;
import com.example.salesmis.dao.impl.ProductDAOImpl;
import com.example.salesmis.dao.impl.SalesOrderDAOImpl;
import com.example.salesmis.service.LookupService;
import com.example.salesmis.service.OrderService;
import com.example.salesmis.service.impl.LookupServiceImpl;
import com.example.salesmis.service.impl.OrderServiceImpl;
import com.example.salesmis.view.MainFrame;
import com.example.salesmis.view.OrderManagementPanel;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        CustomerDAO customerDAO = new CustomerDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        SalesOrderDAO salesOrderDAO = new SalesOrderDAOImpl();

        LookupService lookupService = new LookupServiceImpl(customerDAO, productDAO);
        OrderService orderService = new OrderServiceImpl(salesOrderDAO, customerDAO, productDAO);
        OrderController orderController = new OrderController(orderService, lookupService);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(new OrderManagementPanel(orderController));
            frame.setVisible(true);
        });
    }
}
14. Phần các câu lệnh truy vấn 
Các nhóm truy vấn như sau:
Nhóm A – truy vấn tra cứu nghiệp vụ
1.	Tất cả đơn hàng kèm khách hàng
2.	Tìm đơn theo mã đơn
3.	Tìm đơn theo tên khách hàng
4.	Tìm đơn theo khoảng ngày
5.	Tìm đơn theo trạng thái
Nhóm B – truy vấn sản phẩm và tồn kho
6.	Sản phẩm sắp hết hàng
7.	Sản phẩm bán chạy nhất
8.	Đơn hàng chứa một sản phẩm cụ thể
Nhóm C – truy vấn doanh thu
8.	Doanh thu theo khách hàng
9.	Doanh thu theo tháng
10.	Giá trị đơn trung bình
11.	Giá trị đơn lớn nhất
12.	Giá trị đơn nhỏ nhất
Nhóm D – truy vấn quản trị
10.	Đếm số đơn theo trạng thái
11.	Khách hàng chưa từng mua hàng
15. Điểm mạnh của thiết kế này cho đồ án môn học
Bộ đồ án này có ưu điểm:
•	đúng mô hình MVC + DAO + Service
•	dùng Hibernate/JPA thay cho JDBC thuần
•	có 4 bảng kinh điển của hệ thống bán hàng
•	có CRUD đúng trọng tâm trên Order và OrderDetail
•	có 15 truy vấn JPQL rất phù hợp để dạy:
o	SELECT
o	JOIN
o	GROUP BY
o	SUM / AVG / MAX / MIN
o	constructor expression
o	IS EMPTY
16. Gợi ý phân công nhóm 2–3 sinh viên
Sinh viên 1
•	database
•	entity
•	DAO
Sinh viên 2
•	service
•	controller
•	các truy vấn JPQL
Sinh viên 3
•	Java Swing view
•	CRUD đơn hàng / chi tiết đơn hàng
•	demo + báo cáo + slide
17. Cách chạy trong IntelliJ
1.	Tạo database bằng 3 file SQL
2.	Sửa persistence.xml với user/password MySQL thật
3.	Mở project Maven trong IntelliJ
4.	Wait Maven sync
5.	Chạy AppLauncher.java
18. Gợi ý mở rộng 
Có thể mở rộng tiếp:
•	thêm LoginFrame
•	phân quyền ADMIN / STAFF
•	thêm CustomerManagementPanel
•	thêm ProductManagementPanel
•	xuất hóa đơn PDF
•	biểu đồ doanh thu theo tháng
•	kiểm soát tồn kho khi đơn hàng chuyển sang COMPLETED

