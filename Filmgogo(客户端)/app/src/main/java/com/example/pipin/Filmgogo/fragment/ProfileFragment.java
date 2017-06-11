package com.example.pipin.Filmgogo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.activity.AccountReservation;
import com.example.pipin.Filmgogo.activity.LikeMovie;
import com.example.pipin.Filmgogo.activity.SignInActivity;

import static com.example.pipin.Filmgogo.activity.MainActivity.accountState;

/**
 * Created by Pipin on 2017/6/6.
 */
public class ProfileFragment extends BaseFragment {
    private LocalBroadcastManager broadcastManager;
   private IntentFilter intentFilter;
   private BroadcastReceiver mReceiver;


    private TextView user_name;
    private Button bt_signin;
    private LinearLayout order_to_pay, all_order, order_payed, like_movie, seen_movie;

    public static ProfileFragment newInstance(String from){
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from",from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction("fresh");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                if (intent.getAction().equals("fresh")) {
                    init();
                }
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }


    @Override
    protected void init() {
        if (accountState.getInt("state", 0) == 0) {
            user_name.setText("立即登录");
            bt_signin.setVisibility(View.GONE);
        } else {
            user_name.setText(accountState.getString("userName",""));
            bt_signin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setListener() {
        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountState.getInt("state", 0) == 0) {
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);
                }
            }
        });

        bt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = accountState.edit();
                editor.putInt("state", 0);
                editor.commit();
                init();
            }
        });
        order_to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountState.getInt("state", 0) == 0) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), AccountReservation.class);
                    intent.putExtra("tag", 2);
                    startActivity(intent);
                }
            }
        });
        all_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountState.getInt("state", 0) == 0) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), AccountReservation.class);
                    intent.putExtra("tag", 1);
                    startActivity(intent);
                }
            }
        });
        order_payed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountState.getInt("state", 0) == 0) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), AccountReservation.class);
                    intent.putExtra("tag", 3);
                    startActivity(intent);
                }
            }
        });
        like_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountState.getInt("state", 0) == 0) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LikeMovie.class);
                    startActivity(intent);
                }
            }
        });
        seen_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "还没看过电影哦，赶紧去逛逛吧~", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_layout, null);
        user_name = (TextView) view.findViewById(R.id.user_name);
        bt_signin = (Button) view.findViewById(R.id.bt_signin);
        order_to_pay = (LinearLayout) view.findViewById(R.id.order_to_pay);
        order_payed = (LinearLayout) view.findViewById(R.id.order_payed);
        all_order = (LinearLayout) view.findViewById(R.id.all_order);
        like_movie = (LinearLayout) view.findViewById(R.id.like_movie);
        seen_movie = (LinearLayout) view.findViewById(R.id.seen_movie);



        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);

    }

}
