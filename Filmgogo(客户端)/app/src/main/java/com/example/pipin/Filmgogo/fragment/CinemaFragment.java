package com.example.pipin.Filmgogo.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.activity.MovieOfCinema;
import com.example.pipin.Filmgogo.adapter.CinemaAdapter;
import com.example.pipin.Filmgogo.util.RequestData;

import static com.example.pipin.Filmgogo.activity.MainActivity.accountState;

/**
 * Created by Pipin on 2017/6/6.
 */
public class CinemaFragment extends BaseFragment {
    private ListView CinemaList;
    private TextView tv_empty;
    private TextView appbar_title;
    private CinemaAdapter mAdapter;
    private RelativeLayout tab_LinearLayout;
    private RequestData app;

    public static CinemaFragment newInstance(String from){
        CinemaFragment fragment = new CinemaFragment();
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
        View view = inflater.inflate(R.layout.fragment_cinema_layout,null);
        CinemaList = (ListView) view.findViewById(R.id.cinema_list);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        appbar_title = (TextView) view.findViewById(R.id.appbar_title);
        tab_LinearLayout = (RelativeLayout) view.findViewById(R.id.tab_LinearLayout);

        app = (RequestData) getActivity().getApplication();

        mAdapter = new CinemaAdapter(getActivity().getLayoutInflater(), app.getListItemCinema());
        CinemaList.setAdapter(mAdapter);

        if (mAdapter.getCount() == 0) {
            tv_empty.setVisibility(view.VISIBLE);
        } else {
            tv_empty.setVisibility(view.GONE);
        }

        appbar_title.setText("影院");
        appbar_title.setVisibility(view.VISIBLE);
        tab_LinearLayout.setVisibility(view.GONE);

        return view;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setListener() {
        CinemaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MovieOfCinema.class);
                SharedPreferences.Editor editor = accountState.edit();
                editor.putInt("cinemaId", Integer.parseInt(app.getListItemCinema().get(i).get("id").toString()));
                editor.putInt("nowOrOld", 1);
                editor.commit();
                intent.putExtra("name", app.getListItemCinema().get(i).get("name").toString());//key是取数据时需要用到的键，相当于一把钥匙
                startActivity(intent);
            }
        });
    }
}
