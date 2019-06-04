package com.example.varuns.capstone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.amazon.identity.auth.device.api.authorization.User;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.example.varuns.capstone.model.SessionItem;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private RequestContext requestContext;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private SharedPreferences sharedPreferences = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    String email;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("token")) {
            System.out.println("FOUND TOKEN");
            //validateToken(sharedPreferences.getString("token", ""));
        }

        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mRegistrationButton = findViewById(R.id.registration_button);
        mRegistrationButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.activity_login_registration_popup);

                Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                final AutoCompleteTextView dRegistrationEmail = dialog.findViewById(R.id.registration_email);
                final EditText dRegistrationPassword = dialog.findViewById(R.id.registration_password);

                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String registrationEmail = dRegistrationEmail.getText().toString();
                        String registrationPassword = dRegistrationPassword.getText().toString();
                        UserRegistrationTask dDialogRegistrationTask = new UserRegistrationTask(registrationEmail, registrationPassword);
                        dDialogRegistrationTask.execute();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        requestContext = RequestContext.create(this);

        requestContext.registerListener(new AuthorizeListener() {

            /* Authorization was completed successfully. */
            @Override
            public void onSuccess(AuthorizeResult result) {
                /* Your app is now authorized for the requested scopes */
                UserLoginTask qAuthTask = new UserLoginTask(email, password);
                qAuthTask.execute((Void) null);
                try {
                    if(!qAuthTask.get()) {
                        UserRegistrationTask qRegisterTask = new UserRegistrationTask(email, password);
                        qRegisterTask.execute((Void) null);
                        if(qRegisterTask.get()) {
                            qAuthTask = new UserLoginTask(email, password);
                            qAuthTask.execute((Void) null);
                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Intent intent = new Intent(LoginActivity.this, menu_activity.class);
                //startActivity(intent);
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

    @Override
    protected void onStart() {
        super.onStart();
        Scope[] scopes = {ProfileScope.profile(), ProfileScope.postalCode()};
        AuthorizationManager.getToken(this, scopes, new Listener<AuthorizeResult, AuthError>() {
            @Override
            public void onSuccess(AuthorizeResult result) {
                if (result.getAccessToken() != null) {
                    /* The user is signed in */
                    fetchUserProfile();
                } else {
                    /* The user is not signed in */
                }
            }

            @Override
            public void onError(AuthError ae) {
                /* The user is not signed in */
            }
        });
    }

    private void fetchUserProfile() {
        User.fetch(this, new Listener<User, AuthError>() {

            /* fetch completed successfully. */
            @Override
            public void onSuccess(User user) {
                final String name = user.getUserName();
                final String uemail = user.getUserEmail();
                final String account = user.getUserId();
                final String zipCode = user.getUserPostalCode();

                email = uemail;
                password = account;

            }

            /* There was an error during the attempt to get the profile. */
            @Override
            public void onError(AuthError ae) {
            /*    runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String errorMessage = "Error retrieving profile information.\nPlease log in again";
                        Toast errorToast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG);
                        errorToast.setGravity(Gravity.CENTER, 0, 0);
                        errorToast.show();
                    }
                });
                */
            }
        });
    }


    private void validateToken(String token) {
        ApiService.setToken(token);
        Call<RestfulResponse> call = ApiService.loginService().validate();
        //handle the response
        call.enqueue(new Callback<RestfulResponse>() {
            @Override
            public void onResponse(Call<RestfulResponse> call, Response<RestfulResponse> response) {
                if(response.isSuccessful()){
                    System.out.println("Worked");
                    nextScreen();
                }
                else{
                    System.out.println("Didn't work");
                    System.out.println(response.raw().toString());
                    ApiService.clearToken();
                }
            }

            @Override
            public void onFailure(Call<RestfulResponse> call, Throwable t) {
                System.out.println("Failed");
            }
        });
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private void nextScreen() {
        Intent myIntent = new Intent(LoginActivity.this, menu_activity.class);
        startActivity(myIntent);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        boolean hasat = email.contains("@");
        boolean hascom = email.contains(".com");
        return hasat&&hascom;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private int responseCode = 401;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<String,String> loginObj = new HashMap<>();
            loginObj.put("username", mEmail);
            loginObj.put("password", mPassword);
            Call<RestfulResponse<SessionItem>> call = ApiService.loginService().attemptLogin(loginObj);
            //handle the response
            try {
                Response<RestfulResponse<SessionItem>> response = call.execute();
                if(response.isSuccessful()){
                    String token = response.body().getData().getToken();
                    ApiService.setToken(token);
                    sharedPreferences
                            .edit()
                            .putString("token", token)
                            .apply();
                    System.out.println("Storing token");
                }
                else{
                    System.out.println("Response unsuccessful, clearing token");
                    responseCode = response.code();
                    ApiService.clearToken();
                }
            } catch (IOException e) {
                Toast.makeText(LoginActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }

            return ApiService.hasToken();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                System.out.println("Successful login");
                finish();
                nextScreen();
            } else {
                System.out.println("Unsuccessful login");
                if (responseCode == 401) {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                }
                else {
                    mEmailView.setError(getString(R.string.error_incorrect_email));
                }
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            System.out.println("Cancelled");
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    public class UserRegistrationTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserRegistrationTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<String,String> loginObj = new HashMap<>();
            loginObj.put("username", mEmail);
            loginObj.put("password", mPassword);

            if(mEmail.isEmpty() || mPassword.isEmpty() || mPassword.length() < 5){
                return false;
            }

            Call<RestfulResponse<SessionItem>> call = ApiService.loginService().attemptRegistration(loginObj);
            //handle the response
            try {
                Response<RestfulResponse<SessionItem>> response = call.execute();
                return response.isSuccessful();
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                System.out.println("Successful registration");
            } else {
                if(mEmail.isEmpty() && mPassword.isEmpty())
                    Toast.makeText(LoginActivity.this, "Registration must include an email and password", Toast.LENGTH_SHORT).show();
                else if(mEmail.isEmpty())
                    Toast.makeText(LoginActivity.this, "Registration must include an email", Toast.LENGTH_SHORT).show();
                else if(mPassword.isEmpty())
                    Toast.makeText(LoginActivity.this, "Registration must include a password", Toast.LENGTH_SHORT).show();
                else if(mPassword.length() < 5)
                    Toast.makeText(LoginActivity.this, "Password must contain 5 or more characters", Toast.LENGTH_SHORT).show();
                System.out.println("Unsuccessful registration");
            }
        }

        @Override
        protected void onCancelled() {
            System.out.println("Cancelled Registration");
        }
    }
}

