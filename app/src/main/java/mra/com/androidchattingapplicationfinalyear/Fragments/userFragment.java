package mra.com.androidchattingapplicationfinalyear.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import mra.com.androidchattingapplicationfinalyear.Adapter.CheckAdapter;
import mra.com.androidchattingapplicationfinalyear.Adapter.UserAdapter;
import mra.com.androidchattingapplicationfinalyear.AddUser;
import mra.com.androidchattingapplicationfinalyear.R;


public class userFragment extends Fragment {

  private RecyclerView recyclerView;
  private UserAdapter userAdapter;
  private List<AddUser> muser;
  private EditText search_user;
  private CheckAdapter checkAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user, container, false);
        //LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView=view.findViewById(R.id.recycler_view1);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        //recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        muser=new ArrayList<>();
         readuser();
         search_user=view.findViewById(R.id.searchicon);
         search_user.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 searchuser(s.toString().toLowerCase());
             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         });

        return view;
    }

    private void searchuser(final String s)
    {
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=FirebaseDatabase.getInstance().getReference("user").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    muser.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren())
                    {
                        AddUser user=snapshot.getValue(AddUser.class);
                        assert user != null;
                        assert firebaseUser != null;
                        if(!user.getId().equals(firebaseUser.getUid()))
                        {
                            muser.add(user);
                        }
                    }
                   // userAdapter=new UserAdapter(getContext(),muser,false);
                    //recyclerView.setAdapter(userAdapter);
                    checkAdapter=new CheckAdapter(getContext(),muser);
                    recyclerView.setAdapter(checkAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readuser() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                muser.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    AddUser user=snapshot.getValue(AddUser.class);

                    assert user != null;
                    assert firebaseUser != null;
                    if(!user.getId().equals(firebaseUser.getUid()))
                    {
                        muser.add(user);
                    }

                }

               // userAdapter=new UserAdapter(getContext(),muser,false);
               // recyclerView.setAdapter(userAdapter);
                checkAdapter=new CheckAdapter(getContext(),muser);
                recyclerView.setAdapter(checkAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void Friend_Request()
    {

    }


}
