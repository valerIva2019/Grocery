package com.example.grocery;

import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()){
            case 0:
                return HomePageModel.BANNER_SLIDER;
            case 1:
                return HomePageModel.STRIP_AD_BANNER;
            case 2:
                return HomePageModel.HORIZONTAL_ALL_PRODUCT;
            case 3:
                return HomePageModel.GRID_PRODUCT_VIEW;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       switch (viewType){
           case HomePageModel.BANNER_SLIDER:
           View bannerSliderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_add_layout,parent,false);
           return new BannerSliderViewHolder(bannerSliderView);
           case HomePageModel.STRIP_AD_BANNER:
               View stripAddView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_add_layout,parent,false);
               return new StripAddBannerholder (stripAddView);
           case HomePageModel.HORIZONTAL_ALL_PRODUCT:
               View horizontalProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout,parent,false);
               return new HorizontalProductViewholder(horizontalProductView);

           case HomePageModel.GRID_PRODUCT_VIEW:
               View gridProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout,parent,false);
               return new GridProductViewholder(gridProductView);

           default:
               return null;
       }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
  switch (homePageModelList.get(position).getType())
  {
      case HomePageModel.BANNER_SLIDER:
          List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
          ((BannerSliderViewHolder)holder).setBannerSliderViewPager(sliderModelList);
         break;
         case HomePageModel.STRIP_AD_BANNER:
             int resource = homePageModelList.get(position).getResource();
             String color  = homePageModelList.get(position).getBackgroundColor();
             ((StripAddBannerholder)holder).setStripAdd(resource,color);
             break;

             case HomePageModel.HORIZONTAL_ALL_PRODUCT:
              String horizontalLayoutTitle = homePageModelList.get(position).getTitle();
              List<HorizontalProductScrollModel> horizontalProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                 ((HorizontalProductViewholder)holder).setHorizontalProductLayout(horizontalProductScrollModelList,horizontalLayoutTitle);
                   break;
                 case HomePageModel.GRID_PRODUCT_VIEW:
                     String gridLayoutTitle = homePageModelList.get(position).getTitle();
                     List<HorizontalProductScrollModel> gridProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                  ((GridProductViewholder)holder).setGridProductLayout(gridProductScrollModelList,gridLayoutTitle);
            break;



      default:
          return;

  }

    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }


    public class BannerSliderViewHolder extends RecyclerView.ViewHolder{
        private ViewPager bannerSliderViewPager;
        private  List<SliderModel> sliderModelList;
        private  int currentPage = 2;
        private Timer timer;
        final private  long DELAY_TIME=3000;
        final  private  long PERIOD_TIME = 3000;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);

            bannerSliderViewPager=itemView.findViewById(R.id.bannerSlider_viewPager);




        }
        private void setBannerSliderViewPager(final List<SliderModel> sliderModelList){
            SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
            bannerSliderViewPager.setAdapter(sliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);
            bannerSliderViewPager.setCurrentItem(currentPage);
            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE){
                        pageLooper(sliderModelList);
                    }
                }
            };
            bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);
            startbannerSlideShow(sliderModelList);
            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    pageLooper(sliderModelList);
                    stopbannerSlideShow();
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                        startbannerSlideShow(sliderModelList);
                    }
                    return false;
                }
            });
        }
        private void pageLooper(List<SliderModel> sliderModelList){
            if(currentPage == sliderModelList.size() - 2){
                currentPage = 2;
                bannerSliderViewPager.setCurrentItem(currentPage,false);
            }
            if(currentPage == 1){
                currentPage = sliderModelList.size()-3;
                bannerSliderViewPager.setCurrentItem(currentPage,false);
            }
        }
        private  void startbannerSlideShow(final List<SliderModel> sliderModelList){
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= sliderModelList.size()){
                        currentPage=1;
                    }
                    bannerSliderViewPager.setCurrentItem(currentPage++);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            },DELAY_TIME,PERIOD_TIME);
        }

        private void stopbannerSlideShow(){
            timer.cancel();
        }
    }

    public class StripAddBannerholder extends RecyclerView.ViewHolder{
        private ImageView stripAddImage;
        private ConstraintLayout stripAddContainer;
        public StripAddBannerholder(@NonNull View itemView) {
            super(itemView);
            stripAddImage = itemView.findViewById(R.id.strip_add_image);
            stripAddContainer= itemView.findViewById(R.id.strip_add_container);
        }

        private  void setStripAdd(int resource,String color){
            stripAddImage.setImageResource(resource);
            stripAddContainer.setBackgroundColor(Color.parseColor(color));
        }

    }

    public  class HorizontalProductViewholder  extends RecyclerView.ViewHolder{
        private TextView horizontalLayoutTitle;
        private Button horizontalViewAllBtn;
        private RecyclerView horizontalRecyclerView;
        public HorizontalProductViewholder(@NonNull View itemView) {
            super(itemView);

            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalViewAllBtn = itemView.findViewById(R.id.horizontal_scroll_viewall_button);
            horizontalRecyclerView=itemView.findViewById(R.id.horizontal_layout_recycler_view);
        }

        private void setHorizontalProductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList, String title){
horizontalLayoutTitle.setText(title);
if(horizontalProductScrollModelList.size()> 8){
    horizontalViewAllBtn.setVisibility(View.VISIBLE);
}else{
    horizontalViewAllBtn.setVisibility(View.INVISIBLE);
}
            HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalRecyclerView.setLayoutManager(linearLayoutManager);
            horizontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();
        }


    }

    public class GridProductViewholder extends RecyclerView.ViewHolder{
             private TextView gridLayOutTitle;
             private  Button gridViewAllBtn;
             private GridView gridView;
             public GridProductViewholder(@NonNull View itemView) {
                 super(itemView);
                 gridLayOutTitle = itemView.findViewById(R.id.grid_product_layout_title);
                  gridViewAllBtn = itemView.findViewById(R.id.grid_product_layout_viewallBtn);
                  gridView = itemView.findViewById(R.id.grid_product_layout_gridView);
             }
             private void setGridProductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList,String title){
                  gridLayOutTitle.setText(title);
                 gridView.setAdapter(new GridProductLayoutAdapter(horizontalProductScrollModelList));
             }
         }
}