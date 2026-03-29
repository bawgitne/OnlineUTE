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
