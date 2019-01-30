package com.abdulcodes.githubusernamesearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText username_to_search;
    TextView search_status;
    Button search_username;
    RecyclerView searched_users;
    UsersAdapter usersAdapter;
    List<User> usersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username_to_search = findViewById(R.id.username_to_search);
        search_username = findViewById(R.id.search_username);
        searched_users = findViewById(R.id.searched_users);
        search_status = findViewById(R.id.search_status);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        searched_users.setLayoutManager(mLayoutManager);
        searched_users.setItemAnimator(new DefaultItemAnimator());
        usersAdapter = new UsersAdapter(usersList);
        searched_users.setAdapter(usersAdapter);

        search_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perform_search_for_users();
            }
        });
    }

    public void perform_search_for_users() {
        usersList.clear();
        search_status.setText(getResources().getText(R.string.searching));
        String username = username_to_search.getText().toString();
        if (username.equals("")) {
            return;
        }
        String search_url = "https://api.github.com/search/users?q=" + username;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, search_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject converted_response = new JSONObject(response);
                            search_status.setText(converted_response.getInt("total_count") +" "+ getResources().getString(R.string.results_found));
                            JSONArray items = converted_response.getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject item = items.getJSONObject(i);
                                User user = new User();
                                user.setId(item.getInt("id"));
                                user.setLogin(item.getString("login"));
                                user.setScore(item.getDouble("score"));
                                user.setHtml_url(item.getString("html_url"));
                                user.setAvatar_url(item.getString("avatar_url"));
                                usersList.add(user);
                            }
                            usersAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("ResponseJSONException", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                search_status.setText(getResources().getString(R.string.results_found));
            }
        });
        queue.add(stringRequest);
    }
}
