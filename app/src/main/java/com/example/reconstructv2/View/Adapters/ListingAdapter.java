package com.example.reconstructv2.View.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reconstructv2.Models.Listing;
import com.example.reconstructv2.R;

import java.util.ArrayList;
import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingHolder> {
    private List<Listing> listings = new ArrayList<>();
    private OnClickListener listener;

    @NonNull
    @Override
    public ListingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Creates and returns a ListingHolder, the layout we use in the recycler view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listing_card, parent, false);
        return new ListingHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ListingHolder holder, int position) {
        // puts data from listing object into ListingHolder views
        Listing currListing = listings.get(position);
        holder.textViewTitle.setText(currListing.getTitle());
        holder.textViewBody.setText(currListing.getBody());
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }


    public void setListings(List<Listing> listings) {
        this.listings = listings;
        notifyDataSetChanged();
    }

    class ListingHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewBody;
        private ImageView listingImage;

        public ListingHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewBody = itemView.findViewById(R.id.text_view_body);
            listingImage = itemView.findViewById(R.id.imageViewListing);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(listings.get(pos));
                    }
                }
            });
        }
    }

    public interface OnClickListener {
        void onItemClick(Listing listing);
    }

    public void setOnItemClickListener(OnClickListener listener) {
        this.listener = listener;
    }
}