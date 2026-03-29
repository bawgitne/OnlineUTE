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
