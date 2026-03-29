package com.example.salesmis.view;

import com.example.salesmis.controller.CustomerController;
import com.example.salesmis.model.entity.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerManagementPanel extends JPanel {

    private final CustomerController customerController;

    private JTextField txtId;
    private JTextField txtCustomerCode;
    private JTextField txtFullName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JTextField txtSearch;

    private JCheckBox chkActive;

    private JTable tblCustomers;
    private DefaultTableModel customerTableModel;

    private Long selectedCustomerId;

    public CustomerManagementPanel(CustomerController customerController) {
        this.customerController = customerController;
        initComponents();
        loadCustomerTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel lblHeader = new JLabel("QUẢN LÝ KHÁCH HÀNG");
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

        txtCustomerCode = new JTextField();
        txtFullName = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();
        txtSearch = new JTextField();

        chkActive = new JCheckBox("Active");
        chkActive.setSelected(true);

        form.add(new JLabel("ID"));
        form.add(txtId);
        form.add(new JLabel("Mã KH"));
        form.add(txtCustomerCode);

        form.add(new JLabel("Họ tên"));
        form.add(txtFullName);
        form.add(new JLabel("Điện thoại"));
        form.add(txtPhone);

        form.add(new JLabel("Email"));
        form.add(txtEmail);
        form.add(new JLabel("Địa chỉ"));
        form.add(txtAddress);

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

        btnAdd.addActionListener(e -> createCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnSearch.addActionListener(e -> searchCustomers());
        btnRefresh.addActionListener(e -> {
            clearForm();
            loadCustomerTable();
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
                "ID", "Mã KH", "Họ tên", "Điện thoại", "Email", "Địa chỉ", "Active"
        };

        customerTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblCustomers = new JTable(customerTableModel);
        tblCustomers.setRowHeight(24);

        tblCustomers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblCustomers.getSelectedRow() != -1) {
                fillFormFromSelectedRow();
            }
        });

        return new JScrollPane(tblCustomers);
    }

    private void loadCustomerTable() {
        List<Customer> customers = customerController.getAllCustomers();
        renderCustomerTable(customers);
    }

    private void renderCustomerTable(List<Customer> customers) {
        customerTableModel.setRowCount(0);

        for (Customer c : customers) {
            customerTableModel.addRow(new Object[]{
                    c.getId(),
                    c.getCustomerCode(),
                    c.getFullName(),
                    c.getPhone(),
                    c.getEmail(),
                    c.getAddress(),
                    c.getActive()
            });
        }
    }

    private void createCustomer() {
        try {
            Customer customer = customerController.createCustomer(
                    txtCustomerCode.getText(),
                    txtFullName.getText(),
                    txtPhone.getText(),
                    txtEmail.getText(),
                    txtAddress.getText(),
                    chkActive.isSelected()
            );

            JOptionPane.showMessageDialog(this,
                    "Thêm khách hàng thành công: " + customer.getFullName());

            clearForm();
            loadCustomerTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Lỗi thêm khách hàng",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer() {
        try {
            if (selectedCustomerId == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần cập nhật.");
                return;
            }

            Customer customer = customerController.updateCustomer(
                    selectedCustomerId,
                    txtCustomerCode.getText(),
                    txtFullName.getText(),
                    txtPhone.getText(),
                    txtEmail.getText(),
                    txtAddress.getText(),
                    chkActive.isSelected()
            );

            JOptionPane.showMessageDialog(this,
                    "Cập nhật thành công: " + customer.getFullName());

            clearForm();
            loadCustomerTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Lỗi cập nhật",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        try {
            if (selectedCustomerId == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xóa khách hàng này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                customerController.deleteCustomer(selectedCustomerId);
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công.");
                clearForm();
                loadCustomerTable();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Lỗi xóa",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchCustomers() {
        try {
            List<Customer> customers = customerController.searchCustomers(txtSearch.getText());
            renderCustomerTable(customers);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Lỗi tìm kiếm",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillFormFromSelectedRow() {
        int row = tblCustomers.getSelectedRow();
        if (row == -1) {
            return;
        }

        selectedCustomerId = Long.valueOf(tblCustomers.getValueAt(row, 0).toString());

        txtId.setText(String.valueOf(tblCustomers.getValueAt(row, 0)));
        txtCustomerCode.setText(String.valueOf(tblCustomers.getValueAt(row, 1)));
        txtFullName.setText(String.valueOf(tblCustomers.getValueAt(row, 2)));
        txtPhone.setText(tblCustomers.getValueAt(row, 3) != null ? tblCustomers.getValueAt(row, 3).toString() : "");
        txtEmail.setText(tblCustomers.getValueAt(row, 4) != null ? tblCustomers.getValueAt(row, 4).toString() : "");
        txtAddress.setText(tblCustomers.getValueAt(row, 5) != null ? tblCustomers.getValueAt(row, 5).toString() : "");
        chkActive.setSelected(Boolean.parseBoolean(String.valueOf(tblCustomers.getValueAt(row, 6))));
    }

    private void clearForm() {
        selectedCustomerId = null;
        txtId.setText("");
        txtCustomerCode.setText("");
        txtFullName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtSearch.setText("");
        chkActive.setSelected(true);
        tblCustomers.clearSelection();
    }
}
