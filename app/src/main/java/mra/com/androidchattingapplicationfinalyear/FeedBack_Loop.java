package mra.com.androidchattingapplicationfinalyear;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskedittext.MaskEditText;

import java.util.HashMap;

public class FeedBack_Loop extends AppCompatActivity {

    EditText feed,feedname;
    EditText feedcontact;
    Button feedsned;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back__loop);


        feed=(EditText)findViewById(R.id.Feed);
       // txt=(TextView)findViewById(R.id.devname);
        feedname=(EditText)findViewById(R.id.FeedName);
        feedcontact=(EditText) findViewById(R.id.FeedContact);
        feedsned=(Button) findViewById(R.id.FeedSend);
        progressDialog=new ProgressDialog(this);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        Wave wave=new Wave();
        progressBar.setIndeterminateDrawable(wave);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=FirebaseDatabase.getInstance().getReference("feedback");


        feedsned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedSend();

            }
        });
    }

    private void FeedSend() {
        final String sndfeed = feed.getText().toString();
        final String sendname = feedname.getText().toString();
        final String sendcont = feedcontact.getText().toString();

        mAuth = FirebaseAuth.getInstance();

        if (TextUtils.isEmpty(sendcont) || TextUtils.isEmpty(sendname) || TextUtils.isEmpty(sndfeed)) {
            progressDialog.dismiss();
            Toast.makeText(FeedBack_Loop.this, "Something is Missing....", Toast.LENGTH_SHORT).show();
        }
        else {
            String id = reference.push().getKey();
            feedback f = new feedback(id, sndfeed, sendname, sendcont);
            reference.child(id).setValue(f);
            Toast.makeText(FeedBack_Loop.this, "Feed Receive..!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }




}
