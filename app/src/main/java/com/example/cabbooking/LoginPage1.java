package com.example.cabbooking;

import static android.widget.Toast.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import java.util.HashMap;
import java.util.Map;

public class LoginPage1 extends AppCompatActivity {
    EditText Username, Password;
    Button Login;
    private static final String Key_title = "title";
    public static final String Key_thought = "thought";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docref = db.collection("LoginData").document("Persondata");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page1);
        Username = findViewById(R.id.etUsername);
        Password = findViewById(R.id.etPassword);
        Login = findViewById(R.id.btnLogin);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = Username.getText().toString().trim();
                String password = Password.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginPage1.this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = "http://10.0.2.2/cabbooking1/login_page1.php"; // Use 10.0.2.2 for localhost in emulator

                StringRequest request = new StringRequest(Request.Method.POST, url,
                        response -> {
                            Log.d("VolleyResponse", "Response: '" + response + "'");
                            if (response != null && response.trim().equals("success")) {
                                Toast.makeText(LoginPage1.this, "Login saved successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginPage1.this, MapsActivity.class));
                            } else {
                                Toast.makeText(LoginPage1.this, "Failed to save login: " + response, Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> Toast.makeText(LoginPage1.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                )
                        {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", password);
                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(LoginPage1.this);
                queue.add(request);

            }
        });
    }
}