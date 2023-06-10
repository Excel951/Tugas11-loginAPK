package com.aedeo.tugas11registrasi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncLogin extends AsyncTask<String, Integer, String> {

    String login = "";
    Context context;
    public AsyncLogin(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String data = "";
        String urlString = strings[0];

        try {
            data = postData("http://192.168.1.11/project/mobile/tugas11/login.php", urlString);
            JSONObject json = new JSONObject(data);
            login = json.getString("statusUser");
            Log.d("INIBACKGROUND", "doInBackground: " + data);
        } catch (Exception e) {
            Log.d("Error Background Task", "doInBackground: " + e);
        }

        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("TES", "onPostExecute: " + login);
//        Toast.makeText(context, login, Toast.LENGTH_SHORT).show();
    }

    private String postData(String urlString, String postedData) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        Toast.makeText(context, postedData, Toast.LENGTH_SHORT).show();

        try {
//        MEMBUAT OBJEK DARI STRING URL
            URL url = new URL(urlString);

//        BUAT OBJEK HTTPURLCONNECTION
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

//            AKTIFKAN OUTPUT UNTUK KIRIM DATA
            connection.setDoOutput(true);

//            ATUR PROPERTI CONTENT-TYPE HEADER
            connection.setRequestProperty("Content-Type", "application/x-wwww-form-urlencoded");

//            KIRIM DATA KE SERVER
            connection.getOutputStream().write(postedData.getBytes());

            inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line = "";

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            data = response.toString();
            reader.close();

        } catch (Exception e) {
            Log.d("POSTDATA", "postData: " + e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return data;
    }

    private String getData(String urlString) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {

            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            data = buffer.toString();
            reader.close();

        } catch (Exception e) {
            Log.d("getData", "getData: " + e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return data;
    }
}
