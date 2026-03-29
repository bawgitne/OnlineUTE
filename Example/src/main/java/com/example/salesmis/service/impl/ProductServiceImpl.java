package com.example.salesmis.service.impl;

import com.example.salesmis.dao.ProductDAO;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productDAO.findAll();
        }
        return productDAO.searchByKeyword(keyword.trim());
    }

    @Override
    public Product getProductById(Long id) {
        return productDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với id = " + id));
    }

    @Override
    public Product createProduct(String sku,
                                 String productName,
                                 String category,
                                 BigDecimal unitPrice,
                                 Integer stockQty,
                                 Boolean active) {

        validate(sku, productName, category, unitPrice, stockQty);

        if (productDAO.existsBySku(sku.trim())) {
            throw new IllegalArgumentException("SKU đã tồn tại: " + sku);
        }

        Product product = new Product();
        product.setSku(sku.trim());
        product.setProductName(productName.trim());
        product.setCategory(normalize(category));
        product.setUnitPrice(unitPrice);
        product.setStockQty(stockQty);
        product.setActive(active != null ? active : Boolean.TRUE);

        return productDAO.save(product);
    }

    @Override
    public Product updateProduct(Long id,
                                 String sku,
                                 String productName,
                                 String category,
                                 BigDecimal unitPrice,
                                 Integer stockQty,
                                 Boolean active) {

        validate(sku, productName, category, unitPrice, stockQty);

        Product existing = productDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với id = " + id));

        Product sameSku = productDAO.findBySku(sku.trim()).orElse(null);
        if (sameSku != null && !sameSku.getId().equals(id)) {
            throw new IllegalArgumentException("SKU đã được dùng bởi sản phẩm khác.");
        }

        existing.setSku(sku.trim());
        existing.setProductName(productName.trim());
        existing.setCategory(normalize(category));
        existing.setUnitPrice(unitPrice);
        existing.setStockQty(stockQty);
        existing.setActive(active != null ? active : Boolean.TRUE);

        return productDAO.update(existing);
    }

    @Override
    public void deleteProduct(Long id) {
        getProductById(id);
        productDAO.deleteById(id);
    }

    private void validate(String sku,
                          String productName,
                          String category,
                          BigDecimal unitPrice,
                          Integer stockQty) {

        if (sku == null || sku.trim().isEmpty()) {
            throw new IllegalArgumentException("SKU không được để trống.");
        }

        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }

        if (sku.trim().length() > 30) {
            throw new IllegalArgumentException("SKU tối đa 30 ký tự.");
        }

        if (productName.trim().length() > 150) {
            throw new IllegalArgumentException("Tên sản phẩm tối đa 150 ký tự.");
        }

        if (category != null && category.trim().length() > 100) {
            throw new IllegalArgumentException("Danh mục tối đa 100 ký tự.");
        }

        if (unitPrice == null || unitPrice.signum() < 0) {
            throw new IllegalArgumentException("Đơn giá phải >= 0.");
        }

        if (stockQty == null || stockQty < 0) {
            throw new IllegalArgumentException("Tồn kho phải >= 0.");
        }
    }

    private String normalize(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }
}
