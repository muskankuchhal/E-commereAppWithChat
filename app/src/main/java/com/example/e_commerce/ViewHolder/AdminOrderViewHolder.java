package com.example.e_commerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.interfaces.ItemClickListener;

public class AdminOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView adminShipmentName, adminShipmentPhone, adminShipmentAddressCity, adminShipmentDateTime;
    public Button getDetails,shipOrder;
    private ItemClickListener itemClicklistener;

    public AdminOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        adminShipmentName = (TextView) itemView.findViewById(R.id.admin_shipment_name);
        adminShipmentPhone = (TextView) itemView.findViewById(R.id.admin_shipment_phone);
        adminShipmentAddressCity = (TextView) itemView.findViewById(R.id.admin_shipment_address_city);
        adminShipmentDateTime = (TextView) itemView.findViewById(R.id.admin_shipment_date_time);

        getDetails=(Button)itemView.findViewById(R.id.get_order_details);
        shipOrder=(Button)itemView.findViewById(R.id.ship_order);

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClicklistener = listener;
    }

    @Override
    public void onClick(View v) {
        itemClicklistener.OnClick(itemView, getAdapterPosition(), false);

    }
}
