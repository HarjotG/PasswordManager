package com.example.passwordmanager;

public class Account {
    // Unique ID for each account
    public String id;
    // Username associated with the account (null if none)
    public String username;
    // Password associated with the account (encrypted if returned by getAccounts and decrypted if returned by getAccount)
    public String password;
    // Email associated with the account (null if none)
    public String email;
    // Tag associated with the account (general by default)
    public String tag;
    // Website the account is registered on
    public String site;

    public Account(String id, String username, String password, String email, String site, String tag) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.tag = tag;
        this.site = site;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", tag='" + tag + '\'' +
                ", site='" + site + '\'' +
                '}';
    }
}
