package mra.com.androidchattingapplicationfinalyear;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LockSecurity1 extends AppCompatActivity {

    private EditText newpassword,previouspassword;
    private Button prevuousbtn,set,clear;
    private Intent intent;
    private String userid;
    private FirebaseUser user;
    private RelativeLayout lock,setlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_security1);

        newpassword=(EditText)findViewById(R.id.np);
       // previouspassword=(EditText)findViewById(R.id.prepass);
        set=(Button)findViewById(R.id.sp);
        clear=(Button)findViewById(R.id.clearpass);
        //prevuousbtn=(Button)findViewById(R.id.unlock);
        //lock=(RelativeLayout)findViewById(R.id.lockrel);
        //setlock=(RelativeLayout)findViewById(R.id.setlock);

        userid=getIntent().getStringExtra("userid");

        user= FirebaseAuth.getInstance().getCurrentUser();


        setPass();
        clearpass();
      //  previous();



    }

    private void setPass()
    {
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String newPassword=newpassword.getText().toString();

                    if(TextUtils.isEmpty(newPassword))
                    {
                        Toast.makeText(LockSecurity1.this, "Empty Password :)", Toast.LENGTH_SHORT).show();

                    }
                    else if(newPassword.length()<=5)
                    {
                        Toast.makeText(LockSecurity1.this, "6 digits Password :)", Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("setPass").child(user.getUid())
                                    .child(userid);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("lockstatus", newPassword);

                            reference.updateChildren(hashMap);
                            Toast.makeText(LockSecurity1.this, "Password Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    // reference.setValue(p);


                }
                catch (Exception e)
                {
                    Toast.makeText(LockSecurity1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void previous()
    {

    }

    private void clearpass()
    {
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("setPass").child(user.getUid())
                            .child(userid);
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("lockstatus","default");

                    reference.updateChildren(hashMap);
                    Toast.makeText(LockSecurity1.this,"Password Cleared",Toast.LENGTH_SHORT).show();
                    finish();


                }
                catch (Exception e)
                {
                    Toast.makeText(LockSecurity1.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}
