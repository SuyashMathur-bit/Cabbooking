package com.example.cabbooking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookingHistory extends AppCompatActivity {
    RecyclerView recyclerBookingHistory;
    BookingHistoryAdapter adapter;
    List<Booking> list = new ArrayList<>();
    private static final String URL_CAB_HISTORY = "http://10.0.2.2/cabbooking1/cab_history2.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_history);
        recyclerBookingHistory = findViewById(R.id.recyclerBookingHistory);
        recyclerBookingHistory.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookingHistoryAdapter(this, list);
        recyclerBookingHistory.setAdapter(adapter);


        fetchBookingHistory("123"); // Replace "123" with the logged-in user's ID
    }

    private void fetchBookingHistory(String userId) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_CAB_HISTORY,
                response -> {
                    Log.d("BookingHistoryResponse", response);

                    list.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String pickup = obj.getString("pickup_location");
                            String drop = obj.getString("drop_location");
                            String price = obj.getString("price");
                            String dateRaw = obj.getString("date_time"); // Assuming your PHP also sends date
                            String formattedDate = formatDate(dateRaw);

                            list.add(new Booking(pickup, drop, price, formattedDate));
                        }
                        adapter.notifyDataSetChanged();
                        if (list.isEmpty()) {
                            Toast.makeText(this, "No bookings found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Parsing Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                return params;
            }
        };

        // Add request to Volley queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private String formatDate(String rawDate) {
        try {

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.getDefault());
            Date date = inputFormat.parse(rawDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            return rawDate;
        }

    }
}