package com.example.root.recyclertutorial;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class Signup extends AppCompatActivity {

    public String HrCompanyName, DateOfJoining, CandidateName, CandidateEmail, CandidateComapanyName, natureOfAbuse;
    public TextInputLayout HrEmailInput, PasswordInput, ConfirmPasswordInput;
    public EditText HrEmailEdit, PassEdit,ConfirmPassEdit;
    public TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.signup_screen);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("hrCompanyName"))
                HrCompanyName = extras.getString("hrCompanyName");
            if (extras.containsKey("DOJ"))
                DateOfJoining = extras.getString("DOJ");
            if (extras.containsKey("nameCandidate"))
                CandidateName = extras.getString("nameCandidate");
            if (extras.containsKey("emailCandidate"))
                CandidateEmail = extras.getString("emailCandidate");
            if (extras.containsKey("companyCandidate"))
                CandidateComapanyName = extras.getString("companyCandidate");
            if (extras.containsKey("nature"))
                natureOfAbuse = extras.getString("nature");
        }

        HrEmailInput = (TextInputLayout)findViewById(R.id.emailAddress);
        PasswordInput = (TextInputLayout)findViewById(R.id.password);
        ConfirmPasswordInput = (TextInputLayout)findViewById(R.id.confirmPassword);
        HrEmailEdit = (EditText) findViewById(R.id.emailAddressText);
        PassEdit = (EditText)findViewById(R.id.passwordText);
        ConfirmPassEdit = (EditText)findViewById(R.id.confirmPasswordText);
        signUp = (TextView) findViewById(R.id.signup);

        signUp.setOnClickListener(new View.OnClickListener() {
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
        String HrEMailData = HrEmailEdit.getText().toString();
        String PAssText = PassEdit.getText().toString();
        String confirmPassText = ConfirmPassEdit.getText().toString();

        if (TextUtils.isEmpty(PAssText)) {
            PasswordInput.setError("Please fill this field");
            requestFocus(PassEdit);
            cancel = true;
        }else
        {
            PasswordInput.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(confirmPassText)) {
            ConfirmPasswordInput.setError("Please fill this field");
            requestFocus(ConfirmPassEdit);
            cancel = true;
        }
        else if(!(PAssText.equals(confirmPassText))){
            ConfirmPasswordInput.setError("Password Not Matching");
            requestFocus(ConfirmPassEdit);
            cancel = true;
        }
        else
        {
            ConfirmPasswordInput.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(HrEMailData)) {
            HrEmailInput.setError("Please fill this field");
            requestFocus(HrEmailEdit);
            cancel = true;
        }else
        {
            HrEmailInput.setErrorEnabled(false);
        }

        if (cancel) {
        } else {
            CreateNewUser createNewUser = new CreateNewUser();
            createNewUser.execute();
        }

    }

    public class CreateNewUser extends AsyncTask<String, Void, Void> {
        BufferedReader reader;
        HttpURLConnection urlConnection;
        Boolean postexec = false;
        String valueError="";
        public String HrEMailData,PAssText;

        @Override
        protected void onPreExecute() {
            HrEMailData =  HrEmailEdit.getText().toString();
            PAssText = PassEdit.getText().toString();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                URL url = new URL("http://127.0.0.1:8000/hr/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                JSONObject userData = new JSONObject();
                userData.put("username",HrEMailData);
                userData.put("email",HrEMailData);
                userData.put("password",PAssText);
                JSONObject data = new JSONObject();
                data.put("company_name", HrEMailData);
                data.put("email_confirmed", false);
                data.put("user", userData);
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
                if(responseCode == 200 || responseCode == 201 || responseCode == 500) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    valueError = sb.toString();

                    return null;
                }
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
