
package com.example.aesencryption;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.scottyab.aescrypt.AESCrypt;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    EditText et_key;
    EditText et_message;
    TextView message;
    ImageButton b1,b2;
    String encrpyted,pastedata;
    ClipboardManager clipboardmanager;
    ClipData clipdata;
    String inputkey,inputmessage;
    MaterialButtonToggleGroup btg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et_key = findViewById(R.id.et_key);
        et_message = findViewById(R.id.et_message);
        message = findViewById(R.id.message);
        b1=findViewById(R.id.b1);
        b2=findViewById(R.id.b2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        message.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void encrypt(View view) throws GeneralSecurityException {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if(et_key.length()>0&&et_message.length()>0){
             inputkey=et_key.getText().toString();
             inputmessage=et_message.getText().toString();
     try {
         encrpyted = AESCrypt.encrypt(et_key.getText().toString(), et_message.getText().toString());
         et_message.setText("");
         et_key.setText("");
         message.setText(String.format("%s", encrpyted));
         b1.setVisibility(VISIBLE);

     }catch (GeneralSecurityException e){
         e.printStackTrace();
     } }
        else{
            Toast.makeText(this, "Please Enter Key And Message", Toast.LENGTH_SHORT).show();
        }
    }
    public void copy(View view) {
        String encrypted = message.getText().toString();
        if(encrypted.length()>0) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("lable", encrpyted);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Your Message Was Copied to clip board", Toast.LENGTH_SHORT).show();
            b2.setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(this, "Please Create a Encrypted message", Toast.LENGTH_SHORT).show();
        }
    }
    public void paste(View view) {
        try { if(message.getText().toString().length()>0){
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
            String pastedata = item.getText().toString();
            Toast.makeText(this, "Your Message Was Pasted to clip board", Toast.LENGTH_SHORT).show();
            et_message.setText(pastedata);}
            else{
            Toast.makeText(this, "Please Copy the Message", Toast.LENGTH_SHORT).show();
        }
        }
        catch(Exception  e){
            e.printStackTrace();
        }
    }
    public void decrypt(View view) throws GeneralSecurityException {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if(et_key.length()>0&&et_message.length()>0){

        try {if(et_key.getText().toString().equals(inputkey)){
            String encrpyted = AESCrypt.decrypt(et_key.getText().toString(), et_message.getText().toString());
            et_message.setText("");
            et_key.setText("");
            message.setText(String.format("%s", encrpyted));}
            else{
            Toast.makeText(this, "Please Enter Right Key", Toast.LENGTH_SHORT).show();
        }

        }
        catch (GeneralSecurityException e){
            e.printStackTrace();
        }}
        else{
            Toast.makeText(this, "Please Enter Encrypted Message And Key ", Toast.LENGTH_SHORT).show();
        }



    }
    public void reset(View view){
        et_key.setText("");
        et_message.setText("");
        message.setText("");
        b2.setVisibility(View.INVISIBLE);
        b1.setVisibility(View.INVISIBLE);
        Toast.makeText(this,"Reset",Toast.LENGTH_SHORT).show();
    }


    public void share(View view) {
        String text = message.getText().toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}

