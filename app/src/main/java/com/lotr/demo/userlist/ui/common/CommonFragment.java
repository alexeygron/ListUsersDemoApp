package com.lotr.demo.userlist.ui.common;

import android.app.Fragment;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lotr.demo.userlist.R;

public class CommonFragment extends Fragment {

    private Toolbar toolbar;
    public static final int NO_TITLE = -1000;

    /**
     * Init toolbar for current child fragment
     *
     * @param parentView child fragment
     * @param titleResId string res id for title
     */
    protected void setUpToolBar(@NonNull View parentView, int toolbarResId, int titleResId) {
        toolbar = (Toolbar) parentView.findViewById(toolbarResId);
        if (toolbar == null) return;

        if (titleResId == NO_TITLE) {
            toolbar.setTitle(null);
        } else {
            toolbar.setTitle(titleResId);
        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    protected void enableUpButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (ab == null) return;
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            setHasOptionsMenu(true);
        }
    }

    protected void enableDualPaneMode(boolean state) {
        getActivity().findViewById(R.id.secondary_container).setVisibility(state ? View.VISIBLE : View.GONE);
    }

    protected boolean isDualPaneModeEnabled() {
        return getActivity().getResources().getBoolean(R.bool.dualPaneMode);
    }

    protected void setTitle(int titleResId) {
        if (toolbar == null) return;
        toolbar.setTitle(titleResId);
    }

    protected void showSoftwareKeyboard(IBinder binder, boolean showKeyboard) {
        InputMethodManager inputManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(binder,
                showKeyboard ? InputMethodManager.SHOW_FORCED : InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
