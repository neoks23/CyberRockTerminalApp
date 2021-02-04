package com.example.cyberrockterminalapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText linkText = findViewById(R.id.linkText);
        final EditText hexId = findViewById(R.id.idValue);
        final TextView txt = findViewById(R.id.txtOutput);
        Button sendBtn = findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(linkText.getText()) || TextUtils.isEmpty(hexId.getText())){

                    if(TextUtils.isEmpty(linkText.getText())){
                        linkText.setError("Enter Value");
                        linkText.requestFocus();
                    }
                    if(TextUtils.isEmpty(hexId.getText())){
                        hexId.setError("Enter Value");
                        hexId.requestFocus();
                    }

                }
                else if(!URLUtil.isValidUrl(linkText.getText().toString()) || !Patterns.WEB_URL.matcher(linkText.getText().toString()).matches()){
                    linkText.setError("Enter Valid URL");
                    linkText.requestFocus();
                }
                else if(!checkHex(hexId.getText().toString())){
                    hexId.setError("Enter Valid Hexadecimal");
                    hexId.requestFocus();
                }
                else{
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(linkText.getText().toString())
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful()){
                                final String myResponse = response.body().string();

                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "✅", Toast.LENGTH_SHORT).show();
                                        txt.setText(myResponse);
                                        txt.setMovementMethod(new ScrollingMovementMethod());
                                    }
                                });
                            }
                        }
                    });


                }
            }
        });
    }

    public static boolean checkHex(String s){
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char ch = s.charAt(i);
            if ((ch < '0' || ch > '9')
                    && (ch < 'A' || ch > 'F')) {
                return false;
            }
        }
        return true;
    }
}