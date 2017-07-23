package com.miss.meet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miss.meet.App.ExitAPPUtils;
import com.miss.meet.R;
import com.miss.meet.util.NativePreference;
import com.miss.meet.widget.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2017/5/21.
 */

public class StartActivity extends AppCompatActivity {

    // TODO : 只有第一次打开显示此界面，后续不再显示

    private TextView btn_register;
    private ViewPager viewPager;
    private CardView cardView;
    private TextView tv_one;
    private TextView tv_two;

    //  data
    private List<View> mViews;
    private int[] mImageIds;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ExitAPPUtils.getInstance().addActivity(this);

        intent = new Intent(StartActivity.this,LoginActivity.class);

        initData();

        btn_register = (TextView) findViewById(R.id.register);
        viewPager = (ViewPager) findViewById(R.id.register_vp);
        cardView = (CardView) findViewById(R.id.cd);
        tv_one = (TextView) findViewById(R.id.text_one);
        tv_two = (TextView) findViewById(R.id.text_two);
        cardView.setVisibility(View.GONE);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                finish();
            }
        });

        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                container.addView(mViews.get(position));
                return mViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViews.get(position));

            }

            @Override
            public int getCount() {
                return mImageIds.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0){
                    tv_one.setVisibility(View.VISIBLE);
                }else {
                    tv_one.setVisibility(View.GONE);
                }
                if (position == 1){
                    tv_two.setVisibility(View.VISIBLE);
                }else {
                    tv_two.setVisibility(View.GONE);
                }
                //  第三张图片的按键
                if (position == mImageIds.length-1){
                    cardView.setVisibility(View.VISIBLE);
                }else {
                    cardView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initData(){
        mImageIds = new int[]{R.mipmap.start_one,R.mipmap.start_two,R.mipmap.start_three};
        mViews = new ArrayList<>();
        for (int imageId : mImageIds){
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imageId);
            mViews.add(imageView);
        }
    }


}


