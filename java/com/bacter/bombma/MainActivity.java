package com.bacter.bombma;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
{
    /** ALERT DIALOG ON EXIT **/
    static final String EXIT_BOX_CANCEL = "NO(VERY GOOD!)";
    static final String EXIT_BOX_CONTENT = "Please Don't Exit.... Keep Bombing You IDIOT!";
    static final String EXIT_BOX_OK = "YES, I'LL EXIT";
    static final String EXIT_BOX_TITLE = "Bacter Bomb-Bomber";

    /** ID CONSTANTS **/
    private static final int REQUEST_PERMISSION = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 3;

    /** UI COMPONENTS **/
    private TextView targetNameTextView;
    private EditText messageEditText, quantityEditText;
    private ImageView targetImageView;

    /** INSTANCE VARIABLES **/
    private Uri contact;
    private String contactId, contactNumber;
    private SmsManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        /*INITIALIZING UI COMPONENTS*/
        messageEditText = findViewById(R.id.messageEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        targetImageView = findViewById(R.id.imageView);
        targetNameTextView = findViewById(R.id.textView);
        smsManager = SmsManager.getDefault();
        ArrayList<Integer>segments = new ArrayList<>(Arrays.asList(1,6,0));
        Collections.sort(segments, Integer::compareTo);
        getFlag(segments);
    }
    public void pickContact(View view)
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }
    public void sendText(View view)
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SEND_SMS);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                {   // Granted
                    if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                    {
                        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),REQUEST_PERMISSION);
                    }
                    // Blocked
                    else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS))
                    {
                        AlertDialog.Builder build = new AlertDialog.Builder(this);
                        build
                                .setTitle("PERMISSION WAS BLOCKED!")
                                .setMessage("You have previously blocked this app from accessing contacts, This App will not function without this access. Would you like to go to Settings and ALLOW this Permission?")
                                .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        goToSettings();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                    // Denied
                    else
                        {
                            new AlertDialog.Builder(this)
                                    .setTitle("PERMISSION WAS DENIED")
                                    .setMessage("This App will not function without access to Contacts, Would you like to Allow Access?")
                                    .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    return;
                }
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
                {    // Granted
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        sendAhukBombs();
                    {// Blocked
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS))
                        {
                            new AlertDialog.Builder(this)
                                    .setTitle("PERMISSION WAS BLOCKED!")
                                    .setMessage("You have previously blocked this app from sending SMS. This app will not function wothout this access. Would you like to go to settings and allow this permission?")
                                    // open settings button
                                    .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SEND_SMS);
                                        }
                                    })
                                    // Denied, Close App
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                }
        }
    }
    private String getFlag(ArrayList<Integer> segments)
    {
        String flag = "getFlag{_____";
        for (int i = 0; i < 3; i++)
        {
            flag += new String(Base64.decode(getResources().getQuantityString(R.plurals.flag_segments, segments.get(i)),Base64.DEFAULT));
            if (i < 2)
            {
                flag += "_";
            }
        }
        return flag + "__}";
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION && resultCode == RESULT_OK) {
            contact = data.getData();
            retrieveContactName();
            retrieveContactNumber();
            retrieveContactPhoto();
        }
    }
    private void retrieveContactName() {

        Cursor cursor = getContentResolver().query(contact, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()){
            targetNameTextView.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            cursor.close();
        }
    }
    private void retrieveContactNumber()
    {
        contactNumber = null;
        // Querying Contact ID
        Cursor cursor = getContentResolver().query(contact, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if (cursor != null && cursor.moveToFirst()){
            contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            cursor.close();
        }

        // Retrieving Mobile Number
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{contactId}, null);

        // Storing Mobile Number
        if (cursor != null && cursor.moveToFirst()){
            contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            cursor.close();
        }
    }
    private void retrieveContactPhoto() {

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactId)));

            if (inputStream != null){
                Bitmap photo = BitmapFactory.decodeStream(inputStream);
                targetImageView.setImageBitmap(photo);
                inputStream.close();
            }else{
                targetImageView.setImageResource(R.mipmap.ic_launcher);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void sendAhukBombs()
    {
        if (contact == null || contact.toString().equals(""))
        {
            Toast.makeText(this,"You must Select a Target, YOU IDIOT!",Toast.LENGTH_SHORT).show();
        }
        else if (contactNumber == null || contactNumber.equals(""))
        {
            Toast.makeText(this,"Target has no associated mobile number!",Toast.LENGTH_SHORT).show();
        }
        else if (quantityEditText.getText().toString().equals(""))
        {
            Toast.makeText(this,"An Amount was not specified",Toast.LENGTH_SHORT).show();
        }
        else
            {
                int bombs = Integer.parseInt(quantityEditText.getText().toString());
                try
                {
                    for (int i = 0; i < bombs; i++)
                    {
                        smsManager.sendTextMessage(contactNumber, null, messageEditText.getText().toString(),null,null);
                    }
                    Toast.makeText(this,"Ahuk Bomns Deployed!",Toast.LENGTH_SHORT).show();
                }
                catch (IllegalArgumentException e)
                {
                    Toast.makeText(this,"Ahuk Bombs Failed!",Toast.LENGTH_SHORT).show();
                }
            }
    }
    private void goToSettings(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_PERMISSION);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_themes)
        {
            return true;
        }
        if (id == R.id.action_about)
        {
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_exit)
        {
            exitApp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void exitApp()
    {
        AlertDialog.Builder ahuk = new AlertDialog.Builder(this);
        ahuk.setIcon(R.drawable.alert);
        ahuk.setTitle(EXIT_BOX_TITLE);
        ahuk.setMessage(EXIT_BOX_CONTENT);
        ahuk.setPositiveButton(EXIT_BOX_OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                MainActivity.this.finish();
            }
        });
        ahuk.setCancelable(false);
        ahuk.setNegativeButton(EXIT_BOX_CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ahuk.show();
    }
}