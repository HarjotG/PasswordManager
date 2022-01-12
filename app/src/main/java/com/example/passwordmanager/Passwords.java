package com.example.passwordmanager;

import static com.example.passwordmanager.DB.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Passwords extends AppCompatActivity {
    private DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);

        Button button = (Button) findViewById(R.id.button_createaccount);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createAccount(v);
            }
        });

        Passwords.this.deleteDatabase(DATABASE_NAME);
        db = new DB(Passwords.this);
        Log.d("password", "Password view created");
    }
    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void createAccount(View view) {
        Account account = new Account("1", "Harjot", "pass123", "harjot123g@gmail.com", "test.com", "General");
        long id = db.createAccount(account);
        if(id == -1) {
            Log.e("PASS_ACCOUNT1", "ERROR in adding new account to database");
        }
        Account[] accounts = db.getAccounts();
        for(int i = 0; i < accounts.length; i++) {
            Log.d("PASS_ACCOUNT2", accounts[i].toString());
        }

        db.updateAccount(accounts[0], new Account("12321", "john", "passs12334", "", "", "General"));
        accounts = db.getAccounts();
        for(int i = 0; i < accounts.length; i++) {
            Log.d("PASS_ACCOUNT3", accounts[i].toString());
        }
        if(accounts.length > 3) {
            db.deleteAccount(accounts[1]);
            accounts = db.getAccounts();
            for(int i = 0; i < accounts.length; i++) {
                Log.d("PASS_ACCOUNT4", accounts[i].toString());
            }
        }
    }

}