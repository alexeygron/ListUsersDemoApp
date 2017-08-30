package com.lotr.demo.userlist.ui.users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lotr.demo.userlist.R;
import com.lotr.demo.userlist.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.Holder> {

    private List<User> userList;
    private UsersListFragment.ItemSelectedListener selectedListener;

    UsersListAdapter() {
        userList = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.users_list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Context context = holder.tvName.getContext();
        User user = userList.get(position);

        String fullName = user.getLastName() + " " + user.getFirstName();
        holder.tvName.setText(fullName);

        String email = user.getEmail();
        if (!TextUtils.isEmpty(email)) {
            holder.tvEmail.setText(email);
        }

        String avatarUrl = user.getAvatarUrl();
        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(context).load(avatarUrl).into(holder.ivAvatar);
        } else {
            holder.ivAvatar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_android));
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    void setUserList(List<User> userList) {
        this.userList.clear();
        this.userList.addAll(userList);
        notifyDataSetChanged();
    }

    void setSelectedListener(UsersListFragment.ItemSelectedListener listener) {
        this.selectedListener = listener;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvEmail) TextView tvEmail;
        @BindView(R.id.ivAvatar) ImageView ivAvatar;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            switch (v.getId()) {
                case R.id.cvContainer:
                    selectedListener.onSelected(userList.get(position));
                    break;
            }
        }
    }
}
