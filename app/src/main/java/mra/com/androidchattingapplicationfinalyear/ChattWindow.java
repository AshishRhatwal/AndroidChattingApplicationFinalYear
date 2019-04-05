package mra.com.androidchattingapplicationfinalyear;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import mra.com.androidchattingapplicationfinalyear.Fragments.chatFragment;
import mra.com.androidchattingapplicationfinalyear.Fragments.profileFragment;
import mra.com.androidchattingapplicationfinalyear.Fragments.userFragment;

public class ChattWindow extends AppCompatActivity {

  private CircleImageView profile_image;
  private TextView username;
  private TextView emailname;
  private DatabaseReference reference;
  private FirebaseUser firebaseUser;
  private FirebaseAuth mAuth;


  //private PagerViewAdapter mPagerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatt_window);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        profile_image=(CircleImageView) findViewById(R.id.profile_image);
        username=(TextView)findViewById(R.id.username);
        emailname=(TextView)findViewById(R.id.email);
        mAuth=FirebaseAuth.getInstance();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               try {
                   AddUser user = dataSnapshot.getValue(AddUser.class);
                   username.setText(user.getUsername());
                   emailname.setText(user.getEmail());
                   if(user.getImgurl().equals("default"))
                   {
                       profile_image.setImageResource(R.mipmap.ic_launcher_round);
                   }
                   else
                   {
                       Glide.with(getApplicationContext()).load(user.getImgurl()).into(profile_image);
                   }
               }
               catch (Exception e)
               {
                   Toast.makeText(ChattWindow.this,e.getMessage(),Toast.LENGTH_SHORT).show();
               }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final TabLayout tabLayout=findViewById(R.id.tablayout);
        final ViewPager viewPager=findViewById(R.id.view_pager);


          reference=FirebaseDatabase.getInstance().getReference("chats");
          reference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                  int unread = 0;
                  for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                      Chat chat = snapshot.getValue(Chat.class);
                      if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()) {
                          unread++;
                      }
                  }
                  if (unread == 0) {
                      viewPagerAdapter.addFragment(new chatFragment(), "");

                  } else {
                      viewPagerAdapter.addFragment(new chatFragment(), "(" + unread + ")");
                  }
                  viewPagerAdapter.addFragment(new userFragment(), "");
                  viewPagerAdapter.addFragment(new profileFragment(), "");

                  viewPager.setAdapter(viewPagerAdapter);
                  tabLayout.setupWithViewPager(viewPager);

                  tabLayout.getTabAt(0).setIcon(R.drawable.baseline_forum_white_36);
                  tabLayout.getTabAt(1).setIcon(R.drawable.baseline_people_white_36);
                  tabLayout.getTabAt(2).setIcon(R.drawable.baseline_person_pin_white_36);
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });

         // viewPagerAdapter.addFragment(new chatFragment(),"CHAT");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Logout11:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ChattWindow.this,LoginPage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter
    {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position)
        {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment,String title)
        {
            fragments.add(fragment);
            titles.add(title);
        }
 //ctrl+o
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status)
    {
        reference=FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());

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
        status("offline");
    }
}
