package com.project.eelhea.vaportalk_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.eelhea.vaportalk_android.DTO.User;
import com.project.eelhea.vaportalk_android.helper.UserMyFriendDBHelper;
import com.project.eelhea.vaportalk_android.utils.DialogFriendClick;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by eelhea on 2017. 4. 5..
 */

public class FragFriendListTab extends Fragment {

    private static final String TAG = "FragFriendListTab";

    private SectionedRecyclerViewAdapter sectionAdapter;

    public static DialogFriendClick dialogFriendClick;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.frag_friendlist_view, container, false);

        sectionAdapter = new SectionedRecyclerViewAdapter();
        getMyProfileFromSharedPreference();
        getMyFriendListFromAndroidDB();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.friendlist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);

        return view;
    }

    // for "내 프로필" section
    // sharedPreference에서 내 정보 가져오기
    private void getMyProfileFromSharedPreference(){
        Log.e(TAG, "getMyProfileFromSharedPreference");
        ArrayList<User> myProfile = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("vaporTalk", MODE_PRIVATE);
        User userMyProfile = new User(sharedPreferences.getString("userUid", ""),
                                        sharedPreferences.getString("userEmail",""),
                                        sharedPreferences.getString("userName",""),
                                        sharedPreferences.getString("userSex",""),
                                        sharedPreferences.getString("userBirthday",""),
                                        sharedPreferences.getString("userTel",""),
                                        sharedPreferences.getString("userIsCommAgree",""),
                                        sharedPreferences.getString("userIsNearAgree",""),
                                        sharedPreferences.getString("userProfileImg",""));
        myProfile.add(0, userMyProfile);

        sectionAdapter.addSection(new MyFriendSection("내 프로필", myProfile));
    }

    // for "내 친구" section
    // 내부DB에서 친구목록 가져와서 보여주기
    private void getMyFriendListFromAndroidDB(){
        UserMyFriendDBHelper userMyFriendDBHelper = new UserMyFriendDBHelper(getContext());;
        ArrayList<User> userMyFriendArrayList = new ArrayList<>();
        userMyFriendArrayList.clear();
        userMyFriendArrayList = userMyFriendDBHelper.getAllMyFriend();

        sectionAdapter.addSection(new MyFriendSection("내 친구", userMyFriendArrayList));
    }


    // section 나누기 위한 class
    class MyFriendSection extends StatelessSection{

        String title;
        ArrayList<User> userMyFriendArrayList;

        public MyFriendSection(String title, ArrayList<User> userMyFriends){
            super(R.layout.frag_friendlist_header, R.layout.frag_friendlist_item);
            this.title = title;
            this.userMyFriendArrayList = userMyFriends;
        }

        @Override
        public int getContentItemsTotal() {
            return userMyFriendArrayList.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            final String profile = userMyFriendArrayList.get(position).getProfileImage();
            final String name = userMyFriendArrayList.get(position).getName();
            final String email = userMyFriendArrayList.get(position).getEmail();

            itemHolder.friend_name.setText(name);
            Glide.with(getContext()).load(profile).placeholder(R.mipmap.ic_default_profile).into(itemHolder.friend_profile);

            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogFriendClick = new DialogFriendClick(getActivity(), profile, name, email, close_click_listener, send_click_listener);
                    dialogFriendClick.show();
                }

                //다이얼로그 닫기 클릭
                Button.OnClickListener close_click_listener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialogFriendClick.dismiss();
                    }
                };

                //베이퍼 보내기 버튼 클릭
                Button.OnClickListener send_click_listener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), CameraStartActivity.class);
                        intent.putExtra("receiverEmail", email);
                        startActivity(intent);
                    }
                };
            });
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

    //section의 header holder class
    class HeaderViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvTitle;

        public HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    //section별 item holder class
    class ItemViewHolder extends RecyclerView.ViewHolder{
        private final View rootView;
        private final ImageView friend_profile;
        private final TextView friend_name;

        public ItemViewHolder(View view) {
            super(view);

            rootView = view;
            friend_profile = (ImageView) view.findViewById(R.id.friend_profile);
            friend_name = (TextView) view.findViewById(R.id.friend_name);
        }
    }
}
