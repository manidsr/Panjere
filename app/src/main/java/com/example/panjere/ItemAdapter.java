package com.example.panjere;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;
    private Context context;

    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.nameTextView.setText(item.getName());
        holder.descriptionTextView.setText(item.getDescription());
        holder.priceTextView.setText("Price: $" + String.format("%.2f", item.getPrice()));  // Format the price

        // Decode Base64 image and set it to ImageView
        if (item.getImageBase64() != null && !item.getImageBase64().isEmpty()) {
            byte[] decodedString = Base64.decode(item.getImageBase64(), Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.itemImageView.setImageBitmap(decodedBitmap);
        } else {
            holder.itemImageView.setImageResource(R.drawable.ic_placeholder); // Use a placeholder image if no base64 data is available
        }

        // Handle item click event
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("itemId", item.getId());
            intent.putExtra("itemName", item.getName());
            intent.putExtra("itemDescription", item.getDescription());
            intent.putExtra("itemPrice", item.getPrice());
            intent.putExtra("itemUserId", item.getUserId());
            intent.putExtra("itemImageBase64", item.getImageBase64());
            intent.putExtra("categoryName", item.getCategoryName());
            intent.putExtra("categoryId", item.getCategoryId());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImageView;
        TextView nameTextView, descriptionTextView, priceTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.itemImage);
            nameTextView = itemView.findViewById(R.id.itemName);
            descriptionTextView = itemView.findViewById(R.id.itemDescription);
            priceTextView = itemView.findViewById(R.id.itemPrice);
        }
    }
}
