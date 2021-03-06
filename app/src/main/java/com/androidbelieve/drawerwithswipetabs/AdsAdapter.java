package com.androidbelieve.drawerwithswipetabs;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;


public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Ads> adList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView status, specs, price, date;
        public ImageView ads,overflow;


        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view_ads);
            status = (TextView) view.findViewById(R.id.tv_status);
            price = (TextView) view.findViewById(R.id.tv_price);
            specs = (TextView) view.findViewById(R.id.tv_specs);
            date = (TextView) view.findViewById(R.id.tv_date);
            ads = (ImageView) view.findViewById(R.id.iv_ads);
            overflow = (ImageView) view.findViewById(R.id.overflow);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), "MANNY", Toast.LENGTH_SHORT).show();

                    return true;
                }
            });
        }
    }

    public AdsAdapter(Context mContext, List<Ads> adList) {
        this.mContext = mContext;
        this.adList = adList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ad_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Ads ads = adList.get(position);
        holder.status.setText(ads.getStatus());
        /*if(holder.status.equals("Active"))
                holder.status.setTextColor(Color.parseColor("#80ff80"));*/
        holder.specs.setText(ads.getSpecs());
        holder.date.setText(ads.getDate());
        holder.price.setText("Rs " + ads.getPrice());

        // loadingpic album cover using Glide library
        Glide.with(mContext).load(ads.getImage_ads()).into(holder.ads);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_active_ad, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    Toast.makeText(mContext, "Edit Ad", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_delete:
                    Toast.makeText(mContext, "Delete Ad", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_deactivate:
                    Toast.makeText(mContext, "Deactivate Ad", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }


    }
    @Override
    public int getItemCount() {
        return adList.size();
    }
}