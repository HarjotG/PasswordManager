package com.example.passwordmanager;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Auth auth;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.buttonLogin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login(v);
            }
        });

        // check if key is already created, otherwise create one
        // The key can also be obtained from the Android Keystore any time as follows:
        auth = new Auth();
        if(auth.hasKey()) {
            loggedIn();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void login(View view) {
        if(!auth.hasKey()) {
            auth.generateKey();
        }
        loggedIn();
    }

    // After logging in, switch to the password view to see all the passwords
    private void loggedIn(){
        Intent intent = new Intent(this, Passwords.class);
        startActivity(intent);
    }
}
