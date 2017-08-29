package com.lotr.demo.userlist.ui.users;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import com.lotr.demo.userlist.BuildConfig;
import com.lotr.demo.userlist.model.User;
import com.lotr.demo.userlist.net.APIService;
import com.lotr.demo.userlist.net.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

import static com.lotr.demo.userlist.utils.LogUtils.makeLogTag;

/**
 * Presenter for {@link UsersListFragment}
 */
class UsersListPresenter implements LoaderManager.LoaderCallbacks<List<User>> {

    private IUsersListView view;
    private LoaderManager loaderManager;
    private Context context;

    private static final int LOADER_ID = 1;
    private static final String LOG_TAG = makeLogTag(UsersListPresenter.class);

    UsersListPresenter(IUsersListView view, LoaderManager loaderManager, Context context) {
        this.view = view;
        this.loaderManager = loaderManager;
        this.context = context;
        loadData();
    }

    void onDestroy() {
        view = null;
        context = null;
        loaderManager.destroyLoader(LOADER_ID);
    }

    void onRefresh() {
        loadData();
    }

    private void loadData() {
        loaderManager.restartLoader(LOADER_ID, null, this);
        view.showProgress(true);
        view.showList(false);
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return new UsersListLoader(context);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> data) {
        view.setData(data);
        view.showProgress(false);
        view.showList(true);
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {

    }

    private static class UsersListLoader extends AsyncTaskLoader<List<User>> {

        UsersListLoader(Context context) {
            super(context);
        }

        @Override
        public List<User> loadInBackground() {
            if (BuildConfig.DEBUG) Log.d(LOG_TAG, "loading started");

            List<User> data;
            Retrofit retrofit = RestClient.getInstance();
            APIService api = retrofit.create(APIService.class);

            try {
                data = api.getUsers().execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                data = new ArrayList<>();
            }
            return data;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }
    }
}
