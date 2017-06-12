package com.example.pipin.Filmgogo.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.activity.SignInActivity;
import com.example.pipin.Filmgogo.activity.movieDescription;
import com.example.pipin.Filmgogo.adapter.BuyButtonListener;
import com.example.pipin.Filmgogo.adapter.VoteMovieAdapter;
import com.example.pipin.Filmgogo.util.RequestData;

import static com.example.pipin.Filmgogo.activity.MainActivity.accountState;

/**
 * Created by Pipin on 2017/6/6.
 */
public class VoteFragment extends BaseFragment implements BuyButtonListener{
    private ListView voteMovieList;
    private TextView tv_empty;
    private RequestData app;
    private TextView appbar_title;
    private RelativeLayout tab_LinearLayout;
    private VoteMovieAdapter mAdapter;
    private android.os.Handler handler;

    public static VoteFragment newInstance(String from){
        VoteFragment fragment = new VoteFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from",from);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vote_layout,null);
        voteMovieList = (ListView) view.findViewById(R.id.vote_movie_list);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        appbar_title = (TextView) view.findViewById(R.id.appbar_title);
        tab_LinearLayout = (RelativeLayout) view.findViewById(R.id.tab_LinearLayout);


        app = (RequestData) getActivity().getApplication();
        //更新页面数据
        app.setListItemVoteOldMovie();
        handler = new android.os.Handler();
        handler.postDelayed(selectButtonRunnable, 150);
        mAdapter = new VoteMovieAdapter(getActivity().getLayoutInflater(), app.getListItemVoteOldMovie());
        mAdapter.setBuyButtonClickListener((BuyButtonListener) this);

        appbar_title.setText("发现");
        appbar_title.setVisibility(view.VISIBLE);
        tab_LinearLayout.setVisibility(view.GONE);

        return view;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setListener() {
        voteMovieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                onItemAndButtonClick(position);
            }
        });
    }

    private void onItemAndButtonClick(int position) {
        Intent intent = new Intent(getActivity(), movieDescription.class);
        SharedPreferences.Editor editor = accountState.edit();
        editor.putInt("movieId", Integer.parseInt(app.getListItemVoteOldMovie().get(position).get("id").toString()));
        editor.putInt("nowOrOld", 0);
        editor.commit();
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private Runnable selectButtonRunnable = new Runnable() {
        @Override
        public void run() {
            if (app.getListItemVoteOldMovie().size() == 0) {
                voteMovieList.setVisibility(View.INVISIBLE);
                tv_empty.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 150);
                return;
            }
            int i = 0;
            for (; i < app.getListItemVoteOldMovie().size(); )
                if (app.getListItemVoteOldMovie().get(i).get("icon") instanceof Bitmap) i++;
                else break;
            if (i < app.getListItemVoteOldMovie().size()) {
                voteMovieList.setVisibility(View.INVISIBLE);
                tv_empty.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 150);
            }
            else {
                voteMovieList.setAdapter(mAdapter);
                voteMovieList.setVisibility(View.VISIBLE);
                tv_empty.setVisibility(View.INVISIBLE);
            }
        }
    };

    private Runnable selectButtonRunnable2 = new Runnable() {
        @Override
        public void run() {
            if (app.getListItemVoteOldMovie().size() == 0) {
//                voteMovieList.setVisibility(View.INVISIBLE);
//                tv_empty.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 150);
                return;
            }
            int i = 0;
            for (; i < app.getListItemVoteOldMovie().size(); )
                if (app.getListItemVoteOldMovie().get(i).get("icon") instanceof Bitmap) i++;
                else break;
            if (i < app.getListItemVoteOldMovie().size()) {
//                voteMovieList.setVisibility(View.INVISIBLE);
//                tv_empty.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 150);
            }
            else {
                voteMovieList.setAdapter(mAdapter);
//                voteMovieList.setVisibility(View.VISIBLE);
//                tv_empty.setVisibility(View.INVISIBLE);
            }
        }
    };
    private int position = 0;

    @Override
    public void BuyButtonClick(final int position) {
//        onItemAndButtonClick(position);

        if (accountState.getInt("state", 0) == 1) {
            int vote = Integer.parseInt(app.getListItemVoteOldMovie().get(position).get("votes").toString());

            app.setVoteMovie(Integer.parseInt(app.getListItemVoteOldMovie().get(position).get("id").toString()), app.getUserId());
//            app.setListItemVoteOldMovie();
            Toast.makeText(getActivity(), "投票成功", Toast.LENGTH_SHORT).show();
            app.getListItemVoteOldMovie().get(position).put("votes", vote+1+"");
            mAdapter.notifyDataSetChanged();
//            handler.postDelayed(selectButtonRunnable2, 150);
//            mAdapter = new VoteMovieAdapter(getActivity().getLayoutInflater(), app.getListItemVoteOldMovie());
//            mAdapter.setBuyButtonClickListener((BuyButtonListener) this);

        } else {
            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            startActivity(intent);
        }

//更新ListView
//        app.getListItemVoteOldMovie().get(position).put("votes", Integer.parseInt(app.getListItemVoteOldMovie().get(position).get("votes").toString())+1+"");
//        mAdapter = new VoteMovieAdapter(getActivity().getLayoutInflater(), app.getListItemVoteOldMovie());
//        mAdapter.notifyDataSetChanged();//通知Adapter数据改变
    }

}
