package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Admin() {}

    public Admin(Long id, String code, String fullName, Account account) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.account = account;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
