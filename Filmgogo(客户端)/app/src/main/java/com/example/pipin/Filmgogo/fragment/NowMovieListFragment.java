package com.example.pipin.Filmgogo.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.activity.movieDescription;
import com.example.pipin.Filmgogo.adapter.BuyButtonListener;
import com.example.pipin.Filmgogo.adapter.MovieAdapter;
import com.example.pipin.Filmgogo.util.RequestData;

import java.util.HashMap;

import static com.example.pipin.Filmgogo.activity.MainActivity.accountState;

/**
 * Created by Pipin on 2017/6/8.
 */

public class NowMovieListFragment extends BaseFragment implements BuyButtonListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private ListView NowMovieList;
    private TextView tv_empty;
    private RequestData app;
    private SliderLayout mDemoSlider;
    private MovieAdapter mAdapter;
    private android.os.Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_movie_list,null);
        NowMovieList = (ListView) view.findViewById(R.id.now_movie_list);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);


        app = (RequestData) getActivity().getApplication();
        handler = new android.os.Handler();
        handler.postDelayed(selectButtonRunnable, 150);
        mAdapter = new MovieAdapter(getActivity().getLayoutInflater(), app.getListItemMovie());
        mAdapter.setBuyButtonClickListener((BuyButtonListener) this);

        return view;
    }

    @Override
    protected void init() {
        initBanner();
    }

    private void initBanner() {
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("变形金刚5：最后的骑士", "https://img3.doubanio.com/view/photo/photo/public/p2461412322.webp");
        url_maps.put("异形：契约", "https://img3.doubanio.com/view/photo/photo/public/p2461835665.webp");
        url_maps.put("明月几时有", "https://img3.doubanio.com/view/photo/photo/public/p2459666561.webp");
        url_maps.put("悟空传", "https://img3.doubanio.com/view/photo/photo/public/p2462105212.webp");

//        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
//        file_maps.put("Hannibal",R.drawable.hannibal);
//        file_maps.put("Big Bang Theory",R.drawable.bigbang);
//        file_maps.put("House of Cards",R.drawable.house);
//        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }

    @Override
    protected void setListener() {
        NowMovieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemAndButtonClick(position);
            }
        });
    }

    private void onItemAndButtonClick(int position) {
        Intent intent = new Intent(getActivity(), movieDescription.class);
        SharedPreferences.Editor editor = accountState.edit();
        editor.putInt("movieId", Integer.parseInt(app.getListItemMovie().get(position).get("id").toString()));
        editor.putInt("nowOrOld", 1);
        editor.commit();
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private Runnable selectButtonRunnable = new Runnable() {
        @Override
        public void run() {
            if (app.getListItemMovie().size() == 0) {
                NowMovieList.setVisibility(View.INVISIBLE);
                tv_empty.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 150);
                return;
            }
            int i = 0;
            for (; i < app.getListItemMovie().size(); )
                if (app.getListItemMovie().get(i).get("icon") instanceof Bitmap) i++;
                else break;
            if (i < app.getListItemMovie().size()) {
                NowMovieList.setVisibility(View.INVISIBLE);
                tv_empty.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 150);
            }
            else {
                NowMovieList.setAdapter(mAdapter);
                NowMovieList.setVisibility(View.VISIBLE);
                tv_empty.setVisibility(View.INVISIBLE);
                setListViewHeightBasedOnChildren(NowMovieList);
            }
        }
    };

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = 300 * listView.getCount();
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    public void BuyButtonClick(final int position) {
        onItemAndButtonClick(position);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
