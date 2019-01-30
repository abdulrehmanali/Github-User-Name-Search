package com.abdulcodes.githubusernamesearch;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private List<User> usersList;

    public UsersAdapter(List<User> usersList) {
        this.usersList = usersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_users_list_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = usersList.get(parent.indexOfChild(itemView));
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(user.getHtml_url()));
                view.getContext().startActivity(browserIntent);
            }
        });
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        User user = usersList.get(position);
        Picasso.get().load(user.getAvatar_url()).into(viewHolder.user_avatar);
        viewHolder.user_login.setText(user.getLogin());
        viewHolder.user_score.setText(String.valueOf(user.getScore()));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_login, user_score;
        public ImageView user_avatar;

        public MyViewHolder(View view) {
            super(view);
            user_avatar = view.findViewById(R.id.user_avatar);
            user_login = view.findViewById(R.id.user_login);
            user_score = view.findViewById(R.id.user_score);
        }
    }
}
