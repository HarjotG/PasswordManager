package com.example.passwordmanager;

import static com.example.passwordmanager.AccountsAdapter.ACCOUNT_EMAIL_EXTRA;
import static com.example.passwordmanager.AccountsAdapter.ACCOUNT_PASS_EXTRA;
import static com.example.passwordmanager.AccountsAdapter.ACCOUNT_SITE_EXTRA;
import static com.example.passwordmanager.AccountsAdapter.ACCOUNT_TAG_EXTRA;
import static com.example.passwordmanager.AccountsAdapter.ACCOUNT_UNAME_EXTRA;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.UserNotAuthenticatedException;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AccountView extends AppCompatActivity {
    private static final int LOCK_REQUEST_CODE = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.activity_account_view);

        TextView userView = (TextView) findViewById(R.id.view_username);
        userView.setText(intent.getStringExtra(ACCOUNT_UNAME_EXTRA));
        userView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Username", intent.getStringExtra(ACCOUNT_UNAME_EXTRA));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied username to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        TextView passView = (TextView) findViewById(R.id.view_password);
        // decrypt and set text on click
        passView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View view) {
                Auth auth = new Auth();
                String pass = null;
                try {
                    pass = auth.decrypt(intent.getStringExtra(ACCOUNT_PASS_EXTRA));
                } catch (UserNotAuthenticatedException e) {
                    KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                    Intent screenLockIntent = keyguardManager.createConfirmDeviceCredentialIntent("Authenticate", "Please authenticate yourself");
                    startActivityForResult(screenLockIntent, LOCK_REQUEST_CODE);
                }
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Password", pass);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied password to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        passView.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View view) {
                Auth auth = new Auth();
                String pass = null;
                try {
                    pass = auth.decrypt(intent.getStringExtra(ACCOUNT_PASS_EXTRA));
                    passView.setText(pass);
                } catch (UserNotAuthenticatedException e) {
                    KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                    Intent screenLockIntent = keyguardManager.createConfirmDeviceCredentialIntent("Authenticate", "Please authenticate yourself");
                    startActivityForResult(screenLockIntent, LOCK_REQUEST_CODE);
                }
                return true;
            }
        });

        TextView emailView = (TextView) findViewById(R.id.view_email);
        emailView.setText(intent.getStringExtra(ACCOUNT_EMAIL_EXTRA));
        emailView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Email", intent.getStringExtra(ACCOUNT_EMAIL_EXTRA));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied email to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        TextView siteView = (TextView) findViewById(R.id.view_site);
        siteView.setText(intent.getStringExtra(ACCOUNT_SITE_EXTRA));
        siteView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Site", intent.getStringExtra(ACCOUNT_SITE_EXTRA));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied site to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        TextView tagView = (TextView) findViewById(R.id.view_tag);
        tagView.setText(intent.getStringExtra(ACCOUNT_TAG_EXTRA));
        tagView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Tag", intent.getStringExtra(ACCOUNT_TAG_EXTRA));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied tag to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(LOCK_REQUEST_CODE == requestCode){
            if (resultCode == RESULT_OK) {
                // Authentication is successful
                Toast.makeText(getApplicationContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();
            } else {
                // Authentication failed
                Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}