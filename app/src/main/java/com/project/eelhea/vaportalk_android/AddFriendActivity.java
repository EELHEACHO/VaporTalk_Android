package com.project.eelhea.vaportalk_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.eelhea.vaportalk_android.DTO.User;
import com.project.eelhea.vaportalk_android.DTO.UserContact;
import com.project.eelhea.vaportalk_android.DTO.UserContactFriend;
import com.project.eelhea.vaportalk_android.helper.UserMyFriendDBHelper;
import com.project.eelhea.vaportalk_android.view.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by eelhea on 2017. 4. 22..
 */

public class AddFriendActivity extends AppCompatActivity {

    private static final String TAG = "AddFriendActivity";
    private Toolbar toolbar;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private ArrayList<UserContact> userContactsArrayList; //연락처에 있는 사람
    private ArrayList<User> userArrayList; //firebase DB에 있는 모든 사용자

    private SectionedRecyclerViewAdapter sectionAdapter;

    private ArrayList<UserContactFriend> userFriendAccordingToType = new ArrayList<>();
    private ArrayList<UserContactFriend> alreadyFriendArrayList = new ArrayList<>(); // 이미 친구인 사람 목록
    private ArrayList<UserContactFriend> notFriendYetArrayList = new ArrayList<>(); //아직 친구가 아닌 사람 목록


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        //toolbar setting
        toolbar = (Toolbar)findViewById(R.id.toolbar_addfriend);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sectionAdapter = new SectionedRecyclerViewAdapter();

        userContactsArrayList = new ArrayList<UserContact>();
        userArrayList = new ArrayList<User>();
        accessContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search){
            Toast.makeText(getApplicationContext(), "search icon clicked", Toast.LENGTH_SHORT).show();

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // 연락처 접근권환 확인받기
    private void accessContacts(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            getContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                } else {
                    Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    // 연락처에서 목록 가져오기
    private void getContacts(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME,
                                                    ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        while (cursor.moveToNext()){
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            String phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            UserContact userContact = new UserContact(contactName, phoneNum);
            userContactsArrayList.add(userContact);
        }

        cursor.close();

        getUserListFromFirebaseDB();
    }

    // firebase에서 모든 사용자 목록 가져오기
    private void getUserListFromFirebaseDB(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userArrayList.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    user.setUid(userSnapshot.getKey());
                    userArrayList.add(user);
                }
                matchContactsAndUser();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    // match user_phone number in contacts and database
    // save user_name from contacts and user_email from database
    private void matchContactsAndUser(){
        UserMyFriendDBHelper userMyFriendDBHelper = new UserMyFriendDBHelper(this);
        ArrayList<User> userMyFriendArrayList = userMyFriendDBHelper.getAllMyFriend(); //내부DB 내 친구목록

        // user_name from contacts, user_email from user info in database
        for (int i=0; i<userContactsArrayList.size(); i++){ //연락처 목록
            for (int j=0; j<userArrayList.size(); j++){ //전체 사용자
                // 연락처의 전화번호와 전체 사용자의 전화번호가 같은 사용자만 userContactFriendsArrayList에 저장
                if((userContactsArrayList.get(i).getContact_phonenum()).equals(userArrayList.get(j).getTel())){
                    String user_name = userContactsArrayList.get(i).getContact_name();
                    String user_email = userArrayList.get(j).getEmail();

                    UserContactFriend matchedUser = new UserContactFriend(user_name, user_email, 0);

                    int chk =0;
                    for(int k=0; k<userMyFriendArrayList.size(); k++){
                        if(user_email.equals(userMyFriendArrayList.get(k).getEmail())){
                            chk =1;
                        }
                    }

                    switch (chk){
                        case 0:
                            userFriendAccordingToType.add(matchedUser);
                            break;
                        case 1:
                            matchedUser.setFriend_status(1);
                            userFriendAccordingToType.add(matchedUser);
                            break;
                    }
                }
            }
        } //end for

        compareAndDivideFriendType(userFriendAccordingToType); //이미 친구인 사람과 친구 일 수 있는 사람 나누는 함수
    }

    private void compareAndDivideFriendType(ArrayList<UserContactFriend> userFriendAccordingToType){
        for (int j=0; j<userFriendAccordingToType.size(); j++){
            if(userFriendAccordingToType.get(j).getFriend_status()==1){
                alreadyFriendArrayList.add(userFriendAccordingToType.get(j));
            } else {
                notFriendYetArrayList.add(userFriendAccordingToType.get(j));
            }
        }

        sectionAdapter.addSection(new AddFriendSection("이미 친구인 사람", alreadyFriendArrayList, 1));
        sectionAdapter.addSection(new AddFriendSection("친구 일 수 있는 사람", notFriendYetArrayList, 0));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.contact_friend_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sectionAdapter);
    }

    class AddFriendSection extends StatelessSection {

        String title;
        ArrayList<UserContactFriend> userContactFriendArrayList;
        int status;

        public AddFriendSection(String title, ArrayList userContactFriendArrayList, int status){
            super(R.layout.activity_add_friend_header, R.layout.activity_add_friend_item);

            this.title = title;
            this.userContactFriendArrayList = userContactFriendArrayList;
            this.status = status;
        }

        @Override
        public int getContentItemsTotal() {
            return userContactFriendArrayList.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final UserMyFriendDBHelper userMyFriendDBHelper = new UserMyFriendDBHelper(getApplicationContext());

            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            String name = userContactFriendArrayList.get(position).getFriend_name();
            final String email = userContactFriendArrayList.get(position).getFriend_email();

            itemHolder.contact_name.setText(name);
            itemHolder.contact_email.setText(email);

            if(status==1){
                itemHolder.addfriend_btn.setText("친구");
                itemHolder.addfriend_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                itemHolder.addfriend_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(AddFriendActivity.this)
                                .setMessage("친구를 끊으시겠습니까?")
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        userMyFriendDBHelper.deleteMyFriend(email);
                                        deleteFriendFromFirebaseDB(email);

                                        Toast.makeText(getApplicationContext(), "친구 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    }
                                }).show();
                    }
                });
            } else {
                itemHolder.addfriend_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User userMyFriend = new User();
                        for (int i=0; i<userArrayList.size(); i++){
                            if(email.equals(userArrayList.get(i).getEmail())){
                                userMyFriend = userArrayList.get(i);
                            }
                        }
                        userMyFriendDBHelper.insertMyFriend(userMyFriend); //내부DB에 친구 추가하기
                        addFriendToFirebaseDB(userMyFriend); //Firebase DB에 친구 추가하기

                        Toast.makeText(getApplicationContext(), "친구 추가되었습니다.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), String.format("Clicked on position #%s of Section %s", sectionAdapter.getPositionInSection(itemHolder.getAdapterPosition()), title), Toast.LENGTH_SHORT).show();

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.tvTitle.setText(title);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvTitle;

        public HeaderViewHolder(View view){
            super(view);
            tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private final View rootView;
        private final TextView contact_name;
        private final TextView contact_email;
        private final Button addfriend_btn;

        public ItemViewHolder(View view){
            super(view);

            rootView = view;
            contact_name = (TextView)view.findViewById(R.id.contact_name);
            contact_email = (TextView)view.findViewById(R.id.contact_email);
            addfriend_btn = (Button)view.findViewById(R.id.addfriend_btn);
        }
    }

    private void addFriendToFirebaseDB(User userMyFriend){
        String userFriendUid = userMyFriend.getUid();
        Map<String, String> userFriendValues = userMyFriend.toMap();
        Map<String, Object> updateChildrenMap = new HashMap<>();
        updateChildrenMap.put(userFriendUid, userFriendValues);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("friends").child(uid).updateChildren(updateChildrenMap);
    }

    private void deleteFriendFromFirebaseDB(String email){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query deleteQuery = databaseReference.child("friends").child(user.getUid()).orderByChild("email").equalTo(email);

        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot deleteSnapshot : dataSnapshot.getChildren()){
                    deleteSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
}
