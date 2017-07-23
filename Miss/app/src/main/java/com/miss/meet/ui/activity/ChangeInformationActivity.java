package com.miss.meet.ui.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.miss.meet.R;
import com.miss.meet.constant.Constant;
import com.miss.meet.model.bean.Person;
import com.miss.meet.model.http.RetrofitHelper;
import com.miss.meet.model.socket.MinaEngine;
import com.miss.meet.model.socket.SocketInterface;
import com.miss.meet.util.ImageTools;
import com.miss.meet.util.NativePreference;
import com.miss.meet.widget.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;

/**
 * Created by linmu on 2017/6/14.
 */

public class ChangeInformationActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "ChangeInformation";

    RoundImageView btn_image;
    TextView text_nickname;
    EditText text_labels;
    EditText text_interest;
    EditText text_interestPerson;
    EditText text_address;
    TextView text_sex;
    TextView text_age;
    TextView text_signature;
    CardView btn_change;
    ImageView btn_back;

    String nickname;
    String labels;
    String interest;
    String interestPerson;
    String address;
    String sex;
    String age;
    String signature;
    String photo;


    private static final int CHOOSE_PHOTO = 2;
    Context context;
    //账号
    String account;
    Person person;
    //  次数
    int count;
    //  文件路径
    private String currentImagePath;
    SocketInterface socketInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinformation);

        Gson gson = new Gson();
        person = gson.fromJson(NativePreference.getInstance().read("gson"), Person.class);
        account = person.getAccount();
        count = NativePreference.getInstance().read("count",0);
        context = this;

        initView();

        socketInterface = new MinaEngine();
        socketInterface.init(this);
        socketInterface.start();
        socketInterface.setResultCall(new SocketInterface.ResultCall() {
            @Override
            public void result(String result) {
                try {
                    Log.e(TAG,"收到消息:"+result.toString());
                    JSONObject jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("type");
                    if (type.equals(Constant.RECEIVE_INFORMATION)){
                        text_nickname.setText(jsonObject.getString("nickname"));
                        text_labels.setText(jsonObject.getString("labels"));
                        text_interest.setText(jsonObject.getString("interest"));
                        text_interestPerson.setText(jsonObject.getString("interestPerson"));
                        text_address.setText(jsonObject.getString("address"));
                        text_sex.setText(jsonObject.getString("sex"));
                        text_age.setText(jsonObject.getInt("age"));
                        text_signature.setText(jsonObject.getString("signature"));
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL + "/img/" + jsonObject.getString("icaddress") + ".jpg",
                                btn_image, ImageTools.options, ImageTools.animateFirstListener);
                    }else if (type.equals(Constant.RECEIVE_UPDATAINFORMATION)){
                        String update = jsonObject.getString("updata");
                        if (update.equals("true")){
                            Toast.makeText(ChangeInformationActivity.this,"更改成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ChangeInformationActivity.this,"更改失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        queryInformation();

    }



    private void initView(){
        btn_back = (ImageView) findViewById(R.id.back);
        btn_change = (CardView) findViewById(R.id.change);
        btn_image = (RoundImageView) findViewById(R.id.image);
        btn_change.setOnClickListener(this);
        btn_image.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        String icPath = person.getIc();
        if (icPath.length() > 5){
            Bitmap bitmap = BitmapFactory.decodeFile(icPath);
            btn_image.setImageBitmap(bitmap);
        }else {
            ImageLoader.getInstance().displayImage(Constant.BASE_URL + "/img/" + icPath + ".jpg",
                    btn_image, ImageTools.options, ImageTools.animateFirstListener);
        }

        text_nickname = (TextView) findViewById(R.id.nickname);
        text_labels = (EditText) findViewById(R.id.labels);
        text_interest = (EditText) findViewById(R.id.interest);
        text_interestPerson = (EditText) findViewById(R.id.interestperson);
        text_address = (EditText) findViewById(R.id.address);
        text_sex = (TextView) findViewById(R.id.sex);
        text_age = (TextView) findViewById(R.id.age);
        text_signature = (TextView) findViewById(R.id.signature);

        text_nickname.setText(person.getNickname());
        text_labels.setText(person.getLabels());
        text_interest.setText(person.getInterest());
        text_interestPerson.setText(person.getInterestperson());
        text_address.setText(person.getAddress());
        text_sex.setText(person.getSex());
        text_age.setText(String.valueOf(person.getAge()));
        text_signature.setText(person.getSignature());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Gson gson = new Gson();
        person = gson.fromJson(NativePreference.getInstance().read("gson"), Person.class);
        String icPath = person.getIc();
        if (icPath.length() > 5){
            Bitmap bitmap = BitmapFactory.decodeFile(icPath);
            btn_image.setImageBitmap(bitmap);
        }else {
            ImageLoader.getInstance().displayImage(Constant.BASE_URL + "/img/" + icPath + ".jpg",
                    btn_image, ImageTools.options, ImageTools.animateFirstListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.change:
                nickname = text_nickname.getText().toString();
                labels = text_labels.getText().toString();
                interest = text_interest.getText().toString();
                interestPerson = text_interestPerson.getText().toString();
                address = text_address.getText().toString();
                sex = text_sex.getText().toString();
                age = text_age.getText().toString();
                signature = text_signature.getText().toString();
                if (TextUtils.isEmpty(nickname)||TextUtils.isEmpty(labels)||TextUtils.isEmpty(interest)||TextUtils.isEmpty(interestPerson)||TextUtils.isEmpty(address)||
                        TextUtils.isEmpty(sex)||TextUtils.isEmpty(age)||TextUtils.isEmpty(signature)){
                    Toast.makeText(ChangeInformationActivity.this,"不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    change();
                }
                break;
            case R.id.image:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ChangeInformationActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlum();
                }
                break;
        }
    }

    private void change(){

        JSONObject sendJson = new JSONObject();
        try {
            sendJson.put("type", Constant.TYPE_UPDATAINFORMATION);
            sendJson.put("nickname",nickname);
            sendJson.put("labels",labels);
            sendJson.put("interest",interest);
            sendJson.put("interestPerson",interestPerson);
            sendJson.put("address",address);
            sendJson.put("sex",sex);
            sendJson.put("age",age);
            sendJson.put("signature",signature);
            socketInterface.sendMessage(sendJson.toString());
            Log.e(TAG, "发送--更新");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        person.setNickname(nickname);
        person.setLabels(labels);
        person.setInterest(interest);
        person.setInterestperson(interestPerson);
        person.setAddress(address);
        person.setSex(sex);
        person.setAge(Integer.valueOf(age));
        person.setSignature(signature);
        Gson gson = new Gson();
        String aa = gson.toJson(person);
        NativePreference.getInstance().write("gson", aa);

        Toast.makeText(ChangeInformationActivity.this,"更改成功",Toast.LENGTH_SHORT).show();

    }

    private void queryInformation(){
        JSONObject queryJson = new JSONObject();
        try {
            queryJson.put("type",Constant.TYPE_INFORMATION);
            queryJson.put("account",account);
            socketInterface.sendMessage(queryJson.toString());
            Log.e(TAG,"申请详细信息:" + queryJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void openAlum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    if (Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlum();
                }else {
                    Toast.makeText(context,"aa",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    //    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        Log.e("aa", "-----");
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)) {
            Log.e("aa", "111");
            //如果是Document类型的uri
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                Log.e("aa", "222");
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.provider.downloads.documents".equals(uri.getAuthority())) {
                Log.e("aa", "333");
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("Content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            Log.e("aa", "444");
            imagePath = getImagePath(uri, null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            Log.e("aa", "555");
            imagePath = uri.getPath();
        }
        currentImagePath = imagePath;
        displayImage(imagePath);
        upload(currentImagePath);

    }


    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        Log.e("aa", "path:" + path);
        return  path;
    }

    private void displayImage(String imagePath){
        Log.e("aa", "imagePath:" + imagePath);
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            btn_image.setImageBitmap(bitmap);
            person.setIc(imagePath);
            Gson gson = new Gson();
            String aa = gson.toJson(person);
            NativePreference.getInstance().write("gson", aa);
        }else {
            Toast.makeText(context,"fail get photo",Toast.LENGTH_SHORT).show();
        }
    }


    private void upload(String path){

        File file = new File(path);
        Log.e("aa", "path:" + path);
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

}
