package io.github.aqidd.firebaseauthsample.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import io.github.aqidd.firebaseauthsample.R;
import io.github.aqidd.firebaseauthsample.fragments.ForgotPasswordFragment;
import io.github.aqidd.firebaseauthsample.fragments.LoginFragment;
import io.github.aqidd.firebaseauthsample.fragments.RegisterFragment;

public class AuthActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener,
        ForgotPasswordFragment.OnForgotFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener
{

    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    LoginFragment loginFragment = new LoginFragment();
    ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
    RegisterFragment registerFragment = new RegisterFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, loginFragment);
        ft.commit();

    }

    @Override
    public void onForgotPasswordClick()
    {
        showForgotPassword();
    }

    @Override
    public void onRegisterClick()
    {
        showRegistrationForm();
    }

    @Override
    public void onBackPressed()
    {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
        {
            finish();
        }
        else
        {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void showLoginForm()
    {
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, loginFragment);
        ft.addToBackStack("");
        ft.commit();
    }

    public void showForgotPassword()
    {
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, forgotPasswordFragment);
        ft.addToBackStack("");
        ft.commit();
    }

    public void showRegistrationForm()
    {
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, registerFragment);
        ft.addToBackStack("");
        ft.commit();
    }
}


