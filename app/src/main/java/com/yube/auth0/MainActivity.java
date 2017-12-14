package com.yube.auth0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

public class MainActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {


    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView tv1, tv2;
    private ProfileTracker profileTracker;
    private ProfilePictureView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        loginButton = findViewById(R.id.login_button);
        tv1 = findViewById(R.id.textView3);
        tv2 = findViewById(R.id.textView4);
        loginButton.setReadPermissions("email");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callbackManager = CallbackManager.Factory.create();
                loginButton.registerCallback(callbackManager, MainActivity.this);
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {

                String name = " ", surname = "", id = null;
                profileImage = findViewById(R.id.profilePicture);
                try {
                    name = currentProfile.getFirstName();
                    surname = currentProfile.getLastName();
                    id = currentProfile.getId();
                    profileImage.setProfileId(currentProfile.getId());
                } catch (Exception e) {
                    id = "";
                    name = "Ad";
                    surname = "Soyad";
                } finally {


                    profileImage.setProfileId(id);
                    tv1.setText(name);
                    tv2.setText(surname);
                }
            }
        };


    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Toast.makeText(MainActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "İptal Edildi", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(FacebookException error) {
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }
}
