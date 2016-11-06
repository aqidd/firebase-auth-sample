package io.github.aqidd.firebaseauthsample.fragments;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment
{

    /**
     * method used for first UI initialization & manipulation
     */
    abstract void initUI ();

    /**
     * method used for first event initialization & manipulation
     */
    abstract void initEvent ();

    protected void hideKeyboard ()
    {
        View view = getActivity().getCurrentFocus();
        if (view != null)
        {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                                                                                                       hideSoftInputFromWindow(
                                                                                                               view.getWindowToken(),
                                                                                                               InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
