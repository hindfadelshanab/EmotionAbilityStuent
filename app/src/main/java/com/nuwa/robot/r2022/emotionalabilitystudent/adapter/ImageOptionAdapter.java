package com.nuwa.robot.r2022.emotionalabilitystudent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nuwa.robot.r2022.emotionalabilitystudent.R;
import com.nuwa.robot.r2022.emotionalabilitystudent.databinding.ImageOptionItemBinding;
import com.nuwa.robot.r2022.emotionalabilitystudent.listener.OnImageOptionSelectedListener;
import com.nuwa.robot.r2022.emotionalabilitystudent.model.ImageOption;

import java.util.List;

public class ImageOptionAdapter extends RecyclerView.Adapter<ImageOptionAdapter.ViewHolder> {

    private Context context;
    List<ImageOption> imageOptions;

    private OnImageOptionSelectedListener onImageOptionSelectedListener;

    public ImageOptionAdapter(Context context, List<ImageOption> imageOptions, OnImageOptionSelectedListener onImageOptionSelectedListener) {
        this.context = context;
        this.imageOptions = imageOptions;
        this.onImageOptionSelectedListener = onImageOptionSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ImageOptionItemBinding binding = ImageOptionItemBinding
                .inflate(LayoutInflater.from(context), viewGroup, false);
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ImageOption imageOption = imageOptions.get(i);


//       if (imageOption.getDescription()!=null){
//           viewHolder.binding.txtImageOptionDesc.setText(imageOption.getDescription());
//       }

        if (imageOption.getImage().equals("AngryFace")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.angry_emotion);
        } else if (imageOption.getImage().equals("SadFace")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.sad_emotion);

        } else if (imageOption.getImage().equals("HappyBoy")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.happy_boy);
        } else if (imageOption.getImage().equals("SadBoy")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.sad_boy);

        } else if (imageOption.getImage().equals("AngryBoy")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.angry_boy);

        } else if (imageOption.getImage().equals("ScaredBoy")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.sacared_boy);

        } else if (imageOption.getImage().equals("largeCircle")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.large_circle);

        } else if (imageOption.getImage().equals("mediumCircle")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.medium_circle);

        } else if (imageOption.getImage().equals("smallCircle")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.small_circle);

        } else if (imageOption.getImage().equals("pinkRectangle")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.pink_rectangel);

        } else if (imageOption.getImage().equals("orangeRectangle")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.orange_rectangle);

        } else if (imageOption.getImage().equals("lightBlueRectangle")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.light_blue_rectangle);

        } else if (imageOption.getImage().equals("darkBlueRectangle")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.dark_blue_rectangle);

        }else if (imageOption.getImage().equals("basketball")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.basketball);

        }else if (imageOption.getImage().equals("football")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.football);

        }else if (imageOption.getImage().equals("redBalloon")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.red_ballon);

        }else if (imageOption.getImage().equals("blueBalloon")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.blue_ballon);

        }else if (imageOption.getImage().equals("purpleIceCream")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.purple_ice_cream);

        }else if (imageOption.getImage().equals("blueIceCream")) {
            viewHolder.binding.imageOption.setImageResource(R.drawable.blue_ice_cream);

        }

        viewHolder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onImageOptionSelectedListener.OnImageOptionSelected(imageOption);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageOptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageOptionItemBinding binding;

        public ViewHolder(@NonNull ImageOptionItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

        }
    }
}
