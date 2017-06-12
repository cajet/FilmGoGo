package com.example.pipin.Filmgogo.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.activity.SelectSeat;
import com.example.pipin.Filmgogo.util.RequestData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pipin.Filmgogo.activity.MainActivity.accountState;

/**
 * Created by Pipin on 2017/6/11.
 */

public class ShowTimeListFragment2 extends BaseFragment {
    private ListView mList;
    private TextView tv_empty;

    private List<HashMap<String, Object>> mDataList;
    private MySimpleAdapter mAdapter;

    private RequestData app;
    private android.os.Handler handler;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_old_movie_list,null);
        mList = (ListView) view.findViewById(R.id.old_movie_list);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);


        return view;
    }

    @Override
    protected void init() {
        mDataList = new ArrayList<HashMap<String, Object>>();
        app = (RequestData) getActivity().getApplication();

        mAdapter = new MySimpleAdapter(getActivity(), mDataList, R.layout.item_show_time,
                new String[]{"time", "price"}, new int[]{R.id.id_show_time
                , R.id.id_show_time_price});
        mList.setAdapter(mAdapter);
        handler = new android.os.Handler();
        handler.postDelayed(selectButtonRunnable, 150);
    }

    private Runnable selectButtonRunnable = new Runnable() {
        @Override
        public void run() {
            if (app.getListItemMovieShowTime().size() == 0) {
                    mList.setVisibility(View.INVISIBLE);
                    tv_empty.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 150);
                return;
            }
            mList.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.INVISIBLE);
            setListItem();
        }
    };

    void setListItem() {

        int j = 0;
        for (int i = 0; i <app.getListItemMovieShowTime().size(); ++i) {

            HashMap<String, Object> item3 = new HashMap<String, Object>();



            if (i == 0) {
                j = 1;

            } else if (!app.getListItemMovieShowTime().get(i).get("buttonTitle").equals(app.getListItemMovieShowTime().get(i - 1).get("buttonTitle"))) {
                if (j == 1) {
                    j = 2;
                } else if (j == 2) {
                    j = 3;
                }
            }

            if (j == 3) {
                item3.put("id",  app.getListItemMovieShowTime().get(i).get("id"));
                item3.put("time", app.getListItemMovieShowTime().get(i).get("time"));
                item3.put("price",  app.getListItemMovieShowTime().get(i).get("price").toString());
                mDataList.add(item3);
                mAdapter.notifyDataSetChanged();
            }

        }

    }

    @Override
    protected void setListener() {

    }

    private class MySimpleAdapter extends SimpleAdapter {
        public MySimpleAdapter(Context context,
                               List<? extends Map<String, ?>> data, int resource,
                               String[] from, int[] to) {
            super(context, data, resource, from, to);
            // TODO Auto-generated constructor stub
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            // TODO Auto-generated method stub
            View v= super.getView(position, convertView, parent);
            Button btn=(Button) v.findViewById(R.id.id_buy_ticket);
            final TextView time = (TextView) v.findViewById(R.id.id_show_time);
            btn.setTag(position);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stu

                    SharedPreferences.Editor editor = accountState.edit();
                    editor.putInt("showTimeId", Integer.parseInt(mDataList.get(position).get("id").toString()));
                    editor.commit();
                    Intent intent = new Intent(getActivity(), SelectSeat.class);
                    TextView cinemaName = (TextView) getActivity().findViewById(R.id.id_show_time_cinema_name);
                    TextView movieName = (TextView) getActivity().findViewById(R.id.id_show_time_movie_name);
                    TextView day = (TextView) getActivity().findViewById(R.id.id_one);
                    intent.putExtra("cinemaName", cinemaName.getText().toString());
                    intent.putExtra("movieName", movieName.getText().toString());
                    intent.putExtra("day", day.getText().toString());
                    intent.putExtra("time", time.getText().toString());
                    startActivity(intent);
                }
            });
            return v;
        }
    }
}
