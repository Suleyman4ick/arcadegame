package com.example.arcadegame.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcadegame.R;
import com.example.arcadegame.Skin;

import java.util.List;

public class SkinAdapter extends RecyclerView.Adapter<SkinAdapter.SkinViewHolder> {
    private List<Skin> skinList;
    private OnSkinClickListener listener;
    private Skin selectedSkin;

    public SkinAdapter(List<Skin> skinList, OnSkinClickListener listener) {
        this.skinList = skinList;
        this.listener = listener;
    }

    public interface OnSkinClickListener {
        void onSkinClick(Skin skin);
    }

    public void setSelectedSkin(Skin skin) {
        selectedSkin = skin;
        notifyDataSetChanged();
    }

    public Skin getSelectedSkin() {
        return selectedSkin;
    }

    @NonNull
    @Override
    public SkinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skin_item, parent, false);
        return new SkinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkinViewHolder holder, int position) {
        Skin skin = skinList.get(position);
        holder.bind(skin);
    }

    @Override
    public int getItemCount() {
        return skinList.size();
    }

    public class SkinViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView skinImageView;
        private TextView skinNameTextView;

        public SkinViewHolder(@NonNull View itemView) {
            super(itemView);
            skinImageView = itemView.findViewById(R.id.skinImageView);
            skinNameTextView = itemView.findViewById(R.id.skinNameTextView);

            itemView.setOnClickListener(this);
        }

        public void bind(Skin skin) {
            skinImageView.setImageResource(skin.getImageResId());
            skinNameTextView.setText(skin.getName());

            if (skin == selectedSkin) {
                // Установите черную рамку для выбранного скина
                itemView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.border_black));
            } else {
                // Очистите фон для невыбранных элементов
                itemView.setBackground(null);
            }

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Skin skin = skinList.get(position);
                setSelectedSkin(skin);
                listener.onSkinClick(skin);
            }
        }
    }
}
