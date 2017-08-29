package com.lotr.demo.userlist.ui.users;

import com.lotr.demo.userlist.model.User;

import java.util.List;

interface IUsersListView {

    void showProgress(boolean state);

    void showList(boolean state);

    void setData(List<User> data);
}
