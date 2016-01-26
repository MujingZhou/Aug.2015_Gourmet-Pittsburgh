package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.user.gourmetpitt.R;

import model.GMailSender;

public class BusinessContactUsActivity extends Activity {

    private Context context;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_contact_us);
        Intent intent=getIntent();
        userName=intent.getStringExtra("BusinessUserName");
        context=this;

//        Button send = (Button) this.findViewById(R.id.send);
//        send.setOnClickListener((View.OnClickListener) this);
//        send.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                synchronized(context) {
//                    try {
//                        GMailSender sender = new GMailSender("zhoumujing1992@gmail.com", "Hh!277349971",context);
//                        sender.sendMail("This is Subject",
//                                "This is Body",
//                                "zhoumujing1992@gmail.com",
//                                "mujingz@andrew.cmu.edu");
//                        context.wait();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });
//        Intent i = new Intent(Intent.ACTION_SEND);
//        i.setType("message/rfc822");
//        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
//        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
//        i.putExtra(Intent.EXTRA_TEXT, "body of email");
//        try {
//            startActivity(Intent.createChooser(i, "Send mail..."));
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(BusinessContactUsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//        }
    }

    public void onClickSend(View view){

            try {
//                synchronized (this) {
                    GMailSender sender = new GMailSender("gourmetpitttour@gmail.com", "gourmet123", this);
                    sender.sendMail("Thanks for your feedback!",
                            "Thanks for your feedback on Gourmet Pitt Tour!",
                            "zhoumujing1992@gmail.com",
                            "");
//                    this.wait();
//                }
                Intent intent=new Intent(this,BusinessHomeActivity.class);
                intent.putExtra("BusinessUserName",userName);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void onClickSendApp(View view){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"gourmetpitttour@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT, "Your feedback");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(BusinessContactUsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
//        Intent intent=new Intent(this,BusinessHomeActivity.class);
//        intent.putExtra("BusinessUserName",userName);
//        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_business_contact_us, menu);
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
