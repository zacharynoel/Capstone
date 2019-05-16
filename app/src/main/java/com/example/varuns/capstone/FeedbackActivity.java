package com.example.varuns.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {
    private EditText bugFeedback;
    private EditText bugCause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        bugFeedback = findViewById(R.id.bugDetail);
        bugCause = findViewById(R.id.bugCause);
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitSurvey();
            }
        });
    }

    private void submitSurvey() {
        boolean submittable = true;
        bugFeedback.setError(null);
        bugCause.setError(null);

        String feedback = bugFeedback.getText().toString();
        String cause = bugCause.getText().toString();
        if (feedback.isEmpty()) {
            submittable = false;
            bugFeedback.setError("Please provide detail");
        }

        if (cause.isEmpty()) {
            submittable = false;
            bugCause.setError("Please provide the cause as best you can guess");
        }

        if (submittable) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"mcraney129@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Bug Report");
            i.putExtra(Intent.EXTRA_TEXT   , "Detail: \n" + feedback + "\nCause: \n" + cause );
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(FeedbackActivity.this, "No email client",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
