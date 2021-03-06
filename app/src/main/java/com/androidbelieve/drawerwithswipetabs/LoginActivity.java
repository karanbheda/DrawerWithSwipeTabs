package com.androidbelieve.drawerwithswipetabs;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    private TextView info,info_id,info_mail;
    private EditText info_phone;
    private CallbackManager callbackManager;
    private ProfilePictureView profilePictureView;
    private String userID;
    private String pid,name,email,contact;
    private SharedPreferences sharedPreferences;
    String fromAd;
    private boolean enteredApp=false;

    @Override
    protected void onDestroy() {
        if(enteredApp)
        super.onDestroy();
        else {
            Log.v("logging out","okay");
            LoginManager.getInstance().logOut();
            try {
                sharedPreferences.edit().clear();
            }
            catch (Exception e)
            {
                Log.e("Error Caught",e.getMessage());
            }
            super.onDestroy();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
        callbackManager = CallbackManager.Factory.create();
        info = (TextView)findViewById(R.id.info);
        //info_id=(TextView)findViewById(R.id.info_name);
        info_mail=(TextView)findViewById(R.id.info_mail);
        info_phone=(EditText)findViewById(R.id.info_contact);
        sharedPreferences=getSharedPreferences("LOG",MODE_PRIVATE);
        fromAd = getIntent().getStringExtra("AdActivity");
        if(isLogin())
        {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                info.setText(object.getString("name"));
                                //info_id.setText("Hi, "+object.getString("id"));
                                info_mail.setText(object.getString("email"));
                            } catch(JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
            userID= AccessToken.getCurrentAccessToken().getUserId();
            pid=userID;
            profilePictureView.setProfileId(userID);
            new GenericAsyncTask(this, Config.link + "contact.php?pid=" + pid, "", new AsyncResponse() {
                @Override
                public void processFinish(Object output) {
                    String num = (String) output;
                    info_phone.setText(num);
                    info_phone.setEnabled(false);
                }
            }).execute();

        }

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                        info.setText(" ");
                  //  info_id.setText(" ");
                    info_mail.setText(" ");
                    profilePictureView.setProfileId(null);
                }
            }
        };
    }




    class NewAd extends AsyncTask<String,String,String> {

        String pid,name,email,contact,from;

        LoginActivity thisac;
        NewAd(String pid, String name, String email, String contact,LoginActivity thisac,String from)
        {
            this.pid=pid;
            this.name=name;
            this.email=email;
            this.contact=contact;
            this.thisac=thisac;
            this.from=from;
        }


        @Override
        protected String doInBackground(String... strings) {
            try {
                String link = Config.link+"insertpdetails.php";
                String data = URLEncoder.encode("pid", "UTF-8") + "=" + URLEncoder.encode(pid, "UTF-8")+"&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")+"&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+"&" + URLEncoder.encode("contact", "UTF-8") + "=" + URLEncoder.encode(contact, "UTF-8");
                URL url = new URL(link);
                URLConnection con = url.openConnection();
                con.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(data);
                wr.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);

                }
                Log.v("sb",sb.toString());
                return sb.toString();

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            if(result!=null)
            {
                if(result.equals("Already registered")|| result.equals("sucess")) {
                    String pid=AccessToken.getCurrentAccessToken().getUserId();
                    EncryptDecrypt encryptDecrypt=null;
                    try {
                        encryptDecrypt = new EncryptDecrypt("kthiksramAndroidDevs");
                        String temp = encryptDecrypt.encrypt(pid);
                        sharedPreferences.edit().putString("abcxyz",temp).commit();
                        LoginActivity.this.enteredApp=true;
                        if(from==null)
                            startActivity(new Intent(thisac, MainActivity.class));
                        finish();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        AlertDialog.Builder alertbox = new AlertDialog.Builder(LoginActivity.this);
                        alertbox.setTitle("Error");
                        alertbox.setMessage("There was am error while decoding Login parameters\nPlease notify the developers of this");
                        alertbox.show();
                    }
                }
            }
        }
    }
    public void onSubmit(View view)
    {
       //pid=info_id.getText().toString();
        name=info.getText().toString();
        email=info_mail.getText().toString();
        contact=info_phone.getText().toString();
        if(contact.equals("")||contact.length()<10)
        {
            info_phone.setError("Please enter your phone number!");
            info_phone.requestFocus();
            return;
        }
        info_phone.setError(null);
        new NewAd(userID,name,email,contact,this,fromAd).execute();
    }
    public boolean isLogin()
    {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
