package com.example.passwordmanager;

import static com.example.passwordmanager.AccountsAdapter.ACCOUNT_EMAIL_EXTRA;
import static com.example.passwordmanager.AccountsAdapter.ACCOUNT_PASS_EXTRA;
import static com.example.passwordmanager.AccountsAdapter.ACCOUNT_SITE_EXTRA;
import static com.example.passwordmanager.AccountsAdapter.ACCOUNT_TAG_EXTRA;
import static com.example.passwordmanager.AccountsAdapter.ACCOUNT_UNAME_EXTRA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AccountView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.activity_account_view);

        TextView userView = (TextView) findViewById(R.id.view_username);
        userView.setText(intent.getStringExtra(ACCOUNT_UNAME_EXTRA));

        TextView passView = (TextView) findViewById(R.id.view_password);
        passView.setText(intent.getStringExtra(ACCOUNT_PASS_EXTRA));

        TextView emailView = (TextView) findViewById(R.id.view_email);
        emailView.setText(intent.getStringExtra(ACCOUNT_EMAIL_EXTRA));

        TextView siteView = (TextView) findViewById(R.id.view_site);
        siteView.setText(intent.getStringExtra(ACCOUNT_SITE_EXTRA));

        TextView tagView = (TextView) findViewById(R.id.view_tag);
        tagView.setText(intent.getStringExtra(ACCOUNT_TAG_EXTRA));
    }
}