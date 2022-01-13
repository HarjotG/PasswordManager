package com.example.passwordmanager;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Passwords extends AppCompatActivity  {
    public static final String ACTIVITY_RESULT = "ACTIVITY_RESULT";
    public static final int LAUNCH_SECOND_ACTIVITY = 1;

    private DB db;
    private ArrayList<Account> accounts;
    private AccountsAdapter accountAdapter;
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

        DB db = new DB(Passwords.this);
        accounts = db.getAccounts();

        Log.d("password", "Password view created");

        RecyclerView accountView = (RecyclerView) findViewById(R.id.account_view);
        accountAdapter = new AccountsAdapter(accounts);
        accountView.setAdapter(accountAdapter);
        accountView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void createAccount(View view) {
        Intent intent = new Intent(this, CreateAccount.class);
        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                String[] a = data.getStringArrayExtra(ACTIVITY_RESULT);
                // I know this is ugly, but it is much easier than other methods
                Account newacc = new Account(a[0], a[1], a[2], a[3], a[4], a[5]);
                accounts.add(newacc);
                for(int i = 0; i < accounts.size(); i++) {
                    Log.d("PASSWORDS_ADDED", accounts.get(i).toString());
                }
                Log.d("PASSWORDS_ADDED", String.valueOf(accounts.size()));
                accountAdapter.notifyItemInserted(accounts.size());
            }

        }
    }

}