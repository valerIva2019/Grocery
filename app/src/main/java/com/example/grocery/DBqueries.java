package com.example.grocery;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ThrowOnExtraProperties;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.grocery.HomeFragment.swipeRefreshLayout;
import static com.example.grocery.ProductDetailsActivity.addToWishListBtn;
import static com.example.grocery.ProductDetailsActivity.initialRating;
import static com.example.grocery.ProductDetailsActivity.productID;
import static com.example.grocery.ProductDetailsActivity.setRating;

public class DBqueries {

  //  public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
  //  public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();
   public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<>();
    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public  static List <String> loadedCategoriesnames = new ArrayList<>();
    public static List<String> wishList = new ArrayList<>();
    public static List<WishListModel> wishListModelList = new ArrayList<>();

    public static  List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context){
           categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){


                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(),documentSnapshot.get("categoryName").toString()));
                    }

                    CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                    categoryRecyclerView.setAdapter(categoryAdapter);

                    categoryAdapter.notifyDataSetChanged();
                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void loadFragmentData(final RecyclerView homePageRecyclerView,  final Context context, final int index, String categoryName){

        firebaseFirestore.collection("CATEGORIES").document(categoryName.toUpperCase()).collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                if ((long)documentSnapshot.get("view_type")==0){
                                    List<SliderModel> sliderModelList  = new ArrayList<>();
                                    long no_of_banners = (long)documentSnapshot.get("no_of_banners");

                                    for (long  x=1;x < no_of_banners+1;x++){

                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_"+x).toString(),documentSnapshot.get("background"+x).toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(0,sliderModelList));
                                    Toast.makeText(context,"Check",Toast.LENGTH_LONG).show();



                                }else if ((long)documentSnapshot.get("view_type")==1){
                                    lists.get(index).add(new HomePageModel(1,documentSnapshot.get("strip_ad_banner").toString(),documentSnapshot.get("background").toString()));



                                }else  if ((long)documentSnapshot.get("view_type")==2){
                                    List<WishListModel> viewAllProductList=new ArrayList<>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                    long no_of_products = (long)documentSnapshot.get("no_of_products");

                                    for (long  x=1;x < no_of_products+1;x++){

                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_id_"+x).toString()
                                                ,documentSnapshot.get("product_image_"+x).toString()
                                                ,documentSnapshot.get("product_title_"+x).toString()
                                                ,documentSnapshot.get("product_subtitle_"+x).toString()
                                                ,documentSnapshot.get("product_price_"+x).toString()));

                                        viewAllProductList.add(new WishListModel(documentSnapshot.get("product_id_"+x).toString()
                                                ,documentSnapshot.get("product_image_"+x).toString()
                                        ,documentSnapshot.get("product_full_title_"+x).toString()
                                                ,(long)documentSnapshot.get("free_coupens_"+x)
                                                ,documentSnapshot.get("average_rating_"+x).toString()
                                                ,(long)documentSnapshot.get("total_ratings_"+x)
                                                ,documentSnapshot.get("product_price_"+x).toString()
                                                ,documentSnapshot.get("cutted_price_"+x).toString()
                                                ,(boolean)documentSnapshot.get("COD_"+x) ));


                                    }
                                    lists.get(index).add(new HomePageModel(2,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),horizontalProductScrollModelList,viewAllProductList));

                                }else if ((long)documentSnapshot.get("view_type")==3){
                                    List<HorizontalProductScrollModel> gridLayoutModelList = new ArrayList<>();
                                    long no_of_products = (long)documentSnapshot.get("no_of_products");

                                    for (long  x=1;x < no_of_products+1;x++){
                                        gridLayoutModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_id_"+x).toString()
                                                ,documentSnapshot.get("product_image_"+x).toString()
                                                ,documentSnapshot.get("product_title_"+x).toString()
                                                ,documentSnapshot.get("product_subtitle_"+x).toString()
                                                ,documentSnapshot.get("product_price_"+x).toString()));

                                    }
                                    lists.get(index).add(new HomePageModel(3,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),gridLayoutModelList));



                                }


                            }

                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                            homePageRecyclerView.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }


    public  static  void loadWishList(final Context context, final Dialog dialog,final boolean loadProductData){
         wishList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    for (long x =0;x< (long)task.getResult().get("list_size");x++) {
                        wishList.add(task.getResult().get("product_id_" + x).toString());

                        if (DBqueries.wishList.contains(productID)){
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;
                           if (addToWishListBtn != null){
                                addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
                                // Toast.makeText(, "Product added to WishList", Toast.LENGTH_SHORT).show();

                            }  }else {
                            addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                        }
                        if (loadProductData){
                            wishListModelList.clear();
                              final String productId = task.getResult().get("product_id_"+x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()) {
                                        wishListModelList.add(new WishListModel(productId,task.getResult().get("product_image_1").toString()
                                                , task.getResult().get("product_title").toString()
                                                , (long) task.getResult().get("free_coupens")
                                                , task.getResult().get("average_rating").toString()
                                                , (long) task.getResult().get("total_ratings")
                                                , task.getResult().get("product_price").toString()
                                                , task.getResult().get("cutted_price").toString()
                                                , (boolean) task.getResult().get("COD")));

                                                 MyWishListFragment.wishListAdapter.notifyDataSetChanged();


                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show();

                                    }

                                }
                            });

                    }


                    }

                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_LONG).show();

                }
                dialog.dismiss();
            }
        });
    }


    public  static  void removeFromWishList(final int index, final Context context){

        wishList.remove(index);
        Map<String,Object> updateWishList = new HashMap<>();
        for (int x =0;x<wishList.size();x++){
            updateWishList.put("product_id_"+x,wishList.get(x));
        }
        updateWishList.put("list_size",(long)wishList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST").set(updateWishList)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            if (wishListModelList.size() !=0){
                                wishListModelList.remove(index);
                                MyWishListFragment.wishListAdapter.notifyDataSetChanged();
                            }
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                            Toast.makeText(context,"Removed Successfully",Toast.LENGTH_LONG).show();

                        }else {
                             if (addToWishListBtn !=null) {
                                 addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                             }
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_LONG).show();
                        }
                      //  if (addToWishListBtn !=null) {

                      //      addToWishListBtn.setEnabled(true);
                     //   }
                        ProductDetailsActivity.running_wishlistquery= false;
                    }
                });
    }

  public static  void  loadRatingList(final Context context){

        if (!ProductDetailsActivity.running_ratingquery) {
            ProductDetailsActivity.running_ratingquery=true;

            myRatedIds.clear();
            myRating.clear();


            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {

                            myRatedIds.add(task.getResult().get("product_id_" + x).toString());
                            myRating.add((long) task.getResult().get("rating_" + x));

                            if (task.getResult().get("product_id_" + x).toString().equals(productID)) {
                                ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if (ProductDetailsActivity.rateNowContainer !=null) {
                                    ProductDetailsActivity.setRating(initialRating);
                                }
                            }


                        }

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                    ProductDetailsActivity.running_ratingquery=false;


                }
            });
        }

  }



    public  static void clearData(){
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesnames.clear();
        wishList.clear();
        wishListModelList.clear();
    }


}
