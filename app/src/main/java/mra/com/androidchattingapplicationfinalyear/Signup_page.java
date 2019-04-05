package mra.com.androidchattingapplicationfinalyear;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
//import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskedittext.MaskEditText;

import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Signup_page extends AppCompatActivity implements View.OnClickListener{
       //objects of EditsText And Button
        EditText RegisterName,RegisterPassword,RegisterUserName,Rlname,Receive_OTP;
        MaskEditText mobilenumber;
        Button RegisterButton;
        TextView txt;
        //ProcessDialog
        ProgressDialog progressDialog;
        //FireBase Objects
        private FirebaseAuth mAuth;
        private FirebaseDatabase database;
        private DatabaseReference reference;
        ProgressBar progressBar,codebar,regbtnprogrss;
        //validaton
        AwesomeValidation awesomeValidation;
        //Otp tools
      Button send_otp1;
      int btn=0;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    String mVerificationId;
    RelativeLayout relotp,relform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        //Firebase Objects

        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        //End
        progressBar=(ProgressBar)findViewById(R.id.pbar);
        Wave wave=new Wave();
        //progressBar.setIndeterminateDrawable(wave);
        codebar=(ProgressBar)findViewById(R.id.cbar);
        codebar.setIndeterminateDrawable(wave);
        regbtnprogrss=(ProgressBar)findViewById(R.id.progressreg);
        regbtnprogrss.setIndeterminateDrawable(wave);
        RegisterName=(EditText) findViewById(R.id.Regemail);
        RegisterPassword=(EditText)findViewById(R.id.Regpassword);
        RegisterUserName=(EditText)findViewById(R.id.RegUname);
        Rlname=(EditText)findViewById(R.id.lname);
        RegisterButton=(Button)findViewById(R.id.login);
        send_otp1=(Button)findViewById(R.id.send_otp1);
        Receive_OTP=(EditText)findViewById(R.id.Rec_otp);
        mobilenumber=(MaskEditText) findViewById(R.id.phoneNumber);
        relotp=(RelativeLayout) findViewById(R.id.Layout_OTP1);
        relform=(RelativeLayout) findViewById(R.id.Layout_Form1);
        txt=(TextView)findViewById(R.id.txtmob);
        Intent i = getIntent();
        String text = i.getStringExtra("Text");
        // Now set this value to EditText
        mobilenumber.setText (i.getStringExtra("number") );
        //ed.setEnabled(false);
        progressDialog=new ProgressDialog(this);
        //validation
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation=new AwesomeValidation(ValidationStyle.COLORATION);
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(Signup_page.this,R.id.Regemail, Patterns.EMAIL_ADDRESS,R.string.email);
        //awesomeValidation.addValidation(Signup_page.this,R.id.Regpass,"[a-zA-Z\\s]+" ,R.string.pass);
        awesomeValidation.addValidation(Signup_page.this,R.id.username, "[a-zA-Z\\s]+",R.string.name);
        awesomeValidation.addValidation(Signup_page.this,R.id.lname, "[a-zA-Z\\s]+" ,R.string.mob);
        //Validation Ends
        RegisterButton.setOnClickListener(this);


        send_otp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String PhoneNumber = mobilenumber.getText().toString();

                if(TextUtils.isEmpty(PhoneNumber))
                {
                    Toast.makeText(Signup_page.this,"Enter Mobile Number",Toast.LENGTH_SHORT).show();
                    //send_otp1.setEnabled(false);

                }
                else {
                    send_otp1.setEnabled(true);
                    progressBar.setVisibility(View.VISIBLE);

                if(btn==0)
                {

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                PhoneNumber,
                                60,
                                TimeUnit.SECONDS,
                                Signup_page.this,
                                mcallbacks

                        );
                    send_otp1.setVisibility(View.GONE);
                    FirebaseAuth.getInstance().signOut();
                    //mobilenumber.setVisibility(View.GONE);
                }
                else
                {

                    progressBar.setVisibility(View.VISIBLE);
                    //codebar.setVisibility(View.VISIBLE);
                    send_otp1.setVisibility(View.GONE);
                    //rel12.setVisibility(View.GONE);
                    String vlaid=Receive_OTP.getText().toString();
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerificationId,vlaid);
                    signInWithPhoneAuthCredential(credential);
                    Toast.makeText(Signup_page.this,"Verifing Entered OTP!",Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();


                }}

            }
        });



        mcallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(Signup_page.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                finish();

            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Receive_OTP.setVisibility(View.VISIBLE);
                mobilenumber.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                send_otp1.setVisibility(View.VISIBLE);
                txt.setVisibility(View.GONE);

                mVerificationId = verificationId;
                mResendToken = token;
                btn=1;
                send_otp1.setText("verify Otp");
                //FirebaseAuth.getInstance().signOut();
               // Receive_OTP.setText("Send");




            }
        };

    }


    //Onstart Method For Database CurrentUser
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            //Handle The Logegin User
        }

    }
  //Important Method  For Authentatication And dataBase Entering
    private void register( )
    {
        //fetching Values
        final String email=RegisterName.getText().toString().trim();
        final String password=RegisterPassword.getText().toString().trim();
        final String username=RegisterUserName.getText().toString().trim();
        final String lname=Rlname.getText().toString().trim();
        final String mobile=mobilenumber.getText().toString().trim();
        mAuth=FirebaseAuth.getInstance();

       //validation
        if(TextUtils.isEmpty(password)||TextUtils.isEmpty(username)||TextUtils.isEmpty(lname))
        {
            Toast.makeText(this," Provide All Information",Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<6)
        {
            Toast.makeText(this,"Password Minimum 6 digits",Toast.LENGTH_SHORT).show();
            return;
        }


        //end Validation
        //ProgressDialog

        regbtnprogrss.setVisibility(View.VISIBLE);
        RegisterButton.setVisibility(View.GONE);

        reference = FirebaseDatabase.getInstance().getReference("user");
        reference.orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()!=null)
                        {
                            Toast.makeText(Signup_page.this,"UserName Already Used",Toast.LENGTH_SHORT).show();
                            regbtnprogrss.setVisibility(View.GONE);
                            RegisterButton.setVisibility(View.VISIBLE);

                        }

                        else
                        {
                            mAuth.createUserWithEmailAndPassword(email,password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            regbtnprogrss.setVisibility(View.GONE);
                                            if(task.isSuccessful())
                                            {
                                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                                assert firebaseUser != null;
                                                String userid = firebaseUser.getUid();
                                                reference = FirebaseDatabase.getInstance().getReference("user").child(userid);

                                                HashMap<String, String> hashMap = new HashMap<>();
                                                hashMap.put("id", userid);
                                                hashMap.put("username", username);
                                                hashMap.put("fullname", lname);
                                                hashMap.put("email", email);
                                                hashMap.put("phonenumber", mobile);
                                                hashMap.put("imgurl", "default");
                                                hashMap.put("status", "offline");
                                                hashMap.put("search", username.toLowerCase());

                                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Signup_page.this, "Registered Sucessfully...", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }
                                                });

                                            }
                                            else
                                            {
                                                Toast.makeText(Signup_page.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                regbtnprogrss.setVisibility(View.GONE);
                                                RegisterButton.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });




    }


    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        String phone=mobilenumber.getText().toString();
                        reference = FirebaseDatabase.getInstance().getReference("user");
                        reference.orderByChild("phonenumber").equalTo(phone)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue()!=null)
                                        {
                                            Toast.makeText(Signup_page.this,"Number Is Already Registered....\nPlease Enter Email and Password",Toast.LENGTH_LONG).show();
                                            FirebaseAuth.getInstance().signOut();
                                           finish();
                                        }
                                        else
                                        {
                                            FirebaseUser user=task.getResult().getUser();
                                            relotp.setVisibility(View.GONE);
                                            relform.setVisibility(View.VISIBLE);
                                            codebar.setVisibility(View.GONE);

                                            if(task.isSuccessful())
                                            {

                                                mAuth=FirebaseAuth.getInstance();
                                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                                assert firebaseUser != null;
                                                String userid = firebaseUser.getUid();
                                                reference = FirebaseDatabase.getInstance().getReference("MobileAuthUser").child(userid);
                                                HashMap<String,String> hashMap=new HashMap<>();
                                                hashMap.put("mobileid",userid);

                                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            //Toast.makeText(Signup_page.this,"",Toast.LENGTH_SHORT).show();
                                                            FirebaseAuth.getInstance().signOut();
                                                        }
                                                    }
                                                });
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                    }
                });
    }


    @Override
    public void onClick(View view)
    {
        if(view==RegisterButton)
        {
            register();
        }
        if(awesomeValidation.validate())
        {
            //Toast.makeText(this,"g",Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Toast.makeText(this,"Something Is Missing...Please Try Again",Toast.LENGTH_SHORT).show();
        }

    }
}
