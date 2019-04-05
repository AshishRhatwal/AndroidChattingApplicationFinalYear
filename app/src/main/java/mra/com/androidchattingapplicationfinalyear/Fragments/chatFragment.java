package mra.com.androidchattingapplicationfinalyear.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import mra.com.androidchattingapplicationfinalyear.Adapter.UserAdapter;
import mra.com.androidchattingapplicationfinalyear.AddUser;
import mra.com.androidchattingapplicationfinalyear.Chat;
import mra.com.androidchattingapplicationfinalyear.ChatList;
import mra.com.androidchattingapplicationfinalyear.Notification.Token;
import mra.com.androidchattingapplicationfinalyear.R;


public class chatFragment extends Fragment  {


    private RecyclerView recycler_view;
    private UserAdapter userAdapter;
    private List<AddUser> muser;
    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;

    private List<ChatList> userlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        recycler_view=view.findViewById(R.id.recycler_view1);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser=FirebaseAuth.getInstance().getCurrentUser();
        userlist=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userlist.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    ChatList chatlist=snapshot.getValue(ChatList.class);
                    userlist.add(chatlist);
                }
                chatList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        updateToken(FirebaseInstanceId.getInstance().getToken());


         return view;
    }
    private  void updateToken(String token)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(fuser.getUid()).setValue(token1);

    }

    private void chatList()
    {
        muser=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                muser.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {

                    AddUser user=snapshot.getValue(AddUser.class);
                    for(ChatList chatList:userlist)
                    {
                        if(user.getId().equals(chatList.getId()))
                        {
                            muser.add(user);
                        }
                    }
                }
                userAdapter=new UserAdapter(getContext(),muser,true);
                recycler_view.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
