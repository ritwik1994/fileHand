package com.example.root.recyclertutorial;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.root.recyclertutorial.data.DataContract;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class LogIn extends AppCompatActivity {

    public TextInputLayout passLayout, emailLayout;
    public EditText passEdit, emailEdit;
    public TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_screen);

        passLayout = (TextInputLayout)findViewById(R.id.emailLayout);
        emailLayout = (TextInputLayout) findViewById(R.id.passLayout);
        passEdit = (EditText)findViewById(R.id.password);
        emailEdit = (EditText)findViewById(R.id.email);
        login = (TextView)findViewById(R.id.uploadFromMobile);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptAdd();
            }
        });
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void AttemptAdd() {

        boolean cancel = false;
        String email = emailEdit.getText().toString();
        String password = passEdit.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Please fill this field");
            requestFocus(emailEdit);
            cancel = true;
        }else
        {
            emailLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(password)) {
            passLayout.setError("Please fill this field");
            requestFocus(passEdit);
            cancel = true;
        }else
        {
            passLayout.setErrorEnabled(false);
        }

        if (cancel) {
        } else {
            Login login = new Login();
            login.execute();
        }

    }

    public class Login extends AsyncTask<String, Void, Void> {
        BufferedReader reader;
        HttpURLConnection urlConnection;
        Boolean postexec = false;
        String valueError="";
        public String emailValue, passValue;

        @Override
        protected void onPreExecute() {
            emailValue =  emailEdit.getText().toString();
            passValue =  passEdit.getText().toString();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                URL url = new URL("http://192.168.1.102:8000/login/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                JSONObject data = new JSONObject();
                data.put("username", emailValue);
                data.put("password", passValue);
                wr.write(data.toString());
                wr.flush();
                wr.close();

                StringBuilder sb = new StringBuilder();
                int responseCode = urlConnection.getResponseCode();
                InputStream _is;
                if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                }
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                valueError = sb.toString();
                return null;
            } catch (Exception e) {
                System.out.println(e);
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

        }

    }

}
