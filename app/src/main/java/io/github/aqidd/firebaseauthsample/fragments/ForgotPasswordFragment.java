package io.github.aqidd.firebaseauthsample.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.aqidd.firebaseauthsample.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnForgotFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ForgotPasswordFragment extends Fragment {


    @BindView(R.id.forgot_submit)
    protected Button btForgotSubmit;
    @BindView(R.id.email_wrapper)
    protected TextInputLayout tilEmailWrapper;

    private OnForgotFragmentInteractionListener mListener;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, v);

        btForgotSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                submitForgotPassword();
            }
        });

        return v;
    }

    private void submitForgotPassword() {
        String email = tilEmailWrapper.getEditText().getText().toString();
        if (!validateEmail(email)) {
            return;
        }

        //precaution for double click
        btForgotSubmit.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
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

    public boolean validateEmail(String email) {
        boolean valid = true;
        /**
         * TODO:PUT YOUR FIELD VALIDATION BELOW
         */
        if (email.length() == 0) {
            tilEmailWrapper.setError("Email cannot be empty!");
            valid = false;
        }

        return valid;
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void onSuccess() {
        //TODO:IMPLEMENT SUCCESS HANDLER HERE
        btForgotSubmit.setEnabled(true);
    }

    private void onFailed() {
        //TODO:IMPLEMENT ERROR HANDLING HERE
        btForgotSubmit.setEnabled(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnForgotFragmentInteractionListener) {
            mListener = (OnForgotFragmentInteractionListener) context;
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
    public interface OnForgotFragmentInteractionListener {

    }
}
