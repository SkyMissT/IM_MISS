package com.miss.meet.ui.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.miss.meet.R;
import com.miss.meet.model.http.ApiService;
import com.miss.meet.model.http.RetrofitHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Policy;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;

/**
 * Created by linmu on 2017/6/13.
 */

public class RetrofitActivity extends AppCompatActivity {

    Button btn;
    ImageView image;
    private Uri imageUri;

    Button btn2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_retrofit);

        btn = (Button) findViewById(R.id.btn);
        image = (ImageView) findViewById(R.id.image);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        btn2 = (Button) findViewById(R.id.photo);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

    }


    private void upload(){

        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
        + getResources().getResourcePackageName(R.drawable.one) + "/"
        + getResources().getResourceTypeName(R.drawable.one) + "/"
        + getResources().getResourceEntryName(R.drawable.one) );


        image.setImageURI(uri);

        String path = getRealFilePath(this,uri);
        Log.e("aa", "path:" + path);
        File file = new File(path);



        String user = "aa";

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", user);
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("imgfile",file.getName(),body);

        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitHelper.uploadPicture(parts).enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                Log.e("aa", "succeed");
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
                Log.e("aa", "fail");
                Log.e("aa", t.toString());
            }
        });
    }


    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private void takePhoto(){
        File outputImage = new File(getExternalCacheDir(), "two.jpg");
        try {
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(RetrofitActivity.this, "com.miss.meet.fileprovider", outputImage);
        }else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                   try {
                       Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                       image.setImageBitmap(bitmap);
                   }catch (FileNotFoundException e){
                       e.printStackTrace();
                   }
                }
                break;
        }
    }
}
