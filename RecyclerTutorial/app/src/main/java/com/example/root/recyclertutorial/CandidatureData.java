package com.example.root.recyclertutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class CandidatureData extends AppCompatActivity {

    public String HrCompanyName, DateOfJoining;
    public TextInputLayout candidateNameInput, candidateEMailInput, candidateCompanyInput;
    public EditText candidateName, CandidateEmail, CandidateCompany, NatureOfABuse;
    public TextView PostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_information_screen);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("hrCompanyName"))
                HrCompanyName = extras.getString("hrCompanyName");
            if (extras.containsKey("DOJ"))
                DateOfJoining = extras.getString("DOJ");
        }

        candidateNameInput = (TextInputLayout)findViewById(R.id.nameCandidate);
        candidateEMailInput = (TextInputLayout)findViewById(R.id.emailAddress);
        candidateCompanyInput = (TextInputLayout)findViewById(R.id.companyName);
        candidateName = (EditText) findViewById(R.id.candidateNameText);
        CandidateEmail = (EditText)findViewById(R.id.emailText);
        CandidateCompany = (EditText)findViewById(R.id.nameCompanyText);
        NatureOfABuse = (EditText)findViewById(R.id.nature);
        PostButton = (TextView) findViewById(R.id.reportAbuse);

        PostButton.setOnClickListener(new View.OnClickListener() {
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
        String nameCandidate = candidateName.getText().toString();
        String emailCandidate = CandidateEmail.getText().toString();
        String companyNameCandidate = CandidateCompany.getText().toString();
        String abuseCandidate = NatureOfABuse.getText().toString();
        if (TextUtils.isEmpty(nameCandidate)) {
            candidateNameInput.setError("Please fill this field");
            requestFocus(candidateName);
            cancel = true;
        }else
        {
            candidateNameInput.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(emailCandidate)) {
            candidateEMailInput.setError("Please fill this field");
            requestFocus(CandidateEmail);
            cancel = true;
        }else
        {
            candidateEMailInput.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(companyNameCandidate)) {
            candidateCompanyInput.setError("Please fill this field");
            requestFocus(CandidateCompany);
            cancel = true;
        }else
        {
            candidateCompanyInput.setErrorEnabled(false);
        }

        if (cancel) {
        } else {
            Intent signupIntent = new Intent(CandidatureData.this,Signup.class);
            signupIntent.putExtra("hrCompanyName",HrCompanyName);
            signupIntent.putExtra("DOJ", DateOfJoining);
            signupIntent.putExtra("nameCandidate", nameCandidate);
            signupIntent.putExtra("emailCandidate", emailCandidate);
            signupIntent.putExtra("companyCandidate", companyNameCandidate);
            signupIntent.putExtra("nature", abuseCandidate);
            startActivity(signupIntent);
        }

    }
}
