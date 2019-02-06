package com.example.varuns.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Send_message extends AppCompatActivity implements View.OnClickListener {

    private static final int SEND_PERMISSION =0 ;
    Button sendButton;
    EditText phoneText;
    EditText messageText;
    String phoneNo;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        sendButton = findViewById(R.id.sendText);
        phoneText = findViewById(R.id.phoneText);
        messageText = findViewById(R.id.messageText);

        sendButton.setOnClickListener(this);
        messageText.setOnClickListener(this);
    }

    /*
    onClick
    - Calls sendMessage then clears the message body
     */
    public void onClick(View view) {
        sendMessage();
        messageText.getText().clear();
    }

    /*
    sendMessage
    - Retrieves the entered phone number and message
    - Checks permissions to send SMS
     */
    protected void sendMessage() {
        phoneNo = phoneText.getText().toString();
        message = messageText.getText().toString();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)){
            }

            else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        SEND_PERMISSION);
            }
        }
    }

    /*
    onRequestPermissionResult
    - Override that checks if permission has been granted
    - If so, send the message string to the number stored in the phone string
    - Displays Toast pop-up message to indicate if the message was sent
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode){
            case SEND_PERMISSION:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Could Not Send", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}