package com.androidbelieve.drawerwithswipetabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

public class MyActiveServiceAdapter extends RecyclerView.Adapter<MyActiveServiceAdapter.MyViewHolder> {

    private Context mContext;
    private List<MyAds> serviceList;
    static MyAds MyServices;
    static int pos=0;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView status,specs,price,date,subcat;
        public ImageView ads,overflow;

        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view_service);
            status= (TextView) view.findViewById(R.id.tv_status1);
            price = (TextView) view.findViewById(R.id.tv_rent);
            specs = (TextView) view.findViewById(R.id.tv_name1);
            date = (TextView) view.findViewById(R.id.tv_date);
            ads = (ImageView) view.findViewById(R.id.iv_ads);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            subcat=(TextView)view.findViewById(R.id.tv_subcat);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), "MANNY", Toast.LENGTH_SHORT).show();

                    return true;
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });
        }
    }

    public MyActiveServiceAdapter(Context mContext, List<MyAds> serviceList) {
        this.mContext = mContext;
        this.serviceList = serviceList;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MyAds ad = serviceList.get(position);
        holder.status.setText("Active");
        holder.status.setTextColor(Color.parseColor("#79ff4d"));
        holder.subcat.setText(ad.getSubcat());
        holder.specs.setText(ad.getSpecs());
        holder.date.setText(ad.getDate());
        holder.price.setText("₹ " + ad.getPrice() );
        holder.subcat.setText(ad.getSubcat());
        new DisplayImage(ad.getsLink(),holder.ads).execute();
        final String aid=ad.getAid();
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyServices=ad;
                pos=position;
                showPopupMenu(holder.overflow);
            }
        });
        holder.ads.setOnClickListener(new View.OnClickListener() {
         @Override
          public void onClick(View view) {
          Intent i=new Intent(mContext,AdActivity.class);
         i.putExtra("SID",aid);
         mContext.startActivity(i);

        }
        });
        holder.setIsRecyclable(false);
        Glide.with(mContext).load(ad.getImage_ads()).into(holder.ads);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_album, parent, false);

        return new MyViewHolder(itemView);
    }


    public int getItemCount() {
        return serviceList.size();
    }


    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_active_service, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit: {
                    Toast.makeText(mContext, "Edit Ad", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mContext,EditServiceActivity.class);
                    intent.putExtra("SID",MyServices.getAid());
                    mContext.startActivity(intent);
                    return true;}
                case R.id.action_delete: {
                    GenericAsyncTask g=new GenericAsyncTask(mContext, "http://rng.000webhostapp.com/deletead.php?sid=" + MyServices.getAid(), "", new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            String out=(String)output;
                            if(out.equals("success")) {
                                serviceList.remove(pos);
                                pos = 0;
                                notifyDataSetChanged();
                            }
                        }
                    });
                    g.execute();
                    Toast.makeText(mContext, "Delete Ad", Toast.LENGTH_SHORT).show();
                }
                return true;
                case R.id.action_deactivate:
                    GenericAsyncTask g=new GenericAsyncTask(mContext, "http://rng.000webhostapp.com/changestatus.php?sid=" + MyServices.getAid()+"&status=DEACTIVE", "", new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            String out=(String)output;
                            if(out.equals("success")) {
                                serviceList.remove(pos);
                                pos = 0;
                                notifyDataSetChanged();
                            }
                        }
                    });
                    g.execute();
                    Toast.makeText(mContext, "Deactivate Ad", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }

    }
}

