package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Adapters.CompleteRenterAdapter;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CompleteFrag extends Fragment {

    private OnCompleteInteractionListener mListener;
    List<RentalHeader> rentalHeaderList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Button mBtnCompleteRenter, mBtnCompleteOwner;


    public CompleteFrag() {
    }

    public static CompleteFrag newInstance(String param1, String param2) {
        CompleteFrag fragment = new CompleteFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete, container, false);

        mBtnCompleteRenter = (Button) view.findViewById(R.id.completeBtnRenter);
        mBtnCompleteOwner = (Button) view.findViewById(R.id.completeBtnOwner);


        rentalHeaderList = new ArrayList<RentalHeader>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_complete);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CompleteRenterAdapter(rentalHeaderList);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("inside", "Click Listenr");

        getCompleteRenter();


        mBtnCompleteRenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("btnRenterReceive", "Click Listenr");
                getCompleteRenter();
            }
        });

        mBtnCompleteOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("btnRenterReceive", "Click Listenr");
                getCompleteOwner();

            }
        });
        return view;
    }

    public void getCompleteOwner(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        String URL = Constants.GET_TRANSACTION_COMPLETE_OWNER+"/"+user.getUserId();
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        final RentalHeader rentalHeader =new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseRentalHeader", response);
//                RentalHeader rentalHeaderModel = gson.fromJson(response, RentalHeader.class);
                rentalHeaderList.clear();
                rentalHeaderList.addAll(Arrays.asList(gson.fromJson(response, RentalHeader[].class)));
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCompleteOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCompleteInteractionListener) {
            mListener = (OnCompleteInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnCompleteInteractionListener {
        void onCompleteOnClick(Uri uri);
    }

    public void getCompleteRenter(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        String URL = Constants.GET_TRANSACTION_COMPLETE_RENTER+user.getUserId();
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        final RentalHeader rentalHeader =new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseRentalHeader", response);
//                RentalHeader rentalHeaderModel = gson.fromJson(response, RentalHeader.class);
                rentalHeaderList.clear();
                rentalHeaderList.addAll(Arrays.asList(gson.fromJson(response, RentalHeader[].class)));
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }
}
