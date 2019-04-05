package mra.com.androidchattingapplicationfinalyear;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    AwesomeValidation awesomeValidation;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    RelativeLayout rel1,rel2;
    EditText uname,pass;
    Button loginuser;
    TextView signup,forgot_pass;

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        progressDialog = new ProgressDialog(this);
        progressBar = (ProgressBar) findViewById(R.id.Spinkit);
        Wave wave=new Wave();
        progressBar.setIndeterminateDrawable(wave);
        mAuth = FirebaseAuth.getInstance();
        uname = (EditText) findViewById(R.id.uname);
        pass = (EditText) findViewById(R.id.password);
        //rel1 = (RelativeLayout) findViewById(R.id.rel1);
        rel2 = (RelativeLayout) findViewById(R.id.rel2);
        loginuser = (Button) findViewById(R.id.LoginUser);
        signup = (TextView) findViewById(R.id.sign_up);
        forgot_pass = (TextView) findViewById(R.id.forgot);
        awesomeValidation.addValidation(LoginPage.this, R.id.uname, Patterns.EMAIL_ADDRESS, R.string.email);


            //checking user is empty or not
        user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Intent i=new Intent(LoginPage.this,ChattWindow.class);
            startActivity(i);
            finish();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this,BeforeSignup.class));
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this,FeedBack_Loop.class));
            }
        });
        loginuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=uname.getText().toString();
                String Pass=pass.getText().toString();

                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(Pass))
                {
                    Toast.makeText(LoginPage.this,"Check Input Field",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    loginuser.setVisibility(View.GONE);
                    mAuth.signInWithEmailAndPassword(email,Pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //progressDialog.dismiss();



                                    if(task.isSuccessful())
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        Intent i=new Intent(LoginPage.this,ChattWindow.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                    else
                                    {
                                        loginuser.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(LoginPage.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });


    }

    @Override
    public void onClick(View v) {


        if(awesomeValidation.validate())
        {
            //Toast.makeText(this,"g",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"Something Is Missing...Please Try Again",Toast.LENGTH_SHORT).show();
        }

    }
}
