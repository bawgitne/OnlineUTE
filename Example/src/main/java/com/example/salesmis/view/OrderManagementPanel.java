package com.example.salesmis.view;

import com.example.salesmis.controller.OrderController;
import com.example.salesmis.model.dto.OrderLineInput;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.OrderDetail;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.enumtype.OrderStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderManagementPanel extends JPanel {
    private final OrderController orderController;

    private JTextField txtId, txtOrderNo, txtDate, txtNote, txtSearch;
    private JComboBox<Customer> cbCustomer;
    private JComboBox<OrderStatus> cbStatus;

    private JTable tblOrders;
    private DefaultTableModel orderTableModel;

    private JTable tblDetails;
    private DefaultTableModel detailsTableModel;

    private Long selectedOrderId;

    public OrderManagementPanel(OrderController orderController) {
        this.orderController = orderController;
        initComponents();
        loadOrders();
        refreshLookups();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(buildMasterPanel());
        splitPane.setBottomComponent(buildDetailPanel());
        splitPane.setDividerLocation(400);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel buildMasterPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel form = new JPanel(new GridLayout(4, 4, 5, 5));
        txtId = new JTextField(); txtId.setEditable(false);
        txtOrderNo = new JTextField();
        txtDate = new JTextField(LocalDate.now().toString());
        txtNote = new JTextField();
        cbCustomer = new JComboBox<>();
        cbStatus = new JComboBox<>(OrderStatus.values());
        txtSearch = new JTextField();

        form.add(new JLabel("ID:")); form.add(txtId);
        form.add(new JLabel("Order No:")); form.add(txtOrderNo);
        form.add(new JLabel("Date:")); form.add(txtDate);
        form.add(new JLabel("Customer:")); form.add(cbCustomer);
        form.add(new JLabel("Status:")); form.add(cbStatus);
        form.add(new JLabel("Note:")); form.add(txtNote);
        form.add(new JLabel("Search:")); form.add(txtSearch);

        panel.add(form, BorderLayout.NORTH);

        orderTableModel = new DefaultTableModel(new String[]{"ID", "No", "Date", "Customer", "Total", "Status"}, 0);
        tblOrders = new JTable(orderTableModel);
        tblOrders.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblOrders.getSelectedRow() != -1) {
                fillForm();
            }
        });
        panel.add(new JScrollPane(tblOrders), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Save New");
        JButton btnUpdate = new JButton("Update Selected");
        JButton btnDelete = new JButton("Delete");
        JButton btnSearch = new JButton("Search");

        btnAdd.addActionListener(e -> saveOrder(false));
        btnUpdate.addActionListener(e -> saveOrder(true));
        btnDelete.addActionListener(e -> deleteOrder());
        btnSearch.addActionListener(e -> search());

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnSearch);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Order Details (Line Items)"));

        detailsTableModel = new DefaultTableModel(new String[]{"Product ID", "Product Name", "Qty", "Price", "Total"}, 0);
        tblDetails = new JTable(detailsTableModel);
        panel.add(new JScrollPane(tblDetails), BorderLayout.CENTER);

        JPanel detailOps = new JPanel();
        JButton btnAddLine = new JButton("Add Line");
        JButton btnRemoveLine = new JButton("Remove Line");

        btnAddLine.addActionListener(e -> addLine());
        btnRemoveLine.addActionListener(e -> {
            int row = tblDetails.getSelectedRow();
            if (row != -1) detailsTableModel.removeRow(row);
        });

        detailOps.add(btnAddLine); detailOps.add(btnRemoveLine);
        panel.add(detailOps, BorderLayout.SOUTH);

        return panel;
    }

    private void addLine() {
        List<Product> products = orderController.getLookupProducts();
        if (products.isEmpty()) return;
        Product p = (Product) JOptionPane.showInputDialog(this, "Select Product", "Add Line",
                JOptionPane.QUESTION_MESSAGE, null, products.toArray(), products.get(0));

        if (p != null) {
            String qtyStr = JOptionPane.showInputDialog("Quantity:", "1");
            if (qtyStr != null) {
                int qty = Integer.parseInt(qtyStr);
                detailsTableModel.addRow(new Object[]{
                        p.getId(), p.getProductName(), qty, p.getUnitPrice(),
                        p.getUnitPrice().multiply(BigDecimal.valueOf(qty))
                });
            }
        }
    }

    private void loadOrders() {
        orderTableModel.setRowCount(0);
        for (SalesOrder o : orderController.getAllOrders()) {
            orderTableModel.addRow(new Object[]{
                    o.getId(), o.getOrderNo(), o.getOrderDate(), o.getCustomer().getFullName(),
                    o.getTotalAmount(), o.getStatus()
            });
        }
    }

    private void refreshLookups() {
        cbCustomer.removeAllItems();
        for (Customer c : orderController.getLookupCustomers()) cbCustomer.addItem(c);
    }

    private void fillForm() {
        int row = tblOrders.getSelectedRow();
        selectedOrderId = (Long) tblOrders.getValueAt(row, 0);
        SalesOrder o = orderController.getOrderById(selectedOrderId);

        txtId.setText(o.getId().toString());
        txtOrderNo.setText(o.getOrderNo());
        txtDate.setText(o.getOrderDate().toString());
        txtNote.setText(o.getNote());
        cbStatus.setSelectedItem(o.getStatus());
        cbCustomer.setSelectedItem(o.getCustomer());

        detailsTableModel.setRowCount(0);
        for (OrderDetail d : o.getOrderDetails()) {
            detailsTableModel.addRow(new Object[]{
                    d.getProduct().getId(), d.getProduct().getProductName(),
                    d.getQuantity(), d.getUnitPrice(), d.getLineTotal()
            });
        }
    }

    private void saveOrder(boolean isUpdate) {
        try {
            String no = txtOrderNo.getText();
            LocalDate date = LocalDate.parse(txtDate.getText());
            Customer c = (Customer) cbCustomer.getSelectedItem();
            OrderStatus status = (OrderStatus) cbStatus.getSelectedItem();
            String note = txtNote.getText();

            List<OrderLineInput> lines = new ArrayList<>();
            for (int i = 0; i < detailsTableModel.getRowCount(); i++) {
                lines.add(new OrderLineInput(
                        (Long) detailsTableModel.getValueAt(i, 0),
                        (Integer) detailsTableModel.getValueAt(i, 2),
                        (BigDecimal) detailsTableModel.getValueAt(i, 3)
                ));
            }

            if (isUpdate) {
                orderController.updateOrder(selectedOrderId, no, date, c.getId(), status, note, lines);
            } else {
                orderController.createOrder(no, date, c.getId(), status, note, lines);
            }
            loadOrders();
            JOptionPane.showMessageDialog(this, "Saved successfully");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteOrder() {
        if (selectedOrderId == null) return;
        if (JOptionPane.showConfirmDialog(this, "Delete?") == JOptionPane.YES_OPTION) {
            orderController.deleteOrder(selectedOrderId);
            loadOrders();
        }
    }

    private void search() {
        String kw = txtSearch.getText();
        List<SalesOrder> list = orderController.searchOrders(kw);
        orderTableModel.setRowCount(0);
        for (SalesOrder o : list) {
            orderTableModel.addRow(new Object[]{
                    o.getId(), o.getOrderNo(), o.getOrderDate(), o.getCustomer().getFullName(),
                    o.getTotalAmount(), o.getStatus()
            });
        }
    }
}
