package com.lotr.demo.userlist.ui.users;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lotr.demo.userlist.R;
import com.lotr.demo.userlist.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.Holder> {

    private List<User> data = new ArrayList<>();
    private UsersListFragment.ItemSelectedListener selectedListener;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.users_list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User user = data.get(position);
        holder.bindView(user);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void setData(List<User> data) {
        this.data.clear();
        this.data.addAll(data);
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
                    selectedListener.onSelected(data.get(position));
                    break;
            }
        }

        void bindView(User user) {
            String fullName = user.getLastName() + " " + user.getFirstName();
            tvName.setText(fullName);

            String email = user.getEmail();
            if (!TextUtils.isEmpty(email)) {
                tvEmail.setText(email);
            }

            String avatarUrl = user.getAvatarUrl();
            if (!TextUtils.isEmpty(avatarUrl)) {
                Picasso.with(tvName.getContext()).load(avatarUrl).into(ivAvatar);
            } else {
                ivAvatar.setBackgroundResource(R.drawable.ic_android);
            }
        }
    }
}
