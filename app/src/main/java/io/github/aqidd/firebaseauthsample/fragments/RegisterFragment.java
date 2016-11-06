package io.github.aqidd.firebaseauthsample.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.aqidd.firebaseauthsample.R;


public class RegisterFragment extends BaseFragment
{

    private static final String TAG = RegisterFragment.class.getSimpleName();

    @BindView (R.id.register_button)
    Button btRegister;
    @BindView (R.id.email_wrapper)
    TextInputLayout tilEmailWrapper;
    @BindView (R.id.password_wrapper)
    TextInputLayout tilPasswordWrapper;

    private FirebaseAuth mAuth;

    private OnRegisterFragmentInteractionListener mListener;

    public RegisterFragment ()
    {
        // Required empty public constructor
        setArguments(new Bundle());
    }

    @Override
    public void onAttach (Context context)
    {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentInteractionListener)
        {
            mListener = (OnRegisterFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                                       + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, v);
        mAuth = FirebaseAuth.getInstance();

        initUI();
        initEvent();

        return v;
    }

    @Override
    void initUI ()
    {
    }

    @Override
    void initEvent ()
    {
        btRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                hideKeyboard();
                submitRegistration();
            }
        });
    }

    @Override
    public void onDetach ()
    {
        super.onDetach();
    }

    public void submitRegistration ()
    {
        String email = tilEmailWrapper.getEditText().getText().toString();
        String password = tilPasswordWrapper.getEditText().getText().toString();

        if (!validate(email, password))
        {
            return;
        }
        //precaution for double click
        btRegister.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        /**
         * TODO:PUT YOUR REGISTRATION PROCESS BELOW
         */
        mAuth.createUserWithEmailAndPassword(email, password)
             .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
             {
                 @Override
                 public void onComplete (@NonNull Task<AuthResult> task)
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

    public boolean validate (final String email, final String password)
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

    public void onSuccess ()
    {
        Toast.makeText(getContext(), "Registration Success", Toast.LENGTH_SHORT).show();
        mListener.showLoginForm();
    }

    public void onFailed ()
    {
        Toast.makeText(getContext(), "Registration Failed", Toast.LENGTH_LONG).show();
        btRegister.setEnabled(true);
    }

    public interface OnRegisterFragmentInteractionListener
    {
        void showLoginForm ();
    }
}
