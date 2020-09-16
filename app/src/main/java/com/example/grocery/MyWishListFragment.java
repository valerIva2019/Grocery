package com.example.grocery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class MyWishListFragment extends Fragment {

    private RecyclerView wishlistRecyclerView;
    public MyWishListFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_wish_list, container, false);
         wishlistRecyclerView = view.findViewById(R.id.my_wishlist_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);

        List<WishListModel> wishListModelList = new ArrayList<>();


        WishListAdapter wishListAdapter = new WishListAdapter(wishListModelList,true);
        wishlistRecyclerView.setAdapter(wishListAdapter);
        wishListAdapter.notifyDataSetChanged();

       return view;
    }
}