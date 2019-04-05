package mra.com.androidchattingapplicationfinalyear;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mra.com.androidchattingapplicationfinalyear.Adapter.MessageAdapter;
import mra.com.androidchattingapplicationfinalyear.Fragments.APIService;
import mra.com.androidchattingapplicationfinalyear.Notification.Client;
import mra.com.androidchattingapplicationfinalyear.Notification.Data;
import mra.com.androidchattingapplicationfinalyear.Notification.MyResponse;
import mra.com.androidchattingapplicationfinalyear.Notification.Sender;
import mra.com.androidchattingapplicationfinalyear.Notification.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {


    CircleImageView profile_image;
    TextView username;
    ImageButton btn_send;
    EditText text_send,unlocktext;
    Button unlockbtn;
    FirebaseUser fuser;
    DatabaseReference reference;
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;
    Intent intent,intent1;
    ValueEventListener seenListner;
    private String userid,uid;
    TextView errormsg;
    private RelativeLayout re1chat,rellock;
    APIService apiService;
    boolean notify =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar=findViewById(R.id.toolbar);
        re1chat=findViewById(R.id.msgrel);
        rellock=findViewById(R.id.lockrel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this,ChattWindow.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });



        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        text_send=findViewById(R.id.text_send);
        btn_send=findViewById(R.id.btn_send);
        unlocktext=findViewById(R.id.logpass);
        unlockbtn=findViewById(R.id.unlock);
        errormsg=findViewById(R.id.errormsg);

        intent=getIntent();
         userid=intent.getStringExtra("userid");

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        unlockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    final String getpassword = unlocktext.getText().toString().trim();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("setPass").child(fuser.getUid())
                    .child(userid);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            SetPass setPass=dataSnapshot.getValue(SetPass.class);

                            if(TextUtils.isEmpty(getpassword))
                            {
                                Toast.makeText(MessageActivity.this, "Empty Password :)", Toast.LENGTH_SHORT).show();
                            }

                            else if(setPass.getLockstatus().equals(getpassword))
                            {
                                re1chat.setVisibility(View.VISIBLE);
                                rellock.setVisibility(View.GONE);
                                Toast.makeText(MessageActivity.this,"Checking Password",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                errormsg.setText("Wrong Password");
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                catch (Exception e)
                {
                    Toast.makeText(MessageActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                String msg=text_send.getText().toString();
                if(!msg.equals(""))
                {
                    sendMessage(fuser.getUid(),userid,msg);
                }
                else
                {
                    Toast.makeText(MessageActivity.this,"Message Type karna Bhul gaye",Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });


        reference= FirebaseDatabase.getInstance().getReference("user").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AddUser user=dataSnapshot.getValue(AddUser.class);
                username.setText(user.getUsername());
                if(user.getImgurl().equals("default"))
                {
                    profile_image.setImageResource(R.mipmap.ic_launcher_round);
                }
                else
                {
                    Glide.with(getApplicationContext()).load(user.getImgurl()).into(profile_image);
                }
                readMessage(fuser.getUid(),userid,user.getImgurl());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        seenMessage(userid);
        calling();



    }

    private void calling()
    {
        try {

            final DatabaseReference reference=FirebaseDatabase.getInstance().getReference("setPass").child(fuser.getUid())
                    .child(userid);
            String id=reference.push().getKey();
            SetPass setPass=new SetPass(id,"default");
            //reference.setValue(setPass);


            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists())
                    {
                        reference.child("id").setValue(userid);
                        reference.child("lockstatus").setValue("default");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Toast.makeText(MessageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SetPass setPass1=dataSnapshot.getValue(SetPass.class);

                    if(setPass1.getLockstatus().equals("default"))
                    {
                        re1chat.setVisibility(View.VISIBLE);
                        rellock.setVisibility(View.GONE);
                    }

                    else
                    {
                        re1chat.setVisibility(View.GONE);
                        rellock.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Toast.makeText(MessageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(MessageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private void seenMessage(final String userid)
    {
        reference=FirebaseDatabase.getInstance().getReference("chats");
        seenListner=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(fuser.getUid())&&chat.getSender().equals(userid))
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);

        reference.child("chats").push().setValue(hashMap);

        //add user to chat Fragment
        final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("chatlist")
                .child(fuser.getUid())
                .child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final String msg=message;
        reference=FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AddUser user=dataSnapshot.getValue(AddUser.class);
                if(notify) {
                    sendNotification(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendNotification(String receiver, final String username, final String message)
    {
        DatabaseReference token=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query =token.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Token token=snapshot.getValue(Token.class);
                    Data data=new Data(fuser.getUid(),R.mipmap.ic_launcher,username+" :- "+message,"New Message Arrived"
                    ,userid);

                    Sender sender=new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code()==20)
                                    {
                                        if(response.body().success!=1)
                                        {
                                            Toast.makeText(MessageActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void readMessage(final String myid, final String userid, final String imgurl)
    {
        mchat=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid)&&chat.getSender().equals(userid)||
                    chat.getReceiver().equals(userid)&& chat.getSender().equals(myid))
                    {
                        mchat.add(chat);
                    }
                    messageAdapter=new MessageAdapter(MessageActivity.this,mchat,imgurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void status(String status)
    {
        reference=FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListner);
        status("offline");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lockmenu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.setlock:
                Intent intent = new Intent(MessageActivity.this, LockSecurity1.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
                return true;
        }
        return false;
    }




}
