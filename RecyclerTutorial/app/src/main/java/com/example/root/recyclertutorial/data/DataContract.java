package com.example.root.recyclertutorial.data;

import android.os.StrictMode;
import android.provider.BaseColumns;



public class DataContract {
    public static final class LoginDetails implements BaseColumns{
        public static final String TABLE_NAME = "loginDetail";
        public static final String USER_ID = "UserID";
        public static final String USER_NAME = "UserName";
        public static final String EMAIL = "Email";
        public static final String COMPANY_NAME = "Company_name";
        public static final String email_confirmed = "email_confirmed";
    }

    public static final class CandidateDetails implements BaseColumns{
        public static final String TABLE_NAME = "CandidateDetail";
        public static final String CANDIDATE_NAME = "UserID";
        public static final String REPORT_ABUSED_BY = "report_abused_by";
        public static final String TO_SAVE_IN_DATABASE = "to_save_in_database";
        public static final String NATURE_OF_ABUSE = "nature_of_abuse";
        public static final String DATE_OF_JOINING = "date_of_joining";
        public static final String USER_EMAIL = "user_email";
        public static final String CURRENT_ORGANIZATION = "current_organization";
        public static final String CANDIDATE_ID = "candidate_id";
    }

}
