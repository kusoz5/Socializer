package com.example.android.socializer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class loginInstagram extends AppCompatActivity {

    EditText username;

    EditText password;

    ImageView passwordVisiblity;

    String user;

    String pass;

    Button loginButton;

     int  inp;

     boolean isVisible =false;

     boolean Emailpresent = false;

    HTTP http = new HTTP();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_instagram);

        username = findViewById(R.id.username);

        password = findViewById(R.id.password);

        loginButton = findViewById(R.id.login);

        passwordVisiblity = (ImageView) findViewById(R.id.passwordVisiblity);
        inp = password.getInputType();
        passwordVisiblity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isVisible){

                    passwordVisiblity.setImageResource(R.drawable.eye_open);
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    isVisible = true;

                }
                else {
                    passwordVisiblity.setImageResource(R.drawable.eye_close);
                    password.setInputType(inp);
                    isVisible =false;

                }
            }
        });







           loginButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   user = username.getText().toString().trim();

                   pass = password.getText().toString().trim();

                   boolean checkInput = checkUserInput(user,pass);

                   if(checkInput) {

                       openLoginPage();
                       HTTP.username = user;
                       HTTP.password = pass;
                   }
               }
           });




    }

    private boolean checkUserInput(String user, String pass) {


        if(user.isEmpty()) {

            username.setError("Please Enter The username or emaail ! ");

            return false;

        } if(pass.isEmpty()) {
            password.setError("Please Enter The password! ");
            return false;
        }

        return true;

    }


    private void openLoginPage() {


        HttpRequestAsynce httpRequest = new HttpRequestAsynce();
        GlobalAccessibleClass.httpRequestAsynce= httpRequest;
        httpRequest.execute(HTTP.landingPage,"landingPage");


    }


    public class HttpRequestAsynce extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPostExecute(String s) {


            if(SecurityCheck.check) {
                publishProgress();
                loginButton.setText("Security Check");

                AlertDialog.Builder builder = new AlertDialog.Builder(loginInstagram.this);
                builder.setTitle("Security Check Instagram \nSend Security Code To");

                final ArrayList<String> list = new ArrayList<>();
                if(!SecurityCheck.email.equals("null")) {
                    list.add("Email : "+SecurityCheck.email);
                    Emailpresent = true;

                } if (!SecurityCheck.phone.equals("null")) {
                    list.add(SecurityCheck.phone);
                }

                String item[] = new String[list.size()] ;
                for(int i = 0 ; i <list.size();i++) {
                    item[i] = list.get(i);
                }
                builder.setSingleChoiceItems(item, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SecurityCheck.selected = which;
                    }
                });

                 builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                           if( list.size() == 2 ){
                               if (SecurityCheck.selected == 0){
                                   SecurityCheck.choice =1;
                               }
                               else {
                                   SecurityCheck.choice =0;
                               }
                           }
                           else{
                                if(Emailpresent){
                                    SecurityCheck.choice = 1;
                                }
                                else {
                                    SecurityCheck.choice = 0;
                                }
                           }
                         sendSecurityCheck sendSecurityCheck = new sendSecurityCheck();
                         sendSecurityCheck.execute(SecurityCheck.choice);
                         dialog.dismiss();

                     }
                 });
                builder.create();
                builder.show();


            } else {
                saveCookie saveCookie = new saveCookie(getApplicationContext());
                isNetworkAvailable();
                if (!GlobalAccessibleClass.isNetwrok) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
                    loginButton.setText("Login");

                } else {
                    if (saveCookie.getcookie("Authenticated", null).contains("true")) {


                        loginButton.setText("Logged In");
                        Toast.makeText(getApplicationContext(), "Logged In successfully!", Toast.LENGTH_SHORT).show();
                        finish();


                    } else {
                        Toast.makeText(getApplicationContext(), "Logged In Failed Enter Correct Username or Password", Toast.LENGTH_LONG).show();
                        loginButton.setText("Login");


                    }


                }
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//0-
            if(values[0] == 0){
                loginButton.setText("Connecting...");
            }
            if(values[0] == 1){
                loginButton.setText("Verifying Credentials");
            }
         }

        @Override
        protected String doInBackground(String... strings) {
           loginButton.setText("Logging In");
            String response = new HttpRequest(getApplicationContext()).makeHttpRequest(strings[0],strings[1]);
                   return response;
        }

        public void ProgressPublish (int value){
            publishProgress(value);
        }
        public class sendSecurityCheck extends AsyncTask<Integer,Void,String> {

          int choice;
            LayoutInflater inflater = LayoutInflater.from(loginInstagram.this);
            View view = inflater.inflate(R.layout.activity_securityenterr,null);
            @Override
            protected void onPostExecute(String s) {
             final AlertDialog.Builder builder = new AlertDialog.Builder(loginInstagram.this);

             if(choice == 1 ) {
                 builder.setTitle("Enter Secuirty Code Sent to Email: \n"+SecurityCheck.email);
             } else {
                 builder.setTitle("Enter Secuirty Code Sent to Phone: \n"+SecurityCheck.phone);
             }

             builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {


                     EditText editText = (EditText)  view.findViewById(R.id.securitypass);

                     SecurityCheck.securityCode = editText.getText().toString();
                     Log.e("saas", "onClick: "+SecurityCheck.securityCode );
                   securityCodeSubmission securityCodeSubmission = new securityCodeSubmission();
                   securityCodeSubmission.execute(SecurityCheck.checkUrl);
                     dialog.dismiss();

                 }
             });

             builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                 }
             });




             builder.setView(view);
             builder.create();
             builder.show();


            }

            @Override
            protected String doInBackground(Integer... integers) {

                HttpRequest httpRequest = new HttpRequest(loginInstagram.this);
                choice = integers[0];
             return    httpRequest.selectSecuritCheckMethod(integers[0]);

            }

        }


    }

    public class  securityCodeSubmission extends AsyncTask<String,Void,String >{
        @Override
        protected void onPostExecute(String s) {
            if(s == "true"){
                loginButton.setText("Logged In");
                Toast.makeText(getApplicationContext(), "Logged In successfully!", Toast.LENGTH_SHORT).show();
                SecurityCheck.securityCode = null; SecurityCheck.phone = null; SecurityCheck.email= null ;SecurityCheck.rollout_hast = null;
                SecurityCheck.checkUrl  =null ;
                SecurityCheck.choice = -1;
                SecurityCheck.selected = -1;
                        finish();
            }else {
                loginButton.setText("Security Check Failed ");
                Toast.makeText(getApplicationContext(), "Security Check Failed !", Toast.LENGTH_SHORT).show(); SecurityCheck.securityCode = null; SecurityCheck.phone = null; SecurityCheck.email= null ;SecurityCheck.rollout_hast = null;
                SecurityCheck.checkUrl  =null ;
                SecurityCheck.choice = -1;
                SecurityCheck.selected = -1;
                loginButton.setText("Please Relogin Again");
            }
        }


        @Override
        protected String doInBackground(String... strings) {
            HttpRequest httpRequest = new HttpRequest(loginInstagram.this);

            String isLogin  = httpRequest.submitSecurityCode(strings[0]);
            return isLogin;

        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean network = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if(network) {
            GlobalAccessibleClass.isNetwrok = true;
        } return network;
    }





}
