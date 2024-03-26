package edu.northeastern.memecho.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.northeastern.memecho.R;
import edu.northeastern.memecho.models.HomeModel;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private List<HomeModel> list;
    private Context context;

    public HomeAdapter(List<HomeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        holder.userName.setText(list.get(position).getUserName());
        holder.timestamp.setText(list.get(position).getTimestamp());

        // like count
        int count = list.get(position).getLikeCount();

        if (count == 0) {
            holder.likeCount.setVisibility(View.INVISIBLE);
        } else if (count == 1){
            holder.likeCount.setText(count + " like");
        } else {
            holder.likeCount.setText(count + " likes");
        }

        holder.timestamp.setText(list.get(position).getTimestamp());

        Random random = new Random();

        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_profile)
                .timeout(6500)
                .into(holder.profileImage);

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getPostImage())
                .placeholder(new ColorDrawable(color))
                .timeout(7000)
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HomeHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView userName, timestamp, likeCount;
        private ImageView imageView;
        private ImageButton likeBtn, commentBtn, shareBtn, starBtn, exclamationBtn;
        public HomeHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            imageView = itemView.findViewById(R.id.imageView);
            userName = itemView.findViewById(R.id.textName);
            timestamp = itemView.findViewById(R.id.textTime);
            likeCount = itemView.findViewById(R.id.textLikeCount);
            likeBtn = itemView.findViewById(R.id.likeButton);
            commentBtn = itemView.findViewById(R.id.commentButton);
            shareBtn = itemView.findViewById(R.id.repostButton);
            starBtn = itemView.findViewById(R.id.starButton);
            exclamationBtn = itemView.findViewById(R.id.exclamationMarkButton);
        }
    }
}
