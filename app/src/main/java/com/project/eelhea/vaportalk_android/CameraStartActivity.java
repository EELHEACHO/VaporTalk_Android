package com.project.eelhea.vaportalk_android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.project.eelhea.vaportalk_android.view.MainActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by eelhea on 2017. 5. 2..
 */

public class CameraStartActivity extends AppCompatActivity {

    private static final String TAG = "CameraStartActivity";

    //for camera action
    String receiverEmail;
    static final int REQUEST_TAKE_PICTURE = 1;
    String picturePath;
    Uri pictureUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isExistCameraApplication()) {
            //카메라를 열어 촬영모드로 Intent를 보낸다
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(takePictureIntent.resolveActivity(getPackageManager())!=null){

                Intent friendList = getIntent();
                receiverEmail = friendList.getExtras().getString("receiverEmail");

                //getFileUri 함수를 통해서 사진 파일을 저장하고 임시 엑세스 권한을 부여해주는 content:// URI를 반환받는다
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUri());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
            }
        }
    }

    //카메라 어플리케이션 유무 확인
    private boolean isExistCameraApplication(){
        PackageManager packageManager = this.getPackageManager();
        Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List cameraApps = packageManager.queryIntentActivities(cameraApp, PackageManager.MATCH_DEFAULT_ONLY);

        return  cameraApps.size() > 0;
    }

    private Uri getFileUri(){
        //provider_paths.xml 에 명시된 images/ 경로에 파일 폴더를 가져온다
        File dir = new File(getFilesDir(), "images");
        //존재하지 않는다면 폴더를 생성하도록 한다
        if(!dir.exists()){
            dir.mkdirs();
        }
        //Unique한 파일명으로 저장하기 위하며 날짜 형태로 저장한다
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "IMG_"+timestamp + ".png";
        //위의 폴더에 해당 파일을 저장한다
        File file = new File(dir, fileName);
        //다른 activity로 넘겨주기위해 파일명의 absolute path를 받아온다
        picturePath = file.getAbsolutePath();

        //AndroidManifest.xml 파일에 명시한 provider의 meta-data를 참조하여 FileProvider의 getUriFroFile 함수를 활용하여 URI를 반환한다
        return FileProvider.getUriForFile(this, getApplicationContext().getPackageName()+".provider", file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            Log.e(TAG, "onActivityResult: RESULT_NOT_OK");
        } else {
            switch (requestCode) {
                case REQUEST_TAKE_PICTURE :
                    //Intent에 촬영된 사진의 경로를 extra에 실어서 다른 CameraResultActivity 로 보낸다
                    Intent cameraResultIntent = new Intent(getApplicationContext(), CameraResultActivity.class);
                    cameraResultIntent.putExtra("receiverEmail", receiverEmail);
                    cameraResultIntent.putExtra("picturePath", picturePath);
                    startActivity(cameraResultIntent);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}
