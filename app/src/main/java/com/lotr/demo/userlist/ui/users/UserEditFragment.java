package com.lotr.demo.userlist.ui.users;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lotr.demo.userlist.R;
import com.lotr.demo.userlist.model.User;
import com.lotr.demo.userlist.ui.CommonFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lotr.demo.userlist.utils.LogUtils.makeLogTag;

/**
 * Provide update current or create new users
 */
public class UserEditFragment extends CommonFragment implements IUserEditView{

    @BindView(R.id.etFirstName) EditText etFirstName;
    @BindView(R.id.etLastName) EditText etLastName;
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.ilFirstName) TextInputLayout ilFirstName;
    @BindView(R.id.ilLastName) TextInputLayout ilLastName;
    @BindView(R.id.ilEmail) TextInputLayout ilEmail;

    private UserEditPresenter presenter;

    public static final String KEY_MODE = "mode";
    public static final String KEY_USER = "user";
    private static final String LOG_TAG = makeLogTag(UserEditFragment.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_edit_fragment, container, false);
        ButterKnife.bind(this, v);
        setUpToolBar(v, R.id.toolbar, CommonFragment.NO_TITLE);
        enableUpButton();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Mode mode = (UserEditFragment.Mode) getArguments().get(UserEditFragment.KEY_MODE);
        int titleResId = mode == UserEditFragment.Mode.UPDATE ? R.string.title_user_edit : R.string.title_user_add;
        setTitle(titleResId);

        if (presenter == null) {
            presenter = new UserEditPresenter(this, getArguments(), getLoaderManager(), getActivity());
        }

        etFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ilFirstName.setErrorEnabled(false);
            }
        });
        etLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ilLastName.setErrorEnabled(false);
            }
        });
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ilEmail.setErrorEnabled(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        presenter = null;
    }

    @Override
    public User getUserObject() {
        User user = new User();
        user.setFirstName(etFirstName.getText().toString());
        user.setLastName(etLastName.getText().toString());
        user.setEmail(etEmail.getText().toString());
        return user;
    }

    @Override
    public void showProgress(boolean state) {

    }

    @Override
    public void restoreUserData(@NonNull User user) {
        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmail.setText(user.getEmail());
    }

    @Override
    public boolean validateUserData() {
        boolean valid = true;
        valid = validateFirstName() & validateLastName() & validateEmail();
        return valid;
    }

    private boolean validateFirstName() {
        String name = etFirstName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ilFirstName.setErrorEnabled(true);
            ilFirstName.setError(getString(R.string.error_empty_field));
            return false;
        } else {
            ilFirstName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateLastName() {
        String name = etLastName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ilLastName.setErrorEnabled(true);
            ilLastName.setError(getString(R.string.error_empty_field));
            return false;
        } else {
            ilLastName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String email = etEmail.getText().toString();
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (TextUtils.isEmpty(email)) {
            ilEmail.setErrorEnabled(true);
            ilEmail.setError(getString(R.string.error_empty_field));
            return false;
        } if (!matcher.matches()) {
            ilEmail.setErrorEnabled(true);
            ilEmail.setError(getString(R.string.error_format));
            return false;
        } else {
            ilEmail.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                break;
            case R.id.send:
                presenter.updateOrCreateUser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Enum types actions for create or update users
     */
    enum Mode {
        CREATE,
        UPDATE
    }
}
