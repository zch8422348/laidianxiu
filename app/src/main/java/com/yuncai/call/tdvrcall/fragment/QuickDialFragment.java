package com.yuncai.call.tdvrcall.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuncai.call.tdvrcall.R;
import com.yuncai.call.tdvrcall.adapter.RecyclerViewQuickDialAdapter;
import com.yuncai.call.tdvrcall.bean.QuickContact;
import com.yuncai.call.tdvrcall.db.QuickDialLab;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuickDialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuickDialFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mGroupId;
    private String mParam2;
    private RecyclerView rvQuickDial;
    private QuickDialLab dbTool;
    private RecyclerViewQuickDialAdapter recyclerViewQuickDialAdapter;


    public QuickDialFragment() {
        // Required empty public constructor
    }


    public static QuickDialFragment newInstance(String param1, String param2) {
        QuickDialFragment fragment = new QuickDialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGroupId = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_dial, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        ArrayList<QuickContact> quickContacts = new ArrayList<>();
        rvQuickDial.setLayoutManager(new GridLayoutManager(rvQuickDial.getContext(),5));
        recyclerViewQuickDialAdapter = new RecyclerViewQuickDialAdapter(getActivity(), mGroupId,quickContacts);
        rvQuickDial.setAdapter(recyclerViewQuickDialAdapter);
        loadData(mGroupId);
    }

    private void loadData(final String mGroupId) {
        dbTool = new QuickDialLab(getContext());
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final List<QuickContact> quickContacts = dbTool.queryAllQuickContact(mGroupId);
                for (QuickContact q :
                        quickContacts) {
                    Log.e("===", "====" + q);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewQuickDialAdapter.addData(quickContacts);
                    }
                });
            }
        });
    }

    private void initView(View view) {
        rvQuickDial = view.findViewById(R.id.rv_quick_dial);
    }
}
