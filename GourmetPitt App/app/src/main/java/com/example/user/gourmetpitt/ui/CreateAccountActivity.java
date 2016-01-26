//package com.example.user.gourmetpitt.ui;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//
//import com.example.user.gourmetpitt.R;
//import com.example.user.gourmetpitt.client.DefaultSocketClient_CreateUser;
//
//import java.util.ArrayList;
//
//
//public class CreateAccountActivity extends Activity {
//
//    EditText editUserName;
//    EditText editEmail;
//    EditText editPassword;
//    EditText editRePassword;
//
//    RadioButton radiobutton1;
//    RadioButton radiobutton2;
//
//    RadioGroup radiogroup;
//
//    String favour = null;
//    String email = null;
//    String username = null;
//    String password = null;
//    String repassword = null;
//    String usertype = null;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_account);
//
//        Intent intentreceive = getIntent();
//        usertype = intentreceive.getStringExtra("USERTYPE");
//
//
//        editUserName = (EditText) findViewById(R.id.editTextUserName);
//
//
//        editEmail = (EditText)findViewById(R.id.editTextEmail);
//
//
//        editPassword = (EditText)findViewById(R.id.editTextPassword);
//
//        editRePassword = (EditText) findViewById(R.id.editTextRePassword);
//
//
//        radiobutton1 = (RadioButton) findViewById(R.id.radioButton1);
//        radiobutton2 = (RadioButton) findViewById(R.id.radioButton2);
//
//        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
//
//        radiogroup.setOnCheckedChangeListener(radiochange);
//
//
//    }
//
//    private RadioGroup.OnCheckedChangeListener radiochange = new RadioGroup.OnCheckedChangeListener(){
//
//        public void onCheckedChanged(RadioGroup group, int checkedId){
//
//            if(checkedId == radiobutton1.getId()){
//
//                favour = "west";
//
//            }else if(checkedId == radiobutton2.getId()){
//
//
//                favour = "east";
//            }
//
//        }
//
//    };
//
//
//
//    public void onCancelClick(View view){
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
//    }
//
//
//
//    public void onRegClick(View view){
//        username = editUserName.getText().toString();
//        email = editEmail.getText().toString();
//        password = editPassword.getText().toString();
//        repassword = editRePassword.getText().toString();
//
//        if(favour==null||email==null||username==null||password==null||repassword==null){
//
//            Toast.makeText(getApplicationContext(), "lack input!!!", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        if( !password.equals(repassword)){
//
//            Toast.makeText(getApplicationContext(), "password do not match with repassword", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//
//        ArrayList<String> sendlist = new ArrayList<String>();
//
//        sendlist.add(email);
//        sendlist.add(password);
//        sendlist.add(usertype);
//        sendlist.add(favour);
//
//
//        try {
//            synchronized (this) {
//                DefaultSocketClient_CreateUser clientSocket = new DefaultSocketClient_CreateUser(this, sendlist);
//                clientSocket.start();
//
//                wait();
//                //Thread.sleep(500);
//
//
//
//
//                if (clientSocket.msg.size() != 0) {
//                    String msg = clientSocket.msg.get(0);
//                    if(msg.equals("true")){
//                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
//                        Intent i = new Intent(this, MainActivity.class);
//
//                        startActivity(i);
//
//
//                    }else{
//                        Toast.makeText(getApplicationContext(), "usernameduplicate", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "nothing", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                clientSocket.closeSession();
//            }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//
//
//
//
//
//    }
//
//}
package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.RegisterClient;
import com.example.user.gourmetpitt.util.EmailJudge;

import java.util.ArrayList;


public class CreateAccountActivity extends Activity {

    EditText editUserName;
    EditText editEmail;
    EditText editPassword;
    EditText editRePassword;

    RadioButton radiobutton1;
    RadioButton radiobutton2;

    RadioGroup radiogroup;

    String flavor = null;
    String email = null;
    String username = null;
    String password = null;
    String repassword = null;
    String usertype = null;

    EmailJudge emailj = new EmailJudge();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Intent intentreceive = getIntent();
        usertype = intentreceive.getStringExtra("userType");


        editUserName = (EditText) findViewById(R.id.editTextUserName);


        editEmail = (EditText)findViewById(R.id.editTextEmail);


        editPassword = (EditText)findViewById(R.id.editTextPassword);

        editRePassword = (EditText) findViewById(R.id.editTextRePassword);


        radiobutton1 = (RadioButton) findViewById(R.id.radioButton1);
        radiobutton2 = (RadioButton) findViewById(R.id.radioButton2);

        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);

        radiogroup.setOnCheckedChangeListener(radiochange);
    }

    private RadioGroup.OnCheckedChangeListener radiochange = new RadioGroup.OnCheckedChangeListener(){

        public void onCheckedChanged(RadioGroup group, int checkedId){

            if(checkedId == radiobutton1.getId()){
                flavor = "west";
            }
            else if(checkedId == radiobutton2.getId()){
                flavor = "east";
            }
        }
    };

    public void onCancelClick(View view){
        //Intent i = new Intent(this, MainActivity.class);
        //startActivity(i);
        finish();
    }

    public void onRegClick(View view){
        username = editUserName.getText().toString();
        email = editEmail.getText().toString();
        password = editPassword.getText().toString();
        repassword = editRePassword.getText().toString();

        if(flavor == null||email==null||username==null||password==null||repassword==null){

            Toast.makeText(getApplicationContext(), "lack input!!!", Toast.LENGTH_LONG).show();
            return;
        }

        if(!password.equals(repassword)){

            Toast.makeText(getApplicationContext(), "password do not match with repassword", Toast.LENGTH_LONG).show();
            return;
        }

//        if(!emailj.judge(email)){
//            email = "123@qq.com";
//        }

        ArrayList<String> sendlist = new ArrayList<String>();

        sendlist.add(email);
        sendlist.add(password);
        sendlist.add(usertype);
        sendlist.add(flavor);


        try {
            synchronized (this) {
                RegisterClient clientSocket = new RegisterClient(this, sendlist);

                clientSocket.start();

                wait();

                if(clientSocket.added == true)
                    Toast.makeText(this, "User Added", Toast.LENGTH_SHORT).show();

                else    Toast.makeText(this, "User Existed", Toast.LENGTH_SHORT).show();

                /*if (clientSocket.msg.size() != 0) {
                    String msg = clientSocket.msg.get(0);
                    if(msg.equals("true")){
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(this, MainActivity.class);

                        startActivity(i);


                    }else{
                        Toast.makeText(getApplicationContext(), "usernameduplicate", Toast.LENGTH_LONG).show();
                        return;
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "nothing", Toast.LENGTH_LONG).show();
                    return;
                }*/
                clientSocket.closeSession();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_acount, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
