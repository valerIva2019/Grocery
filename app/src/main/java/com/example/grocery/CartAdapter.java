package com.example.grocery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {
private List<CartitemModel> cartitemModelList;

    public CartAdapter(List<CartitemModel> cartitemModelList) {
        this.cartitemModelList = cartitemModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartitemModelList.get(position).getType()){
            case 0:
                return CartitemModel.CART_ITEM;
            case 1:
                return CartitemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       switch (viewType){

           case CartitemModel.CART_ITEM:
               View cartitemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
               return new CartItemViewholder(cartitemView);

               case CartitemModel.TOTAL_AMOUNT:
                   View cartTotalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout,parent,false);
                   return new CartTotalAmountViewholder(cartTotalView);
           default:
               return null;
       }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
              switch (cartitemModelList.get(position).getType()){

                  case CartitemModel.CART_ITEM:
                      int resource = cartitemModelList.get(position).getProductImage();
                      String title= cartitemModelList.get(position).getProductTitle();
                      int freeCoupens = cartitemModelList.get(position).getFreecoupens();
                      String productPrice = cartitemModelList.get(position).getProductPrice();
                      String cuttedPrice= cartitemModelList.get(position).getCuttedPrice();
                      int offersApplied = cartitemModelList.get(position).getOffersApplied();
                      ((CartItemViewholder)holder).setItemDetails(resource,title,freeCoupens,productPrice,cuttedPrice,offersApplied);

                      break;
                      case CartitemModel.TOTAL_AMOUNT:
                        String totalItems = cartitemModelList.get(position).getTotalItems();
                          String totalItemPrice = cartitemModelList.get(position).getTotalItemPrice();
                          String deliveryPrice = cartitemModelList.get(position).getDeliveryPrice();
                          String totalAmount = cartitemModelList.get(position).getTotalAmount();
                          String savedAmount = cartitemModelList.get(position).getSavedAmount();
                          ((CartTotalAmountViewholder)holder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount,savedAmount);
                          break;
                  default:
                      return;
              }
    }

    @Override
    public int getItemCount() {
        return cartitemModelList.size();
    }
    class CartItemViewholder extends RecyclerView.ViewHolder{

        private ImageView productImage,freeCoupensIcon;
        private TextView productTitle,freeCoupens,productPrice,cuttedPrice,offersApplied,coupensApplied,productQuantity;


        public CartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupensIcon = itemView.findViewById(R.id.free_coupen_icon);
            freeCoupens = itemView.findViewById(R.id.tv_free_coupen);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            coupensApplied = itemView.findViewById(R.id.coupens_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
        }
        private void setItemDetails(int resource,String title,int freeCoupensNo,String productPriceText,String cuttedPriceText, int offersAppliedNo) {
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if (freeCoupensNo > 0) {
                if (freeCoupensNo == 1) {
                    freeCoupensIcon.setVisibility(View.VISIBLE);
                    freeCoupens.setVisibility(View.VISIBLE);
                    freeCoupens.setText("free " + freeCoupensNo + " coupen");
                } else {
                    freeCoupens.setText("free " + freeCoupensNo + " coupens");
                }
            } else {
                freeCoupensIcon.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
            }

            productPrice.setText(productPriceText);
            cuttedPrice.setText(cuttedPriceText);
         if (offersAppliedNo>0){
             offersApplied.setVisibility(View.VISIBLE);
             offersApplied.setText(offersAppliedNo+" Offers Applied");
         }else {
             offersApplied.setVisibility(View.INVISIBLE);
         }

        }
    }


     class CartTotalAmountViewholder extends RecyclerView.ViewHolder{
     private TextView totalItems,totalItemPrice,deliveryPrice,totalAmount,savedAmount;

         public CartTotalAmountViewholder(@NonNull View itemView) {
             super(itemView);

             totalItems = itemView.findViewById(R.id.total_items);
             totalItemPrice= itemView.findViewById(R.id.total_items_price);
             deliveryPrice = itemView.findViewById(R.id.delivery_price);
             totalAmount = itemView.findViewById(R.id.total_price);
             savedAmount= itemView.findViewById(R.id.saved_amount);

         }

         private void setTotalAmount(String totalItemText,String totalItemPriceText,String deliveryPriceText,String totalAmountText,String savedAmountText){

             totalItems.setText(totalItemText);
             totalItemPrice.setText(totalItemPriceText);
             deliveryPrice.setText(deliveryPriceText);
             totalAmount.setText(totalAmountText);
             savedAmount.setText(savedAmountText);
         }
     }

}
