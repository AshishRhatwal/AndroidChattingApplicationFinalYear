package mra.com.androidchattingapplicationfinalyear.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mra.com.androidchattingapplicationfinalyear.AddUser;
import mra.com.androidchattingapplicationfinalyear.MessageActivity;
import mra.com.androidchattingapplicationfinalyear.R;

/**
 * Created by mr. A on 28-01-2019.
 */

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder>

{

private Context mcontext;
private List<AddUser> muser;
private DatabaseReference reference;
private FirebaseAuth mAuth;
Button Follow;
public CheckAdapter(Context mcontext,List<AddUser> muser)
{
 this.mcontext=mcontext;
 this.muser=muser;
}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view=LayoutInflater.from(mcontext).inflate(R.layout.testing,parent,false);
        return new CheckAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        final AddUser auser=muser.get(position);
        holder.username.setText(auser.getUsername());
        holder.user.setText(auser.getEmail());
        if(auser.getImgurl().equals("default"))
        {
            holder.img.setImageResource(R.drawable.baseline_account_box_white_18);
        }
        else
        {
            Glide.with(mcontext).load(auser.getImgurl()).into(holder.img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent intent = new Intent(mcontext, MessageActivity.class);
                    intent.putExtra("userid", auser.getId());
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
     public TextView username,user;
     public CircleImageView img;
     public Button follow,following;
    public ViewHolder(View itemView) {
        super(itemView);

        username=itemView.findViewById(R.id.n);
        img=itemView.findViewById(R.id.p);
        user=itemView.findViewById(R.id.user);





    }
}



}
