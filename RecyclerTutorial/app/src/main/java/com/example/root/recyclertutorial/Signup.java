package com.example.root.recyclertutorial;

import android.content.ContentValues;
import android.content.Intent;
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


public class Signup extends AppCompatActivity {

    public String HrCompanyName, DateOfJoining, CandidateName, CandidateEmail, CandidateComapanyName, natureOfAbuse, HrPrimaryKey = "";
    public TextInputLayout HrEmailInput, PasswordInput, ConfirmPasswordInput;
    public EditText HrEmailEdit, PassEdit,ConfirmPassEdit;
    public TextView signUp;
    public String candidatePk="", hrPk = "";
    private DatabaseHelper mDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        mDataHelper = DatabaseHelper.getInstance(this);
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

        public void addTODatabase(String value){
            try {
                String companyName="", username="", email="", id="";
                JSONObject jsonValue = new JSONObject(value);
                if(jsonValue.has("company_name")){
                    companyName = jsonValue.getString("company_name");
                }
                if(jsonValue.has("user")){
                    JSONObject userJson = new JSONObject(jsonValue.getString("user"));
                    if(userJson.has("username")){
                        username = userJson.getString("username");
                    }
                    if(userJson.has("email")){
                        email = userJson.getString("email");
                    }
                }
                if(jsonValue.has("id")){
                    id = jsonValue.getString("id");
                    HrPrimaryKey = id;
                }
                SQLiteDatabase db = mDataHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(DataContract.LoginDetails.USER_ID, id);
                cv.put(DataContract.LoginDetails.USER_NAME, username);
                cv.put(DataContract.LoginDetails.EMAIL, email);
                cv.put(DataContract.LoginDetails.COMPANY_NAME, companyName);
                cv.put(DataContract.LoginDetails.email_confirmed, "false");
                int dataInserted=(int)db.insertWithOnConflict(DataContract.LoginDetails.TABLE_NAME, null, cv,SQLiteDatabase.CONFLICT_REPLACE);

            }catch(Exception e){

            }

        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                URL url = new URL("http://192.168.1.102:8000/hr/");
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
                data.put("company_name", HrCompanyName);
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
            CreateNewCandidate createNewCandidate = new CreateNewCandidate();
            createNewCandidate.execute();
        }

    }

    public class CreateNewCandidate extends AsyncTask<String, Void, Void> {
        BufferedReader reader;
        HttpURLConnection urlConnection;
        Boolean postexec = false;
        String valueError="";

        public void storeInDataBase(String response){
            try{
                String nameCand="",candidateOrganisation="",emailCand="",date_joining="",reportAbusedBy="",nature="",id="";
                JSONObject responseObject = new JSONObject(response);
                if(responseObject.has("name")){
                    nameCand = responseObject.getString("name");
                }
                if(responseObject.has("current_organization")){
                    candidateOrganisation = responseObject.getString("current_organization");
                }
                if(responseObject.has("email")){
                    emailCand = responseObject.getString("email");
                }
                if(responseObject.has("date_of_joining")){
                    date_joining = responseObject.getString("date_of_joining");
                }
                if(responseObject.has("report_abused_by")){
                    reportAbusedBy = responseObject.getString("report_abused_by");
                }
                if(responseObject.has("nature_of_abuse")){
                    nature = responseObject.getString("nature_of_abuse");
                }
                if(responseObject.has("id")){
                    id = responseObject.getString("id");
                    candidatePk = id;
                }
                SQLiteDatabase db = mDataHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(DataContract.CandidateDetails.CANDIDATE_ID, id);
                cv.put(DataContract.CandidateDetails.TO_SAVE_IN_DATABASE, false);
                cv.put(DataContract.CandidateDetails.CANDIDATE_NAME, nameCand);
                cv.put(DataContract.CandidateDetails.NATURE_OF_ABUSE, nature);
                cv.put(DataContract.CandidateDetails.DATE_OF_JOINING, date_joining);
                cv.put(DataContract.CandidateDetails.USER_EMAIL, emailCand);
                cv.put(DataContract.CandidateDetails.CURRENT_ORGANIZATION, candidateOrganisation);
                cv.put(DataContract.CandidateDetails.REPORT_ABUSED_BY, reportAbusedBy);
                int dataInserted=(int)db.insertWithOnConflict(DataContract.CandidateDetails.TABLE_NAME, null, cv,SQLiteDatabase.CONFLICT_REPLACE);

            }catch(Exception e){

            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                URL url = new URL("http://192.168.1.102:8000/candidate/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                JSONObject data = new JSONObject();
                data.put("name", CandidateName);
                data.put("current_organization", CandidateComapanyName);
                data.put("position", "Software Developer");
                data.put("educational_background", "Example");
                data.put("email", CandidateEmail);
                data.put("phone_no", "1234567891");
                data.put("past_experience", "past_exp");
                data.put("report_abused_by", HrPrimaryKey);
                data.put("to_save_in_database", false);
                data.put("nature_of_abuse",natureOfAbuse);
                data.put("date_of_joining",DateOfJoining);
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
                storeInDataBase(valueError);
                return null;
            } catch (Exception e) {
                System.out.println(e);
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            SendingOtp sendingOtp = new SendingOtp();
            sendingOtp.execute();
        }

    }

    public class SendingOtp extends AsyncTask<String, Void, Void> {
        BufferedReader reader;
        HttpURLConnection urlConnection;
        Boolean postexec = false;
        String valueError="";
        public String HrEMailData;

        @Override
        protected void onPreExecute() {
            HrEMailData =  HrEmailEdit.getText().toString();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                URL url = new URL("http://192.168.1.102:8000/auth/send-signup-otp/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                JSONObject data = new JSONObject();
                data.put("email", HrEMailData);
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
            Intent otpScreen = new Intent(Signup.this, VerifyOtp.class);
            otpScreen.putExtra("CandidatePK", candidatePk);
            otpScreen.putExtra("HrPK",HrPrimaryKey);
            otpScreen.putExtra("EmailId",HrEmailEdit.getText().toString());
            startActivity(otpScreen);
        }

    }

}
