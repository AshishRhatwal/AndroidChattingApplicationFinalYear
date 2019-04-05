package mra.com.androidchattingapplicationfinalyear;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Registercheck extends AppCompatActivity {

    EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registercheck);

       ed=(EditText)findViewById(R.id.prac);

        Intent i = getIntent();
        String text = i.getStringExtra("Text");
// Now set this value to EditText
        ed.setText (i.getStringExtra("number") );
        //ed.setEnabled(false);

    }
}
