package com.aedeo.tugas11registrasi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public EditText etRegisterName, etRegisterUsername, etRegisterPassword, etLoginUsername, etLoginPassword;
    public Button getbtnRegister, getbtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DEKLARASI ELEMENT XML MAIN ACTIVITY
//        EDITTEXT
        etRegisterName = (EditText) findViewById(R.id.etRegisterName);
        etRegisterUsername = (EditText) findViewById(R.id.etRegisterUsername);
        etRegisterPassword = (EditText) findViewById(R.id.etRegisterPassword);
        etLoginUsername = (EditText) findViewById(R.id.etLoginUsername);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
//        BUTTON
        getbtnLogin = (Button) findViewById(R.id.btnLogin);
        getbtnRegister = (Button) findViewById(R.id.btnRegister);

        registerProgress();
        loginProgress();
    }

    private void registerProgress() {
        getbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etRegisterUsername.getText().toString();
                String password = etRegisterPassword.getText().toString();
                String name = etRegisterName.getText().toString();
                if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Nama lengkap, Username, dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (name.length() > 35 || password.length() > 8 || username.length() > 12) {
                    Toast.makeText(MainActivity.this, "Nama lengkap, Username, dan Password melebihi panjang yang ditentukan", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "http://192.168.1.11/project/mobile/tugas11/register.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(MainActivity.this, "Berhasil Register", Toast.LENGTH_LONG).show();
//                            Log.d("INI RESPONSE", "onResponse: "+response.trim());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                            Log.d("INI ERROR", "onErrorResponse: " + error.getMessage());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", name);
                            params.put("username", username);
                            params.put("password", password);

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);
                }

                etRegisterName.setText("");
                etRegisterUsername.setText("");
                etRegisterPassword.setText("");
            }
        });
    }

    private void loginProgress() {
        getbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (username.length() > 12 || password.length() > 8) {
                    Toast.makeText(MainActivity.this, "Username dan Password melebihi panjang yang ditentukan", Toast.LENGTH_SHORT).show();
                } else {
//                    AsyncLogin asyncLogin = new AsyncLogin(getApplicationContext());
//                    String postdata = "username=" + username + "&password=" + password;
//                    asyncLogin.execute(postdata);
                    String url = "http://192.168.1.11/project/mobile/tugas11/login.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String state = jsonObject.getString("statusUser");
                                String status = "";
                                if (state.equalsIgnoreCase("Ada")) {
                                    status = "Berhasil Login";
                                } else if (state.equalsIgnoreCase("Kosong")) {
                                    status = "Gagal Login: Username atau password salah";
                                }
                                Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = error.toString() + "111";
//                            if (state == "Kosong") {
//                                String status = "Gagal Login: Username atau password salah";
//                            }
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", username);
                            params.put("password", password);

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);
                }
                etLoginUsername.setText("");
                etLoginPassword.setText("");
            }
        });
    }
}