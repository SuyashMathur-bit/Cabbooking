package com.example.cabbooking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {

    private Context context;
    private List<Booking> bookings;

    public BookingHistoryAdapter(Context context, List<Booking> bookings) {
        this.context = context;
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        holder.tvPickup.setText("Pickup: " + booking.getPickup());
        holder.tvDrop.setText("Drop: " + booking.getDrop());
        holder.tvPrice.setText("₹" + booking.getPrice());
        holder.tvDate.setText("Booked on: " + booking.getDate());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPickup, tvDrop, tvPrice, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPickup = itemView.findViewById(R.id.tvPickup);
            tvDrop = itemView.findViewById(R.id.tvDrop);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
