Thiết kế Customer CRUD theo đúng cấu trúc của dự án Java Swing MVC + Hibernate/JPA + MySQL cho hệ MIS bán hàng.
Thực hiện đồng bộ với project trước:
•	package: com.example.salesmis
•	kiến trúc: DAO -> Service -> Controller -> View
•	dùng entity Customer
•	Swing panel CRUD đầy đủ:
o	thêm
o	sửa
o	xóa
o	tìm kiếm
o	chọn dòng trên bảng để nạp lại form
1. Mở rộng CustomerDAO.java
Thay bằng bản đầy đủ này.
package com.example.salesmis.dao;

import com.example.salesmis.model.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> findAll();

    Optional<Customer> findById(Long id);

    Optional<Customer> findByCode(String customerCode);

    Optional<Customer> findByEmail(String email);

    List<Customer> searchByKeyword(String keyword);

    Customer save(Customer customer);

    Customer update(Customer customer);

    void deleteById(Long id);

    boolean existsByCode(String customerCode);

    boolean existsByEmail(String email);
}
2. CustomerDAOImpl.java
package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.model.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public List<Customer> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT c
                    FROM Customer c
                    ORDER BY c.id
                    """, Customer.class).getResultList();
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

    @Override
    public Optional<Customer> findByCode(String customerCode) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Customer> list = em.createQuery("""
                    SELECT c
                    FROM Customer c
                    WHERE LOWER(c.customerCode) = LOWER(:customerCode)
                    """, Customer.class)
                    .setParameter("customerCode", customerCode)
                    .getResultList();

            return list.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Customer> list = em.createQuery("""
                    SELECT c
                    FROM Customer c
                    WHERE LOWER(c.email) = LOWER(:email)
                    """, Customer.class)
                    .setParameter("email", email)
                    .getResultList();

            return list.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Customer> searchByKeyword(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT c
                    FROM Customer c
                    WHERE LOWER(c.customerCode) LIKE LOWER(:kw)
                       OR LOWER(c.fullName) LIKE LOWER(:kw)
                       OR LOWER(COALESCE(c.phone, '')) LIKE LOWER(:kw)
                       OR LOWER(COALESCE(c.email, '')) LIKE LOWER(:kw)
                    ORDER BY c.fullName
                    """, Customer.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Customer save(Customer customer) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(customer);
            tx.commit();
            return customer;
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
    public Customer update(Customer customer) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Customer merged = em.merge(customer);
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
            Customer customer = em.find(Customer.class, id);
            if (customer != null) {
                em.remove(customer);
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
    public boolean existsByCode(String customerCode) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("""
                    SELECT COUNT(c)
                    FROM Customer c
                    WHERE LOWER(c.customerCode) = LOWER(:customerCode)
                    """, Long.class)
                    .setParameter("customerCode", customerCode)
                    .getSingleResult();

            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("""
                    SELECT COUNT(c)
                    FROM Customer c
                    WHERE LOWER(c.email) = LOWER(:email)
                    """, Long.class)
                    .setParameter("email", email)
                    .getSingleResult();

            return count != null && count > 0;
        } finally {
            em.close();
        }
    }
}
3. CustomerService.java
package com.example.salesmis.service;

import com.example.salesmis.model.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();

    List<Customer> searchCustomers(String keyword);

    Customer getCustomerById(Long id);

    Customer createCustomer(String customerCode,
                            String fullName,
                            String phone,
                            String email,
                            String address,
                            Boolean active);

    Customer updateCustomer(Long id,
                            String customerCode,
                            String fullName,
                            String phone,
                            String email,
                            String address,
                            Boolean active);

    void deleteCustomer(Long id);
}
4. CustomerServiceImpl.java
package com.example.salesmis.service.impl;

import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    @Override
    public List<Customer> searchCustomers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return customerDAO.findAll();
        }
        return customerDAO.searchByKeyword(keyword.trim());
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với id = " + id));
    }

    @Override
    public Customer createCustomer(String customerCode,
                                   String fullName,
                                   String phone,
                                   String email,
                                   String address,
                                   Boolean active) {

        validate(customerCode, fullName, phone, email, address);

        if (customerDAO.existsByCode(customerCode.trim())) {
            throw new IllegalArgumentException("Mã khách hàng đã tồn tại: " + customerCode);
        }

        if (email != null && !email.trim().isEmpty() && customerDAO.existsByEmail(email.trim())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + email);
        }

        Customer customer = new Customer();
        customer.setCustomerCode(customerCode.trim());
        customer.setFullName(fullName.trim());
        customer.setPhone(normalize(phone));
        customer.setEmail(normalize(email));
        customer.setAddress(normalize(address));
        customer.setActive(active != null ? active : Boolean.TRUE);

        return customerDAO.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id,
                                   String customerCode,
                                   String fullName,
                                   String phone,
                                   String email,
                                   String address,
                                   Boolean active) {

        validate(customerCode, fullName, phone, email, address);

        Customer existing = customerDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với id = " + id));

        Customer sameCode = customerDAO.findByCode(customerCode.trim()).orElse(null);
        if (sameCode != null && !sameCode.getId().equals(id)) {
            throw new IllegalArgumentException("Mã khách hàng đã được dùng bởi khách hàng khác.");
        }

        if (email != null && !email.trim().isEmpty()) {
            Customer sameEmail = customerDAO.findByEmail(email.trim()).orElse(null);
            if (sameEmail != null && !sameEmail.getId().equals(id)) {
                throw new IllegalArgumentException("Email đã được dùng bởi khách hàng khác.");
            }
        }

        existing.setCustomerCode(customerCode.trim());
        existing.setFullName(fullName.trim());
        existing.setPhone(normalize(phone));
        existing.setEmail(normalize(email));
        existing.setAddress(normalize(address));
        existing.setActive(active != null ? active : Boolean.TRUE);

        return customerDAO.update(existing);
    }

    @Override
    public void deleteCustomer(Long id) {
        getCustomerById(id);
        customerDAO.deleteById(id);
    }

    private void validate(String customerCode,
                          String fullName,
                          String phone,
                          String email,
                          String address) {

        if (customerCode == null || customerCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng không được để trống.");
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống.");
        }

        if (customerCode.trim().length() > 20) {
            throw new IllegalArgumentException("Mã khách hàng tối đa 20 ký tự.");
        }

        if (fullName.trim().length() > 150) {
            throw new IllegalArgumentException("Tên khách hàng tối đa 150 ký tự.");
        }

        if (phone != null && phone.trim().length() > 20) {
            throw new IllegalArgumentException("Số điện thoại tối đa 20 ký tự.");
        }

        if (email != null && email.trim().length() > 150) {
            throw new IllegalArgumentException("Email tối đa 150 ký tự.");
        }

        if (address != null && address.trim().length() > 255) {
            throw new IllegalArgumentException("Địa chỉ tối đa 255 ký tự.");
        }

        if (email != null && !email.trim().isEmpty() && !isValidEmail(email.trim())) {
            throw new IllegalArgumentException("Email không đúng định dạng.");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private String normalize(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }
}
5. CustomerController.java
package com.example.salesmis.controller;

import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.service.CustomerService;

import java.util.List;

public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public List<Customer> searchCustomers(String keyword) {
        return customerService.searchCustomers(keyword);
    }

    public Customer getCustomerById(Long id) {
        return customerService.getCustomerById(id);
    }

    public Customer createCustomer(String customerCode,
                                   String fullName,
                                   String phone,
                                   String email,
                                   String address,
                                   boolean active) {

        return customerService.createCustomer(
                customerCode,
                fullName,
                phone,
                email,
                address,
                active
        );
    }

    public Customer updateCustomer(Long id,
                                   String customerCode,
                                   String fullName,
                                   String phone,
                                   String email,
                                   String address,
                                   boolean active) {

        return customerService.updateCustomer(
                id,
                customerCode,
                fullName,
                phone,
                email,
                address,
                active
        );
    }

    public void deleteCustomer(Long id) {
        customerService.deleteCustomer(id);
    }
}
6. CustomerManagementPanel.java
Đây là phần giao diện Swing CRUD đầy đủ.
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
7. Cách ghép vào AppLauncher.java
Nếu muốn chạy Customer CRUD riêng, có thể test nhanh bằng frame riêng:
package com.example.salesmis;

import com.example.salesmis.controller.CustomerController;
import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.dao.impl.CustomerDAOImpl;
import com.example.salesmis.service.CustomerService;
import com.example.salesmis.service.impl.CustomerServiceImpl;
import com.example.salesmis.view.CustomerManagementPanel;

import javax.swing.*;

public class AppLauncherCustomer {
    public static void main(String[] args) {
        CustomerDAO customerDAO = new CustomerDAOImpl();
        CustomerService customerService = new CustomerServiceImpl(customerDAO);
        CustomerController customerController = new CustomerController(customerService);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Customer CRUD - Sales MIS");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 650);
            frame.setLocationRelativeTo(null);
            frame.add(new CustomerManagementPanel(customerController));
            frame.setVisible(true);
        });
    }
}
8. Cách ghép Customer CRUD vào MainFrame cùng Order CRUD
Nên dùng JTabbedPane.
MainFrame.java bản ghép 2 module
package com.example.salesmis.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(CustomerManagementPanel customerPanel,
                     OrderManagementPanel orderPanel) {

        setTitle("MIS - Sales Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1350, 760);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Customer CRUD", customerPanel);
        tabs.addTab("Order CRUD", orderPanel);

        add(tabs, BorderLayout.CENTER);
    }
}
AppLauncher.java bản ghép 2 module
package com.example.salesmis;

import com.example.salesmis.controller.CustomerController;
import com.example.salesmis.controller.OrderController;
import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.dao.ProductDAO;
import com.example.salesmis.dao.SalesOrderDAO;
import com.example.salesmis.dao.impl.CustomerDAOImpl;
import com.example.salesmis.dao.impl.ProductDAOImpl;
import com.example.salesmis.dao.impl.SalesOrderDAOImpl;
import com.example.salesmis.service.CustomerService;
import com.example.salesmis.service.LookupService;
import com.example.salesmis.service.OrderService;
import com.example.salesmis.service.impl.CustomerServiceImpl;
import com.example.salesmis.service.impl.LookupServiceImpl;
import com.example.salesmis.service.impl.OrderServiceImpl;
import com.example.salesmis.view.CustomerManagementPanel;
import com.example.salesmis.view.MainFrame;
import com.example.salesmis.view.OrderManagementPanel;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        CustomerDAO customerDAO = new CustomerDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        SalesOrderDAO salesOrderDAO = new SalesOrderDAOImpl();

        CustomerService customerService = new CustomerServiceImpl(customerDAO);
        CustomerController customerController = new CustomerController(customerService);

        LookupService lookupService = new LookupServiceImpl(customerDAO, productDAO);
        OrderService orderService = new OrderServiceImpl(salesOrderDAO, customerDAO, productDAO);
        OrderController orderController = new OrderController(orderService, lookupService);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(
                    new CustomerManagementPanel(customerController),
                    new OrderManagementPanel(orderController)
            );
            frame.setVisible(true);
        });
    }
}
9. Trình bày phần Customer CRUD 
Bước 1: Entity
•	Customer ánh xạ bảng customers
Bước 2: DAO
•	findAll()
•	findById()
•	save()
•	update()
•	deleteById()
•	searchByKeyword()
Bước 3: Service
•	validate nghiệp vụ
•	kiểm tra trùng customerCode
•	kiểm tra trùng email
Bước 4: Controller
•	nhận dữ liệu từ Swing
•	gọi Service
Bước 5: View
•	form nhập liệu
•	bảng hiển thị dữ liệu
•	CRUD hoàn chỉnh
10. Điểm mạnh của module Customer này
Module này đồng bộ với Order CRUD ở các điểm:
•	cùng kiến trúc DAO -> Service -> Controller -> View
•	cùng style validate dữ liệu
•	cùng style transaction ở DAO
•	cùng dùng DefaultTableModel cho JTable
•	dễ ghép vào JTabbedPane hoặc CardLayout
11. Lưu ý thực tế
Trong đồ án hiện tại, orders.customer_id đang FK tới customers.id, nên:
•	nếu khách hàng đã có đơn hàng
•	xóa khách hàng sẽ có thể bị chặn bởi MySQL ON DELETE RESTRICT
Điều này là đúng nghiệp vụ về:
•	ràng buộc khóa ngoại
•	toàn vẹn dữ liệu
•	khác biệt giữa “xóa vật lý” và “khóa hoạt động”
Nếu muốn, có thể sửa thêm để:
•	không xóa vật lý Customer
•	mà chuyển sang soft delete / active = false
để phù hợp hệ MIS hơn.
12. Bước tiếp theo 
Sau Customer CRUD, thực hiện tiếp:
•	Product CRUD
•	rồi cập nhật MainFrame thành 3 tab:
o	Customer CRUD
o	Product CRUD
o	Order CRUD

