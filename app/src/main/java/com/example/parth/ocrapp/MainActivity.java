package com.example.parth.ocrapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private VisionServiceClient visionServiceClient = new VisionServiceRestClient(""); //Generate your own API KEY From Microsoft Cognitive Service Under VISION API!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.linux);
        ImageView img = (ImageView)findViewById(R.id.imageView);
        img.setImageBitmap(mBitmap);
        Button btnProcess = (Button)findViewById(R.id.btnProcess);
        //Convert Bitmap to Stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<InputStream,String,String> recognizeTextTask = new AsyncTask<InputStream, String, String>() {
                    ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
                    @Override
                    protected String doInBackground(InputStream... params) {
                        try{
                                publishProgress("Recogniing...");
                            OCR ocr  = visionServiceClient.recognizeText(params[0], LanguageCodes.English,true);
                            String result = new Gson().toJson(ocr);
                            return result;
                        }catch (Exception exp){

                            return null;
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        mDialog.show();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        mDialog.dismiss();
                        OCR ocr = new Gson().fromJson(s,OCR.class);
                        TextView textView = (TextView)findViewById(R.id.txtDescription);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Recognized Text\n");
                        for (Region region:ocr.regions){
                            for (Line line:region.lines){
                                for (Word word:line.words){
                                    stringBuilder.append(word.text+" ");
                                    stringBuilder.append("\n");
                                }
                                stringBuilder.append("\n\n");
                            }
                        }
                        //textView.setText(stringBuilder);
                        //Toast.makeText(getApplicationContext(),stringBuilder,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                        intent.putExtra("texts",stringBuilder.toString());
                        startActivity(intent);

                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        mDialog.setMessage(values[0]);
                    }
                };
                recognizeTextTask.execute(inputStream);
            }
        });
    }
}
