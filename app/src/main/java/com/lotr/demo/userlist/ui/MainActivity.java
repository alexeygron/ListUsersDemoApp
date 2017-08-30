package com.lotr.demo.userlist.ui;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lotr.demo.userlist.R;
import com.lotr.demo.userlist.ui.users.UsersListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment frag = new UsersListFragment();
        getFragmentManager().beginTransaction().replace(R.id.primary_container, frag).commit();
    }
}
