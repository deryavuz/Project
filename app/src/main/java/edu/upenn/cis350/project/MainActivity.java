package edu.upenn.cis350.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

import edu.upenn.cis350.crawler.Crawler;

public class MainActivity extends AppCompatActivity {

    Button forgotPass;
    Button shareButton;
    private FirebaseAuth mAuth;
   // EditText email;
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
    private String m_Text = "";
    Crawler c = new Crawler("philadelphia", "pa", "camping", "2020-07-01");


//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //email = findViewById(R.id.email_field);
        forgotPass = findViewById(R.id.forgot_button);
        shareButton = findViewById(R.id.share_button);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popUpWindowPassword();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popUpWindowEvent();
            }
        });



    }


    private static String generatePassword(final int sizeOfRandomString) {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    protected void shareEventEmail(String shareEmail){

        String databaseEmail;
        String eventLink = "";

        String[] TO = {shareEmail};
        //String[] CC = {"mcmohd@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Here is an interesting event for you!");
        emailIntent.putExtra(Intent.EXTRA_TEXT, eventLink);

        try{
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Sent email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void sendPasswordEmail(String toSend) {
        Log.i("Send email", "");

        String databaseEmail = "";

        final int sizePass = 10;
        String[] TO = {toSend};
        String newRandom =  generatePassword(sizePass);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        if(databaseEmail.equals("") || databaseEmail== null){
            Toast.makeText(getApplicationContext(), "This email does not exist. Please enter another one.", Toast.LENGTH_SHORT);

        }

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your new password is...");
        emailIntent.putExtra(Intent.EXTRA_TEXT, newRandom);

        //if email is already registered, send a random password.

        try{
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Sent email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void popUpWindowPassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter email for password");

    // Set up the input
        final EditText input = new EditText(this);
    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

    // Set up the buttons
        builder.setPositiveButton("Send Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                sendPasswordEmail(m_Text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    protected void popUpWindowEvent(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter email to share");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Share event!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                shareEventEmail(m_Text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
