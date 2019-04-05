package mra.com.androidchattingapplicationfinalyear.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import mra.com.androidchattingapplicationfinalyear.AddUser;
import mra.com.androidchattingapplicationfinalyear.Chat;
import mra.com.androidchattingapplicationfinalyear.LockSecurity;
import mra.com.androidchattingapplicationfinalyear.LockSecurity1;
import mra.com.androidchattingapplicationfinalyear.MessageActivity;
import mra.com.androidchattingapplicationfinalyear.R;

/**
 * Created by mr. A on 09-01-2019.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>
{
    private Context mcontext;
    private List<AddUser> muser;
    private boolean ischat;
    String theLastMessage;

    public UserAdapter(Context mcontext, List<AddUser> muser ,boolean ischat)
    {
        this.muser=muser;
        this.mcontext=mcontext;
        this.ischat=ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.user_list,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         final AddUser addUser=muser.get(position);
        holder.username.setText(addUser.getUsername());

        if(addUser.getImgurl().equals("default"))
        {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher_round);
        }
        else
        {
            Glide.with(mcontext).load(addUser.getImgurl()).into(holder.profile_image);
        }
        if(ischat)
        {
            lastMessage(addUser.getId(),holder.last_msg);
        }
        else
        {
            holder.last_msg.setVisibility(View.GONE);
        }

        if(ischat)
        {
            if(addUser.getStatus().equals("online"))
            {
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);

            }
            else
            {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }

        }
        else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   //AddUser user=new AddUser();

                Intent intent = new Intent(mcontext, MessageActivity.class);
                intent.putExtra("userid", addUser.getId());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return muser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView username;
        public ImageView profile_image;
        private ImageView img_on,img_off;
        private TextView last_msg;


        public ViewHolder(View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username1);
            profile_image=itemView.findViewById(R.id.profile_image1);
            img_on=itemView.findViewById(R.id.img_on);
            img_off=itemView.findViewById(R.id.img_off);
            last_msg=itemView.findViewById(R.id.last_msg);

        }
    }

    public void lastMessage(final String userid, final TextView last_msg)
    {
        theLastMessage="default";
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    try {
                        Chat chat = snapshot.getValue(Chat.class);
                        assert chat != null;
                        assert firebaseUser != null;
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                    catch(Exception e)
                        {
                            Toast.makeText(mcontext,"Logged Out",Toast.LENGTH_SHORT).show();


                        }


                }
                switch (theLastMessage)
                {
                    case "default":
                        last_msg.setText("no message");
                        break;
                    default:
                        last_msg.setText(theLastMessage);
                }
                theLastMessage="default";
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(mcontext,databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}
