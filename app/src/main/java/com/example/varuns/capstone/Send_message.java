package com.example.varuns.capstone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Send_message extends AppCompatActivity implements View.OnClickListener {

    //Global to denote if SMS permissions enabled
    private static final int SEND_PERMISSION = 0;

    Button sendButton;
    EditText phoneText;
    EditText messageText;
    ListView savedText;
    String phoneNo;
    String message;
    String currentNum = "";

    //Used to populate ListView
    ArrayList<String> messages = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        sendButton = findViewById(R.id.sendText);
        phoneText = findViewById(R.id.phoneText);
        try {
            if (getIntent().getExtras().getString("phoneNo").length() != 0)
                phoneText.setText(getIntent().getExtras().getString("phoneNo"), TextView.BufferType.EDITABLE);
        }catch (Exception e){
            System.out.println("Could not set Artisan's phone number");
        }
        messageText = findViewById(R.id.messageText);
        savedText = findViewById(R.id.listText);

        sendButton.setOnClickListener(this);

        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(Send_message.this, menu_activity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent1 = new Intent(Send_message.this, Send_message.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent2 = new Intent(Send_message.this, ReportsActivity.class);
                        startActivity(intent2);
                        break;

                }
                return true;
            }
        });
    }

    //Calls sendMessage then clears the message body
    public void onClick(View view) {
        sendMessage();
        messageText.getText().clear();
    }

    //Retrieves the entered phone number and message
    //Checks permissions to send SMS
    protected void sendMessage() {
        phoneNo = phoneText.getText().toString();
        message = messageText.getText().toString();

        if(currentNum.isEmpty())
            currentNum = phoneNo;

        if(!phoneNo.matches("\\d+")) {
            Toast.makeText(getApplicationContext(),
                    "Could not send, phone number is not an integer", Toast.LENGTH_LONG).show();
        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED){
            //Permission granted, proceed to send message
            try {
                processMessage();
            }
            catch(Exception e){
                System.out.println("Could not handle message processing request");
            }
        }
        else{
            //Ask the user to enable SMS permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SEND_PERMISSION);
        }
    }

    //If permissions are granted, process sending the message
    public void processMessage(){
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> split;

        if(message.length() > 160) {
            split = smsManager.divideMessage(message);

            for(int i = 0; i < split.size(); i++)
                smsManager.sendTextMessage(phoneNo, null, split.get(i), null, null);
        }
        else{
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
        }
        Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();

        if(!currentNum.equals(phoneNo)) {
            messages = new ArrayList<>();
            currentNum = phoneNo;
        }

        messages.add(message);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, messages);
        savedText.setAdapter(adapter);
    }

    //Override called after requestPermissions
    //If permission granted (for first message after asking), send messages
    //If not, display Toast pop-up indicating failure
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode){
            case SEND_PERMISSION:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    processMessage();
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Could not send, must enable SMS Permissions", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}