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
import com.example.root.recyclertutorial.data.DatabaseHelper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Luther on 5/23/18.
 */

public class VerifyOtp extends AppCompatActivity {

    public TextInputLayout otpLayout;
    private DatabaseHelper mDataHelper;
    public EditText otpEdit;
    public TextView verifyOtp;
    String candidatePK="", hrPK="", hrEmail="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.otp_layout);
        mDataHelper = DatabaseHelper.getInstance(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("CandidatePK"))
                candidatePK = extras.getString("CandidatePK");
            if (extras.containsKey("HrPK"))
                hrPK = extras.getString("HrPK");
            if (extras.containsKey("EmailId"))
                hrEmail = extras.getString("EmailId");
        }

        otpLayout = (TextInputLayout) findViewById(R.id.otpLayout);
        otpEdit = (EditText) findViewById(R.id.otp);
        verifyOtp = (TextView) findViewById(R.id.verifyOtp);

        verifyOtp.setOnClickListener(new View.OnClickListener() {
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
        String otpVal = otpEdit.getText().toString();

        if (TextUtils.isEmpty(otpVal)) {
            otpLayout.setError("Please fill this field");
            requestFocus(otpEdit);
            cancel = true;
        }else
        {
            otpLayout.setErrorEnabled(false);
        }

        if (cancel) {
        } else {
            VerifyingOtp verifyingOtp = new VerifyingOtp();
            verifyingOtp.execute();
        }

    }

    public class VerifyingOtp extends AsyncTask<String, Void, Void> {
        BufferedReader reader;
        HttpURLConnection urlConnection;
        Boolean postexec = false;
        String valueError="";
        public String otpValue;

        @Override
        protected void onPreExecute() {
            otpValue =  otpEdit.getText().toString();
        }

        public void addTODatabase(String value){
            try {
//                String companyName="", username="", email="", id="";
                JSONObject jsonValue = new JSONObject(value);
                if(jsonValue.has("otp_valid")){
                    if(jsonValue.getString("otp_valid").equals(true)){
                        ContentValues cv = new ContentValues();
                        SQLiteDatabase db = mDataHelper.getWritableDatabase();
                        cv.put(DataContract.LoginDetails.email_confirmed, "true");
                        int val = (int)db.updateWithOnConflict(DataContract.LoginDetails.TABLE_NAME, cv, DataContract.LoginDetails.USER_ID + "=" + "'" + hrPK + "'", null, SQLiteDatabase.CONFLICT_IGNORE);
                        System.out.println(val);
                        ContentValues cvForCand = new ContentValues();
                        cvForCand.put(DataContract.CandidateDetails.TO_SAVE_IN_DATABASE, "true");
                        int valToCHeck = (int)db.updateWithOnConflict(DataContract.CandidateDetails.TABLE_NAME, cvForCand, DataContract.CandidateDetails.CANDIDATE_ID + "=" + "'" + candidatePK + "'", null, SQLiteDatabase.CONFLICT_IGNORE);
                        System.out.println(valToCHeck);
                    }
                }

            }catch(Exception e){

            }

        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                URL url = new URL("http://192.168.1.102:8000/auth/verify-signup-otp/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                JSONObject data = new JSONObject();
                data.put("Candidatepk", candidatePK);
                data.put("Hrpk", hrPK);
                data.put("otp", otpValue);
                data.put("email",hrEmail);
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
                addTODatabase(valueError);
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
