package com.lotr.demo.userlist.ui.users;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lotr.demo.userlist.BuildConfig;
import com.lotr.demo.userlist.R;
import com.lotr.demo.userlist.model.User;
import com.lotr.demo.userlist.ui.common.CommonFragment;
import com.lotr.demo.userlist.ui.common.VerticalSpaceItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lotr.demo.userlist.utils.LogUtils.makeLogTag;

/**
 * Show all users list
 */
public class UsersListFragment extends CommonFragment implements IUsersListView, SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = makeLogTag(UsersListFragment.class);

    @BindView(R.id.progressBar) AVLoadingIndicatorView progressBar;
    @BindView(R.id.rvUsersList) RecyclerView rvUsersList;
    @BindView(R.id.btnAdd) FloatingActionButton btnAdd;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout swiperefresh;

    private UsersListPresenter presenter;
    private UsersListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.users_list_fragment, container, false);
        ButterKnife.bind(this, v);
        setUpToolBar(v, R.id.toolbar, R.string.title_users_list);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        swiperefresh.setOnRefreshListener(this);
        if (presenter == null) {
            presenter = new UsersListPresenter(this, getLoaderManager(), getActivity());
        }
    }

    private void initRecyclerView() {
        rvUsersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvUsersList.addItemDecoration(new VerticalSpaceItemDecoration(10));
        listAdapter = new UsersListAdapter();

        ItemSelectedListener listener = new ItemSelectedListener() {
            @Override
            public void onSelected(User user) {
                onItemSelected(user, UserEditFragment.Mode.UPDATE);
            }
        };

        listAdapter.setSelectedListener(listener);
        rvUsersList.setAdapter(listAdapter);
    }

    private void onItemSelected(User user, UserEditFragment.Mode mode) {
        UserEditFragment frag = new UserEditFragment();
        Bundle args = new Bundle();
        args.putSerializable(UserEditFragment.KEY_MODE, mode);
        args.putParcelable(UserEditFragment.KEY_USER, user);
        frag.setArguments(args);

        if (isDualPaneModeEnabled()) {
            enableDualPaneMode(true);
            getFragmentManager().beginTransaction().replace(R.id.secondary_container, frag).addToBackStack(null).commit();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.primary_container, frag).addToBackStack(null).commit();
        }
    }

    @OnClick(R.id.btnAdd)
    public void addUser() {
        onItemSelected(new User(), UserEditFragment.Mode.CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        presenter = null;
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh();
        swiperefresh.setRefreshing(true);
        swiperefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                swiperefresh.setRefreshing(false);
            }
        }, 700);
    }

    @Override
    public void showProgress(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showList(boolean state) {
        rvUsersList.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setData(List<User> data) {
        if (BuildConfig.DEBUG) Log.d(LOG_TAG, "users_count " + data.size());
        listAdapter.setUserList(data);
    }

    interface ItemSelectedListener {
        void onSelected(User user);
    }
}
