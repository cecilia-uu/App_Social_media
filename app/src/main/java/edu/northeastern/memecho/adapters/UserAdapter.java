package edu.northeastern.memecho.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.memecho.R;
import edu.northeastern.memecho.databinding.ItemContainerUserBinding;
import edu.northeastern.memecho.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        ItemContainerUserBinding binding;
        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding) {
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }

        void setUserData(User user) {
            Log.d("UserAdapter", "Setting user data for: " + user.name);
            binding.textName.setText(user.name);
            binding.textEmail.setText(user.email);
            // 检查用户图像字符串是否为空或无效
            if (user.image != null && !user.image.isEmpty()) {
                Bitmap bitmap = getUserImage(user.image);
                if (bitmap != null) {
                    binding.imageProfile.setImageBitmap(bitmap);
                } else {
                    // 设置默认图像或隐藏图像视图
                    binding.imageProfile.setImageResource(R.drawable.ic_launcher_foreground); // 或设置默认图像
                }
            } else {
                // 设置默认图像或隐藏图像视图
                binding.imageProfile.setImageResource(R.drawable.ic_launcher_foreground); // 或设置默认图像
            }
//            binding.imageProfile.setImageBitmap(getUserImage(user.image));
        }
    }
    private Bitmap getUserImage(String encodedImage) {
        try {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (IllegalArgumentException e) {
            Log.e("UserAdapter", "Error decoding Base64 string: " + e.getMessage());
            return null;
        }
    }
}
