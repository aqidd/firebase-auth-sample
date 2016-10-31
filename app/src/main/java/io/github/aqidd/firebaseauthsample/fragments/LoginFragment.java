package io.github.aqidd.firebaseauthsample.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.aqidd.firebaseauthsample.MainActivity;
import io.github.aqidd.firebaseauthsample.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLoginFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();
    @BindView(R.id.login_button)
    Button btLoginButton;
    @BindView(R.id.forgot_password)
    TextView tvForgotPassword;
    @BindView(R.id.email_wrapper)
    TextInputLayout tilEmailWrapper;
    @BindView(R.id.password_wrapper)
    TextInputLayout tilPasswordWrapper;

    private OnLoginFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                submitLogin();
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onForgotPasswordClick();
                }
            }
        });

        return v;
    }

    public void submitLogin() {
        String email = tilEmailWrapper.getEditText().getText().toString();
        String password = tilPasswordWrapper.getEditText().getText().toString();

        if (!validate(email, password)) {
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
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSuccess or onFailed
                        onSuccess();
                        // onFailed();
                        progressDialog.dismiss();
                    }
                }, 2000
        );

    }

    public boolean validate(final String email, final String password) {
        boolean valid = true;

        /**
         * TODO:PUT YOUR FIELD VALIDATION BELOW
         */
        if (email.length() == 0) {
            tilEmailWrapper.setError("Email cannot be empty!");
            valid = false;
        }
        if (password.length() == 0) {
            tilPasswordWrapper.setError("Password cannot be empty!");
            valid = false;
        }

        return valid;
    }

    public void onSuccess() {
        btLoginButton.setEnabled(true);
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void onFailed() {
        Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_LONG).show();
        btLoginButton.setEnabled(true);
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLoginFragmentInteractionListener {
        void onForgotPasswordClick();
    }
}
