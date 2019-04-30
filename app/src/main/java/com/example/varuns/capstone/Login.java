package com.example.varuns.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.workflow.RequestContext;

public class Login extends AppCompatActivity {

    private RequestContext requestContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Button button = (Button) findViewById(R.id.sign_in);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, menu_activity.class);
                startActivity(intent);
            }
        });

        requestContext = RequestContext.create(this);

        requestContext.registerListener(new AuthorizeListener() {

            /* Authorization was completed successfully. */
            @Override
            public void onSuccess(AuthorizeResult result) {
                /* Your app is now authorized for the requested scopes */
                Intent intent = new Intent(Login.this, menu_activity.class);
                startActivity(intent);
            }

            /* There was an error during the attempt to authorize the
            application. */
            @Override
            public void onError(AuthError ae) {
                System.out.println("Error when signing in");
                
            }

            /* Authorization was cancelled before it could be completed. */
            @Override
            public void onCancel(AuthCancellation cancellation) {
                System.out.println("Cancel when signing in");
            }
        });

        View loginButton = findViewById(R.id.login_with_amazon);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthorizationManager.authorize(new AuthorizeRequest
                        .Builder(requestContext)
                        .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
                        .build());
            }});



    }


    protected void onResume() {
        super.onResume();
        requestContext.onResume();
    }


    }


