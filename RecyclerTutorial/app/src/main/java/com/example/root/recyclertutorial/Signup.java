package com.example.root.recyclertutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;



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
            
        }

    }
}
