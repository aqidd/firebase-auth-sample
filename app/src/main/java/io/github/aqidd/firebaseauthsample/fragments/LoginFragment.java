package io.github.aqidd.firebaseauthsample.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.aqidd.firebaseauthsample.R;
import io.github.aqidd.firebaseauthsample.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLoginFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment
{
    private static final String TAG = LoginFragment.class.getSimpleName();

    @BindView(R.id.login_button)
    Button btLoginButton;
    @BindView(R.id.facebook_login_button)
    LoginButton btFacebookLogin;
    @BindView(R.id.forgot_password)
    TextView tvForgotPassword;
    @BindView(R.id.email_wrapper)
    TextInputLayout tilEmailWrapper;
    @BindView(R.id.password_wrapper)
    TextInputLayout tilPasswordWrapper;
    @BindView(R.id.register)
    TextView tvRegister;

    private OnLoginFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private CallbackManager mCallbackManager;

    public LoginFragment()
    {
        // Required empty public constructor
        setArguments(new Bundle());
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener)
        {
            mListener = (OnLoginFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                                       + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult result)
            {
                facebookLoginSuccess(result.getAccessToken());
            }

            @Override
            public void onCancel()
            {
                onFailed();
            }

            @Override
            public void onError(FacebookException error)
            {
                onFailed();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener()
        {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth auth)
            {
                FirebaseUser lUser = auth.getCurrentUser();
                if (lUser != null)
                {
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                }
                else
                {
                    // do nothing. wait for user to sign in.
                }
            }
        };

        //get key hash for facebook
        //try {
        //    PackageInfo info = getActivity().getPackageManager().getPackageInfo(
        //            "io.github.aqidd.firebaseauthsample",
        //            PackageManager.GET_SIGNATURES);
        //    for (android.content.pm.Signature signature : info.signatures) {
        //        MessageDigest md = MessageDigest.getInstance("SHA");
        //        md.update(signature.toByteArray());
        //        Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        //    }
        //} catch (PackageManager.NameNotFoundException e) {
        //
        //} catch (NoSuchAlgorithmException e) {
        //
        //}

        btFacebookLogin.setReadPermissions("email", "public_profile");
        btFacebookLogin.setFragment(this);
        btFacebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult result)
            {
                facebookLoginSuccess(result.getAccessToken());
            }

            @Override
            public void onCancel()
            {
                onFailed();
            }

            @Override
            public void onError(FacebookException error)
            {
                onFailed();
            }
        });

        btLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideKeyboard();
                submitLogin();
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mListener != null)
                {
                    mListener.onForgotPasswordClick();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mListener != null)
                {
                    mListener.onRegisterClick();
                }
            }
        });

        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthStateListener != null)
        {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    private void facebookLoginSuccess(AccessToken token)
    {
        AuthCredential lCredential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(lCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (!task.isSuccessful())
                {
                    onFailed();
                }
                else
                {
                    onSuccess();
                }
            }
        });
    }

    public void submitLogin()
    {
        String email = tilEmailWrapper.getEditText().getText().toString();
        String password = tilPasswordWrapper.getEditText().getText().toString();

        if (!validate(email, password))
        {
            return;
        }
        //precaution for double click
        btLoginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating");
        progressDialog.show();

        /**
         * TODO:PUT YOUR AUTH PROCESS BELOW
         */
        mAuth.signInWithEmailAndPassword(email, password)
             .addOnCompleteListener(getActivity(),
                                    new OnCompleteListener<AuthResult>()
                                    {
                                        @Override
                                        public void onComplete(
                                                @NonNull Task<AuthResult> task)
                                        {
                                            progressDialog.dismiss();
                                            if (!task.isSuccessful())
                                            {
                                                onFailed();
                                            }
                                            else
                                            {
                                                onSuccess();
                                            }
                                        }
                                    });

    }

    public boolean validate(final String email, final String password)
    {
        boolean valid = true;

        /**
         * TODO:PUT YOUR FIELD VALIDATION BELOW
         */
        if (email.length() == 0)
        {
            tilEmailWrapper.setError("Email cannot be empty!");
            valid = false;
        }
        if (password.length() == 0)
        {
            tilPasswordWrapper.setError("Password cannot be empty!");
            valid = false;
        }

        return valid;
    }

    public void onSuccess()
    {
        btLoginButton.setEnabled(true);
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void onFailed()
    {
        Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_LONG).show();
        btLoginButton.setEnabled(true);
    }

    private void hideKeyboard()
    {
        View view = getActivity().getCurrentFocus();
        if (view != null)
        {
            ((InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public interface OnLoginFragmentInteractionListener
    {
        void onForgotPasswordClick();

        void onRegisterClick();
    }
}
