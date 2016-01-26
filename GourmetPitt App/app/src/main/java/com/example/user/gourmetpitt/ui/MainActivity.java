package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.LoginSocketClient;
import com.example.user.gourmetpitt.client.RequestCusInfoClient;
import com.example.user.gourmetpitt.service.BackGroundMusicService;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {

    EditText username;
    EditText password;

    boolean toCustomer = false;
    boolean toBusiness = false;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.editTextUserName);
        password = (EditText) findViewById(R.id.editPassword);

        i = new Intent(this, BackGroundMusicService.class);
        startService(i);

    }

    public void onRegClick(View view){

        Intent i = new Intent(this, CreateAccountActivity.class);
        //startActivity(i);
        if(toCustomer){
            i.putExtra("userType", "Customer");
            startActivity(i);
        }
        else if(toBusiness){
            i.putExtra("userType", "Business");
            startActivity(i);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please select a user type(Customer/Business)", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLogClick(View view){
        /*
        Needs to retrieve all restaurants information
         */
        String userName = username.getText().toString();
        String passWord = password.getText().toString();
        boolean pass = false;
        boolean goOn = false;

        HashMap<String, String> loginInfo = new HashMap<String, String>();
        loginInfo.put("UserName", userName);
        loginInfo.put("Password", passWord);
        if (toCustomer){
            loginInfo.put("Identification", "Customer");
            goOn = true;
        }
        else if (toBusiness){
            loginInfo.put("Identification", "Business");
            goOn = true;
        }
        else {
            pass = false;
            goOn = false;
            Toast.makeText(getApplicationContext(), "Please select a user type(Customer/Business)", Toast.LENGTH_SHORT).show();
        }

        if(goOn) {
            try {
                synchronized (this) {
                    LoginSocketClient clientSocket = new LoginSocketClient(this);
                    clientSocket.setLoginInfo(loginInfo);
                    clientSocket.start();

                    wait();
                    if (clientSocket.msg.get(0).equals("pass")) pass = true;
                    else pass = false;
                }
            } catch (Exception e) {
                pass = false;
                e.printStackTrace();
            }

            if (pass) {
                if (toCustomer) {
                    Intent i = new Intent(this, CustomHomeActivity.class);
                    pass = false;
                    try {
                        synchronized (this) {
                            RequestCusInfoClient clientSocket = new RequestCusInfoClient(this);
                            ArrayList<String> nameAndType = new ArrayList<String>();
                            nameAndType.add(userName);
                            nameAndType.add("Customer");
                            clientSocket.setNameAndType(nameAndType);
                            clientSocket.start();

                            wait();
                            if(clientSocket.customerInfo != null){
                                i.putExtra("customerInfo", clientSocket.customerInfo);
                                startActivity(i);
                            }
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                } else if (toBusiness) {
                    Intent i = new Intent(this, BusinessHomeActivity.class);
                    i.putExtra("BusinessUserName",userName);
                    pass = false;
                    startActivity(i);
                } else {
                    pass = false;
                    Toast.makeText(getApplicationContext(), "Please select a user type(Customer/Business)", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Invalid login information, enter again", Toast.LENGTH_SHORT).show();
                username.setText("");
                password.setText("");
                pass = false;
            }
        }
    }


    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.radioCustomer:
                if(checked)
                    toCustomer = true;
                toBusiness = false;
                break;
            case R.id.radioBusiness:
                if(checked)
                    toBusiness = true;
                toCustomer = false;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // i = new Intent(this, BackGroundMusicService.class);
        stopService(i);

        super.onDestroy();


    }
}
