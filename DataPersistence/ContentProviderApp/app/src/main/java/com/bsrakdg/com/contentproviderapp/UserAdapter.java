package com.bsrakdg.com.contentproviderapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by bakdag on 9.02.2018.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserVH> {
    Context context;
    List<User> userList;

    UserAdapter(Context context, List<User> userList){
        this.context = context;
        this.userList  =userList;
    }

    @Override
    public UserVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_item, parent, false);
        return new UserAdapter.UserVH(itemView);

    }

    @Override
    public void onBindViewHolder(UserVH holder, int position) {
        holder.txtName.setText(userList.get(position).getName());
        holder.txtPhone.setText(userList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserVH extends RecyclerView.ViewHolder{
        TextView txtName, txtPhone;

        public UserVH(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtPhone = (TextView) itemView.findViewById(R.id.txtPhone);
        }
    }
}
