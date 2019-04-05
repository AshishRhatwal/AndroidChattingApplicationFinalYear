package mra.com.androidchattingapplicationfinalyear.Adapter;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import mra.com.androidchattingapplicationfinalyear.AddUser;
import mra.com.androidchattingapplicationfinalyear.Chat;
import mra.com.androidchattingapplicationfinalyear.ChatList;
import mra.com.androidchattingapplicationfinalyear.MessageActivity;
import mra.com.androidchattingapplicationfinalyear.R;

/**
 * Created by mr. A on 09-01-2019.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>
{
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context mcontext;
    private List<Chat> mchat;
    private String imgurl;
    FirebaseUser fuser;
    public MessageAdapter(Context mcontext, List<Chat> mchat ,String imgurl)
    {
        this.mchat=mchat;
        this.mcontext=mcontext;
        this.imgurl=imgurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT){
        View view= LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right,parent,false);
        return new MessageAdapter.ViewHolder(view);
    }
    else
        {
            View view= LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

          Chat chat=mchat.get(position);
          holder.show_message.setText(chat.getMessage());

        if(imgurl.equals("default"))
        {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher_round);
        }
        else
        {
            Glide.with(mcontext).load(imgurl).into(holder.profile_image);
        }

        if(position==mchat.size()-1)
        {
            if(chat.isIsseen())
            {
                holder.txt_seen1.setText("Seen");
            }
            else
            {
                holder.txt_seen1.setText("Delivered");
            }
        }
        else
        {
            holder.txt_seen1.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen1;

        public ViewHolder(View view)
        {
            super(view);

            show_message=view.findViewById(R.id.show_message);
            profile_image=view.findViewById(R.id.profile_image);
            txt_seen1=view.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(mchat.get(position).getSender().equals(fuser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }


}

