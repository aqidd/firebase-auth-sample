package io.github.aqidd.firebaseauthsample.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import io.github.aqidd.firebaseauthsample.R;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (findViewById(R.id.logout_button)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //firebase logout
                FirebaseAuth.getInstance().signOut();
                //facebook logout
                LoginManager.getInstance().logOut();
                startActivity(new Intent(MainActivity.this, AuthActivity.class));
                finish();
            }
        });
    }
}
