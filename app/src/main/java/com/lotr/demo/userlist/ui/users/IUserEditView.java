package com.lotr.demo.userlist.ui.users;

import com.lotr.demo.userlist.model.User;

interface IUserEditView {

    void showProgress(boolean state);

    void restoreUserData(User user);

    boolean validateUserData();

    User getUserObject();
}
