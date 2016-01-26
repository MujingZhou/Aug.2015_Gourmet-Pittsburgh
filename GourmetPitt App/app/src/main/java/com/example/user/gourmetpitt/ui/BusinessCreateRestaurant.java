package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.DefaultSocketClient_CreateRestaurant;
import com.example.user.gourmetpitt.client.DefaultSocketClient_RetrieveInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

//import android.<span id="IL_AD7" class ="IL_AD">database</span>.Cursor;

public class BusinessCreateRestaurant extends Activity {
    private String userName;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath="";

    private final String BUCKET_NAME = "cmurunyfirsttry";
    private final String BUSINESS_FOLDER_NAME = "business"+"/";
    private final String COSTUMER_FOLDER_NAME = "costumer"+"/";


    private ImageView img;

    private EditText resname;

    AmazonS3Client amazonclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.user.gourmetpitt.R.layout.activity_business_create_restaurant);

        //use strictmode to avoid exception

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //set amazons3client
        amazonclient = new AmazonS3Client(new BasicAWSCredentials("AKIAIYSC6ORMFNV5GQ4Q","PJcX1hZslfMH/yoDUMOLvkuhnAuk0J3qvhkxX2pT")) ;


        resname = (EditText)findViewById(R.id.editName);

        Intent intent=getIntent();
        String userName=intent.getStringExtra("BusinessUserName");

        img = (ImageView)findViewById(R.id.imageView28);

        ((Button) findViewById(R.id.imagePhoto))
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
                    }
                });


        this.userName=userName;
        try {
            synchronized (this) {
                DefaultSocketClient_RetrieveInfo clientSocket = new DefaultSocketClient_RetrieveInfo(this);
                clientSocket.start();

                wait();
                clientSocket.closeSession();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

//    public void choosePhoto(View view){

//    }

    public void AWSupload(String imagepath){

        File imageFile = new File(imagepath);
        long lengthOfFile = imageFile.length();

        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME,BUSINESS_FOLDER_NAME+resname.getText().toString(),imageFile);

        ObjectMetadata objdata = new ObjectMetadata();
        objdata.setContentLength(lengthOfFile);
        putObjectRequest.withMetadata(objdata);

        amazonclient.putObject(putObjectRequest);


    }

    public URL getAWSURL(String filename, String foldername){

        String folder=null;

        java.util.Date expiration = new java.util.Date();
        long msec = expiration.getTime();
        msec += 1000 * 60 * 60; // 60 hour.
        expiration.setTime(msec);

        if(foldername.equals("costumer")){
            folder = COSTUMER_FOLDER_NAME;
        }else{
            folder = BUSINESS_FOLDER_NAME;
        }

        URL url = amazonclient.generatePresignedUrl(BUCKET_NAME,folder+filename, expiration);

        return url;

    }

    public void saveBitMap(Bitmap picture){

        String bitmapname = resname.getText().toString();
        if(bitmapname==null)bitmapname="runyang";


        File f = new File("/mnt/sdcard/runy/"+bitmapname);
       // if(f.exists()){
         //   f.delete();
        //}
        try{

            if(!f.exists()){
                f.createNewFile();
            }

        }catch(IOException e){
            Log.d("save picture", "错误");
            e.printStackTrace();

        }

        FileOutputStream out=null;
        try{
             out = new FileOutputStream(f);
            if(picture.compress(Bitmap.CompressFormat.JPEG,80,out)){
                Log.d("save picture", "保存中");
            }
            out.flush();
            out.close();
            Log.d("save picture", "已经保存");
        }catch(FileNotFoundException e){
            e.printStackTrace();
            Log.d("save picture", "filenotfound");
        }catch(IOException e){
            e.printStackTrace();
            Log.d("save picture", "ioexception");
        }


    }


    public void clickCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            ImageView mImageView=(ImageView)findViewById(R.id.imageView28);
//            mImageView.setImageBitmap(imageBitmap);
//        }

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath=getRealPathFromURI(this,selectedImageUri);
//                selectedImagePath = selectedImageUri.getPath();
                System.out.println("Image Path : " + selectedImagePath);

                AWSupload(selectedImagePath);

                img.setImageURI(selectedImageUri);
                return;
            }

            else if (requestCode == REQUEST_IMAGE_CAPTURE){
                Bundle extras = data.getExtras();
                 Bitmap imageBitmap = (Bitmap) extras.get("data");
                 ImageView mImageView=(ImageView)findViewById(R.id.imageView28);
                saveBitMap(imageBitmap);
                 mImageView.setImageBitmap(imageBitmap);

                Log.d("save picture", "aftersave");
                return;
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        String wholeID = DocumentsContract.getDocumentId(contentUri);

// Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

// where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();
        return filePath;
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    public void clickEditOK(View view){
        EditText editName=(EditText)findViewById(R.id.editName);
        EditText editTelephone=(EditText)findViewById(R.id.editTelephone);
        EditText editLocation=(EditText)findViewById(R.id.editLocation);
        EditText edtiOpenHour=(EditText)findViewById(R.id.editOpenHour);
        EditText editFlavor=(EditText)findViewById(R.id.editFlavor);

        try {
            synchronized (this) {
                DefaultSocketClient_CreateRestaurant clientSocket = new DefaultSocketClient_CreateRestaurant(this);
                String restaurantName=editName.getText().toString();
                String telephone=editTelephone.getText().toString();
                String location=editLocation.getText().toString();
                String openHour=edtiOpenHour.getText().toString();
                String flavor=editFlavor.getText().toString();
                String url=getAWSURL(restaurantName,"business/").toString();
                System.out.println("imageurl: "+url);

                clientSocket.setEditInfomation(userName, restaurantName, telephone, location, openHour, flavor,url);

                System.out.println("userName is create is"+userName);
                clientSocket.start();

                wait();
                clientSocket.closeSession();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

//        URL url=getAWSURL(userName, "business");
//        Drawable drawable = LoadImageFromWebOperations(url.toString());
//        showimage.setImageDrawable(drawable);

        Intent intent=new Intent(this,BusinessMyRestaurantActivity.class);
        intent.putExtra("BusinessUserName",userName);
//        intent.putExtra("url",url);
        startActivity(intent);

    }

//    private Drawable LoadImageFromWebOperations(String url,String restName)
//    {
//        try
//        {
//
//            InputStream is = (InputStream) new URL(url).getContent();
//            Drawable d = Drawable.createFromStream(is, restName);
//            return d;
//
//        }catch (Exception e) {
//            Log.d("Excep","Exc="+e);
//            return null;
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_business_create_restaurant, menu);
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
