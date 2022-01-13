package com.example.passwordmanager;

import static com.example.passwordmanager.Passwords.ACTIVITY_RESULT;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {
    private DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Button button = (Button) findViewById(R.id.button_addAccount);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addAccount(view);
            }
        });
        db = new DB(CreateAccount.this);
    }

    // Onclick handler for the add account button
    // Add the account to the database and encrypt the password
    // TODO: Generate a password
    public void addAccount(View view) {
        EditText userText = (EditText) findViewById(R.id.text_username);
        String username = userText.getText().toString();
        EditText passText = (EditText) findViewById(R.id.text_password);
        String password = passText.getText().toString();
        EditText emailText = (EditText) findViewById(R.id.text_email);
        String email = emailText.getText().toString();
        EditText siteText = (EditText) findViewById(R.id.text_site);
        String site = siteText.getText().toString();
        EditText tagText = (EditText) findViewById(R.id.text_tag);
        String tag = tagText.getText().toString();

        // check if password is given
        if(password.length() == 0) {
            Log.e("CREATE_ACCOUNT", "Password not provided");
            Toast.makeText(getApplicationContext(), "Password must be provided", Toast.LENGTH_SHORT).show();
            return;
        }
        // default tag to General if not given
        if(tag.length() == 0) {
            tag = "General";
        }

        // add account to database
        // TODO: Encrypt Password
        Account account = new Account("", username, password, email, site, tag);
        long id = db.createAccount(account);
        if(id == -1) {
            Log.e("CREATE_ACCOUNT", "Database error");
            Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
            return;
        }
        account.id = String.valueOf(id);
        Log.d("CREATE_ACCOUNT", "Account created " + account.toString());
        Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ACTIVITY_RESULT, new String[]{account.id, account.username, account.password, account.email, account.site, account.tag});
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}