package com.lotr.demo.userlist.ui.users;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.lotr.demo.userlist.BuildConfig;
import com.lotr.demo.userlist.R;
import com.lotr.demo.userlist.model.User;
import com.lotr.demo.userlist.net.APIService;
import com.lotr.demo.userlist.net.Config;
import com.lotr.demo.userlist.net.RestClient;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;

import static com.lotr.demo.userlist.ui.users.UserEditFragment.KEY_MODE;
import static com.lotr.demo.userlist.ui.users.UserEditFragment.KEY_USER;
import static com.lotr.demo.userlist.utils.Helpers.showEasyDialog;
import static com.lotr.demo.userlist.utils.LogUtils.makeLogTag;

/**
 * Presenter for {@link UserEditFragment}
 */
class UserEditPresenter implements LoaderManager.LoaderCallbacks<Integer> {

    private IUserEditView view;
    private UserEditFragment.Mode mode;
    private Bundle args;
    private Context context;
    private LoaderManager loaderManager;

    private User user;

    private static final int LOADER_ID = 1;
    private static final String LOG_TAG = makeLogTag(UserEditPresenter.class);

    UserEditPresenter(IUserEditView view, Bundle args, LoaderManager loaderManager, Context context) {
        this.context = context;
        this.view = view;
        this.args = args;
        this.loaderManager = loaderManager;
        this.mode = (UserEditFragment.Mode) args.get(UserEditFragment.KEY_MODE);
        this.user = this.args.getParcelable(KEY_USER);
        restoreUsertData();
    }

    /**
     * Restores field values for mode {@link UserEditFragment.Mode#UPDATE}
     */
    private void restoreUsertData() {
        if (mode == UserEditFragment.Mode.UPDATE) {
            view.restoreUserData(user);
        }
    }

    /**
     * Create or update user data for current {@link UserEditFragment.Mode}
     */
    void updateOrCreateUser() {
        boolean userDataValid = view.validateUserData();
        if (!userDataValid) return;

        Bundle args = new Bundle();
        args.putSerializable(KEY_MODE, mode);

        User userData;
        if (mode == UserEditFragment.Mode.CREATE) {
            userData = view.getUserObject();
        } else {
            // For update user data need userID, if current action mode is UPDATE, update this
            userData = view.getUserObject();
            Integer userId = user.getId();
            userData.setId(userId);
        }
        args.putParcelable(KEY_USER, userData);
        loaderManager.restartLoader(LOADER_ID, args, this);
    }

    private void onRequestComplete(int responseCode) {
        if (mode == UserEditFragment.Mode.CREATE) {
            if (responseCode == Config.CREATE_USER_COMPLETE_CODE) {
                showEasyDialog(context, R.string.msg_user_create, dialogPositiveClickListener);
            } else {
                showEasyDialog(context, R.string.msg_user_create_error, null);
            }

        } else if (mode == UserEditFragment.Mode.UPDATE) {
            if (responseCode == Config.UPDATE_USER_COMPLETE_CODE) {
                showEasyDialog(context, R.string.msg_user_update, dialogPositiveClickListener);
            } else {
                showEasyDialog(context, R.string.msg_user_update_error, null);
            }
        }
    }

    private DialogInterface.OnClickListener dialogPositiveClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            view.finish();
        }
    };

    void onDestroy() {
        view = null;
    }

    @Override
    public Loader<Integer> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return new UserEditLoader(context, args);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, final Integer responseCode) {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                onRequestComplete(responseCode);
            }
        }.sendEmptyMessage(1);
    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {

    }

    /**
     * Update user data or create new user in background thread
     */
    private static class UserEditLoader extends AsyncTaskLoader<Integer> {

        private Bundle args;

        UserEditLoader(Context context, Bundle args) {
            super(context);
            this.args = args;
        }

        @Override
        public Integer loadInBackground() {
            UserEditFragment.Mode mode = (UserEditFragment.Mode) args.get(KEY_MODE);
            User user = args.getParcelable(KEY_USER);
            Integer responseCode = 0;

            if (mode == UserEditFragment.Mode.CREATE) {
                responseCode = createUser(user);
            } else if (mode == UserEditFragment.Mode.UPDATE) {
                responseCode = updateUser(user);
            }
            return responseCode;
        }

        @WorkerThread
        private int createUser(User user) {
            Retrofit retrofit = RestClient.getInstance();
            APIService api = retrofit.create(APIService.class);
            int responseCode = 0;
            try {
                Response response = api.createUser(user).execute();
                responseCode = response.code();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseCode;
        }

        @WorkerThread
        private int updateUser(User user) {
            Retrofit retrofit = RestClient.getInstance();
            APIService api = retrofit.create(APIService.class);
            int responseCode = 0;
            try {
                Response response = api.updateUser(user, user.getId()).execute();
                if (BuildConfig.DEBUG) Log.d(LOG_TAG, "update_user_response" + response);
                responseCode = response.code();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseCode;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }
    }
}
