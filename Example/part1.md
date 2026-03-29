Trọn bộ Product CRUD để đồng bộ với:
•	Customer CRUD
•	Order CRUD
theo đúng kiến trúc của dự án:
•	DAO
•	Service
•	Controller
•	Swing View
và có thể ghép thành 3 tab trong MainFrame.
1. Mở rộng ProductDAO.java
Thay bằng bản đầy đủ này.
package com.example.salesmis.dao;

import com.example.salesmis.model.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    List<Product> findAll();

    Optional<Product> findById(Long id);

    Optional<Product> findBySku(String sku);

    List<Product> searchByKeyword(String keyword);

    Product save(Product product);

    Product update(Product product);

    void deleteById(Long id);

    boolean existsBySku(String sku);
}
2. ProductDAOImpl.java
package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.ProductDAO;
import com.example.salesmis.model.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public List<Product> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT p
                    FROM Product p
                    ORDER BY p.id
                    """, Product.class).getResultList();
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

    @Override
    public Optional<Product> findBySku(String sku) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Product> list = em.createQuery("""
                    SELECT p
                    FROM Product p
                    WHERE LOWER(p.sku) = LOWER(:sku)
                    """, Product.class)
                    .setParameter("sku", sku)
                    .getResultList();

            return list.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> searchByKeyword(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT p
                    FROM Product p
                    WHERE LOWER(p.sku) LIKE LOWER(:kw)
                       OR LOWER(p.productName) LIKE LOWER(:kw)
                       OR LOWER(COALESCE(p.category, '')) LIKE LOWER(:kw)
                    ORDER BY p.productName
                    """, Product.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Product save(Product product) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(product);
            tx.commit();
            return product;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Product update(Product product) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Product merged = em.merge(product);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
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
            Product product = em.find(Product.class, id);
            if (product != null) {
                em.remove(product);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsBySku(String sku) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("""
                    SELECT COUNT(p)
                    FROM Product p
                    WHERE LOWER(p.sku) = LOWER(:sku)
                    """, Long.class)
                    .setParameter("sku", sku)
                    .getSingleResult();

            return count != null && count > 0;
        } finally {
            em.close();
        }
    }
}
3. ProductService.java
package com.example.salesmis.service;

import com.example.salesmis.model.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    List<Product> searchProducts(String keyword);

    Product getProductById(Long id);

    Product createProduct(String sku,
                          String productName,
                          String category,
                          BigDecimal unitPrice,
                          Integer stockQty,
                          Boolean active);

    Product updateProduct(Long id,
                          String sku,
                          String productName,
                          String category,
                          BigDecimal unitPrice,
                          Integer stockQty,
                          Boolean active);

    void deleteProduct(Long id);
}
4. ProductServiceImpl.java
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
5. ProductController.java
package com.example.salesmis.controller;

import com.example.salesmis.model.entity.Product;
import com.example.salesmis.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public List<Product> searchProducts(String keyword) {
        return productService.searchProducts(keyword);
    }

    public Product getProductById(Long id) {
        return productService.getProductById(id);
    }

    public Product createProduct(String sku,
                                 String productName,
                                 String category,
                                 String unitPriceText,
                                 String stockQtyText,
                                 boolean active) {

        BigDecimal unitPrice = parseBigDecimal(unitPriceText, "Đơn giá");
        Integer stockQty = parseInteger(stockQtyText, "Tồn kho");

        return productService.createProduct(
                sku,
                productName,
                category,
                unitPrice,
                stockQty,
                active
        );
    }

    public Product updateProduct(Long id,
                                 String sku,
                                 String productName,
                                 String category,
                                 String unitPriceText,
                                 String stockQtyText,
                                 boolean active) {

        BigDecimal unitPrice = parseBigDecimal(unitPriceText, "Đơn giá");
        Integer stockQty = parseInteger(stockQtyText, "Tồn kho");

        return productService.updateProduct(
                id,
                sku,
                productName,
                category,
                unitPrice,
                stockQty,
                active
        );
    }

    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }

    private Integer parseInteger(String value, String fieldName) {
        try {
            return Integer.valueOf(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(fieldName + " phải là số nguyên hợp lệ.");
        }
    }

    private BigDecimal parseBigDecimal(String value, String fieldName) {
        try {
            return new BigDecimal(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(fieldName + " phải là số hợp lệ.");
        }
    }
}
6. ProductManagementPanel.java
package com.example.salesmis.view;

import com.example.salesmis.controller.ProductController;
import com.example.salesmis.model.entity.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductManagementPanel extends JPanel {

    private final ProductController productController;

    private JTextField txtId;
    private JTextField txtSku;
    private JTextField txtProductName;
    private JTextField txtCategory;
    private JTextField txtUnitPrice;
    private JTextField txtStockQty;
    private JTextField txtSearch;

    private JCheckBox chkActive;

    private JTable tblProducts;
    private DefaultTableModel productTableModel;

    private Long selectedProductId;

    public ProductManagementPanel(ProductController productController) {
        this.productController = productController;
        initComponents();
        loadProductTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel lblHeader = new JLabel("QUẢN LÝ SẢN PHẨM");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblHeader, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        add(centerPanel, BorderLayout.CENTER);

        centerPanel.add(buildFormPanel(), BorderLayout.NORTH);
        centerPanel.add(buildTablePanel(), BorderLayout.CENTER);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(4, 4, 8, 8));

        txtId = new JTextField();
        txtId.setEditable(false);

        txtSku = new JTextField();
        txtProductName = new JTextField();
        txtCategory = new JTextField();
        txtUnitPrice = new JTextField();
        txtStockQty = new JTextField();
        txtSearch = new JTextField();

        chkActive = new JCheckBox("Active");
        chkActive.setSelected(true);

        form.add(new JLabel("ID"));
        form.add(txtId);
        form.add(new JLabel("SKU"));
        form.add(txtSku);

        form.add(new JLabel("Tên sản phẩm"));
        form.add(txtProductName);
        form.add(new JLabel("Danh mục"));
        form.add(txtCategory);

        form.add(new JLabel("Đơn giá"));
        form.add(txtUnitPrice);
        form.add(new JLabel("Tồn kho"));
        form.add(txtStockQty);

        form.add(new JLabel("Trạng thái"));
        form.add(chkActive);
        form.add(new JLabel("Tìm kiếm"));
        form.add(txtSearch);

        panel.add(form, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnSearch = new JButton("Tìm");
        JButton btnRefresh = new JButton("Làm mới");

        btnAdd.addActionListener(e -> createProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnSearch.addActionListener(e -> searchProducts());
        btnRefresh.addActionListener(e -> {
            clearForm();
            loadProductTable();
        });

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnRefresh);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JScrollPane buildTablePanel() {
        String[] columns = {
                "ID", "SKU", "Tên sản phẩm", "Danh mục", "Đơn giá", "Tồn kho", "Active"
        };

        productTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblProducts = new JTable(productTableModel);
        tblProducts.setRowHeight(24);

        tblProducts.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblProducts.getSelectedRow() != -1) {
                fillFormFromSelectedRow();
            }
        });

        return new JScrollPane(tblProducts);
    }

    private void loadProductTable() {
        List<Product> products = productController.getAllProducts();
        renderProductTable(products);
    }

    private void renderProductTable(List<Product> products) {
        productTableModel.setRowCount(0);

        for (Product p : products) {
            productTableModel.addRow(new Object[]{
                    p.getId(),
                    p.getSku(),
                    p.getProductName(),
                    p.getCategory(),
                    p.getUnitPrice(),
                    p.getStockQty(),
                    p.getActive()
            });
        }
    }

    private void createProduct() {
        try {
            Product product = productController.createProduct(
                    txtSku.getText(),
                    txtProductName.getText(),
                    txtCategory.getText(),
                    txtUnitPrice.getText(),
                    txtStockQty.getText(),
                    chkActive.isSelected()
            );

            JOptionPane.showMessageDialog(this,
                    "Thêm sản phẩm thành công: " + product.getProductName());

            clearForm();
            loadProductTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Lỗi thêm sản phẩm",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct() {
        try {
            if (selectedProductId == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần cập nhật.");
                return;
            }

            Product product = productController.updateProduct(
                    selectedProductId,
                    txtSku.getText(),
                    txtProductName.getText(),
                    txtCategory.getText(),
                    txtUnitPrice.getText(),
                    txtStockQty.getText(),
                    chkActive.isSelected()
            );

            JOptionPane.showMessageDialog(this,
                    "Cập nhật thành công: " + product.getProductName());

            clearForm();
            loadProductTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Lỗi cập nhật",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        try {
            if (selectedProductId == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xóa sản phẩm này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                productController.deleteProduct(selectedProductId);
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công.");
                clearForm();
                loadProductTable();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Lỗi xóa",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchProducts() {
        try {
            List<Product> products = productController.searchProducts(txtSearch.getText());
            renderProductTable(products);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Lỗi tìm kiếm",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillFormFromSelectedRow() {
        int row = tblProducts.getSelectedRow();
        if (row == -1) {
            return;
        }

        selectedProductId = Long.valueOf(tblProducts.getValueAt(row, 0).toString());

        txtId.setText(String.valueOf(tblProducts.getValueAt(row, 0)));
        txtSku.setText(String.valueOf(tblProducts.getValueAt(row, 1)));
        txtProductName.setText(String.valueOf(tblProducts.getValueAt(row, 2)));
        txtCategory.setText(tblProducts.getValueAt(row, 3) != null ? tblProducts.getValueAt(row, 3).toString() : "");
        txtUnitPrice.setText(String.valueOf(tblProducts.getValueAt(row, 4)));
        txtStockQty.setText(String.valueOf(tblProducts.getValueAt(row, 5)));
        chkActive.setSelected(Boolean.parseBoolean(String.valueOf(tblProducts.getValueAt(row, 6))));
    }

    private void clearForm() {
        selectedProductId = null;
        txtId.setText("");
        txtSku.setText("");
        txtProductName.setText("");
        txtCategory.setText("");
        txtUnitPrice.setText("");
        txtStockQty.setText("");
        txtSearch.setText("");
        chkActive.setSelected(true);
        tblProducts.clearSelection();
    }
}
7. MainFrame.java bản ghép 3 module
Giờ có thể ghép thành 3 tab:
•	Customer CRUD
•	Product CRUD
•	Order CRUD
package com.example.salesmis.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(CustomerManagementPanel customerPanel,
                     ProductManagementPanel productPanel,
                     OrderManagementPanel orderPanel) {

        setTitle("MIS - Sales Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 780);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Customer CRUD", customerPanel);
        tabs.addTab("Product CRUD", productPanel);
        tabs.addTab("Order CRUD", orderPanel);

        add(tabs, BorderLayout.CENTER);
    }
}
8. AppLauncher.java bản hoàn chỉnh
package com.example.salesmis;

import com.example.salesmis.controller.CustomerController;
import com.example.salesmis.controller.OrderController;
import com.example.salesmis.controller.ProductController;
import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.dao.ProductDAO;
import com.example.salesmis.dao.SalesOrderDAO;
import com.example.salesmis.dao.impl.CustomerDAOImpl;
import com.example.salesmis.dao.impl.ProductDAOImpl;
import com.example.salesmis.dao.impl.SalesOrderDAOImpl;
import com.example.salesmis.service.CustomerService;
import com.example.salesmis.service.LookupService;
import com.example.salesmis.service.OrderService;
import com.example.salesmis.service.ProductService;
import com.example.salesmis.service.impl.CustomerServiceImpl;
import com.example.salesmis.service.impl.LookupServiceImpl;
import com.example.salesmis.service.impl.OrderServiceImpl;
import com.example.salesmis.service.impl.ProductServiceImpl;
import com.example.salesmis.view.CustomerManagementPanel;
import com.example.salesmis.view.MainFrame;
import com.example.salesmis.view.OrderManagementPanel;
import com.example.salesmis.view.ProductManagementPanel;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        CustomerDAO customerDAO = new CustomerDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        SalesOrderDAO salesOrderDAO = new SalesOrderDAOImpl();

        CustomerService customerService = new CustomerServiceImpl(customerDAO);
        CustomerController customerController = new CustomerController(customerService);

        ProductService productService = new ProductServiceImpl(productDAO);
        ProductController productController = new ProductController(productService);

        LookupService lookupService = new LookupServiceImpl(customerDAO, productDAO);
        OrderService orderService = new OrderServiceImpl(salesOrderDAO, customerDAO, productDAO);
        OrderController orderController = new OrderController(orderService, lookupService);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(
                    new CustomerManagementPanel(customerController),
                    new ProductManagementPanel(productController),
                    new OrderManagementPanel(orderController)
            );
            frame.setVisible(true);
        });
    }
}
9. Nhận xét 
Bây giờ bộ dự án của đã có đủ 3 module cốt lõi:
1. Customer CRUD
•	quản lý khách hàng
•	ràng buộc mã khách hàng
•	email duy nhất
2. Product CRUD
•	quản lý sản phẩm
•	ràng buộc SKU
•	đơn giá và tồn kho hợp lệ
3. Order CRUD
•	quản lý đơn hàng
•	quản lý chi tiết đơn hàng
•	tự tính tổng tiền đơn hàng
•	nhiều dòng chi tiết trong một đơn
Đây là cấu trúc rất chuẩn cho một đồ án MIS bán hàng ở mức môn học.
10. Điểm mạnh 
Nhấn mạnh các điểm quan trọng:
•	Customer và Product là master data
•	SalesOrder và OrderDetail là transaction data
Đây là cách phân loại rất hay trong hệ thống thông tin quản lý.

