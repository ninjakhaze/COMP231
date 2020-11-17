package fitness.buddy.comp231.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

import fitness.buddy.comp231.MessageActivity;
import fitness.buddy.comp231.R;
import fitness.buddy.comp231.UsersData;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<UsersData> mUsersData;

    //constructor
    public UserAdapter(Context context, List<UsersData> mUsersData) {
        this.context    = context;
        this.mUsersData = mUsersData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UsersData usersData = mUsersData.get(position);
        holder.username.setText(usersData.getUsername());

        if (usersData.getImageURL().equals("default")) {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }else {
            //adding Glide Library
            Glide.with(context)
                    .load(usersData.getImageURL())
                    .into(holder.imageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("userid", usersData.getUserId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsersData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username    = itemView.findViewById(R.id.textViewUsername);
            imageView   = itemView.findViewById(R.id.imageViewUser);
        }
    }
}
