package com.example.im.practicetask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

//    MyGallery myGallery=new MyGallery();

    Databasehelper dbhelp = new Databasehelper(this);
    Contact c=new Contact();
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private Button gmailSignInButton, fb_LoginButton;
    private TextView loginStatus;
    String userName,email,userPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getApplicationContext());            //initializing fb sdk
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gmailSignInButton = (Button) findViewById(R.id.gmail_signinbutton);
        gmailSignInButton.setOnClickListener(this);
        fb_LoginButton = (Button) findViewById(R.id.fb_LoginButton);
        fb_LoginButton.setOnClickListener(this);
        loginStatus = (TextView) findViewById(R.id.loginStatus);


        //checks network state
        isNetworkAvailable();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        // fb begins
        callbackManager = CallbackManager.Factory.create();     //initializing callbackmanager  factory is to initialize constructor and create is to create instance.
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) //executes if log in is successful
                    {
                        setFacebookData(loginResult);

                    }

                    @Override
                    public void onCancel()    //executes when login is cancelled by user
                    {
                        loginStatus.setText(" FB Login Cancelled");
                    }

                    @Override
                    public void onError(FacebookException exception) // executes if any error occurs and shows the exception
                    {
                        loginStatus.setText(exception.toString());
                    }

                });

//        c=dbhelp.checkLogin();
//        if (c.getApp().equals("Google"))
//        {
//            dbhelp.delete();
//            signIn();
//        }
//
//
//         else if (c.getApp().equals("Facebook"))
//        {
//            dbhelp.delete();
//            LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
//        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.fb_LoginButton: {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
                //LoginManager class manages login and permissions for Facebook and getInstance is Getter for the login manager.
                break;
            }
            case R.id.gmail_signinbutton:
            {
                signIn();
                break;
            }
        }
    }


    //google sign in function
    private void signIn() {
        boolean connected = isNetworkAvailable();
        if (connected == true) {
            c.setApp("Google");
            dbhelp.insert(c);
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }
    //sign out  function for google
    void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
//                        updateUI(false);
                    }
                });

                    }

                    //function to fetch the google log in data(Name , Email and profile) for current profile logged in
                    public void handleSignInResult(GoogleSignInResult result) {
                        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
                        if (!result.isSuccess())
                        {

                        }
                        else {
                            // Signed in successfully, show authenticated UI.
                            GoogleSignInAccount acct = result.getSignInAccount();
                            Log.e(TAG, "display name: " + acct.getDisplayName());

                            userName = acct.getDisplayName();
                            email = acct.getEmail();
                            if(acct.getPhotoUrl()!=null)
                            {
                                userPic = acct.getPhotoUrl().toString();
                            }
                            else
                            {
                                userPic="Nopic";
                            }
                            c.setName(userName);
                            c.setEmail(email);
                            c.setPic(userPic);
                            c.setApp("Google");
                            c.setDate(DateFormat.getDateTimeInstance().format(new Date()).toString());
                            dbhelp.insert(c);
                            Intent intent = new Intent(MainActivity.this, MyGallery.class);
                            startActivity(intent);
                        }

                    }




                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }

                    private void setFacebookData(LoginResult loginResult) {
                        //GraphRequest sents a request to the Facebook Platform through the Graph API and newMeRequest create a new request to retrieve data from user's profile
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        // Application code
                                        try {

                                            Log.i("Response", response.toString());
                                            String emails = response.getJSONObject().getString("email");
                                            Profile profile = Profile.getCurrentProfile();
                                            String id = profile.getId();
                                            String link = profile.getLinkUri().toString();
                                            String name = profile.getName();
                                            String time = DateFormat.getDateTimeInstance().format(new Date());
                                            //Inserting values in the database
                                            userName = name;
                                            email = emails;
                                            userPic = Profile.getCurrentProfile().getProfilePictureUri(200, 200).toString();
                                            Log.i("Link", link);
                                            c.setName(userName);
                                            c.setEmail(email);
                                            c.setPic(userPic);
                                            c.setApp("Facebook");
                                            c.setDate(time);
                                            dbhelp.insert(c);
                                            Intent intent = new Intent(MainActivity.this, MyGallery.class);
                                            startActivity(intent);
//                            myGallery.display(userName,email,userPic);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,email,first_name,last_name,gender");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }


    //receives result from the function being called in mainactivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        //for file manager's result

    }

    boolean connected = false;
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            connected = false;
        } else {
            connected = true;
        }
        return connected;
    }



}
