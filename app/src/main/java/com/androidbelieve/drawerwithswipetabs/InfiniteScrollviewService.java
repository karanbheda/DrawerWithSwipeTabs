package com.androidbelieve.drawerwithswipetabs;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by 20000136 on 11/18/2016.
 */

public class InfiniteScrollviewService extends RecyclerView.OnScrollListener {


        private final int VIEW_ITEM = 1;
        private final int VIEW_PROG = 0;

        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;
        private boolean loading;
        private ServiceCategoryAdapter adapter;
        private List<ServiceAlbum> albumList;
        private String category;
        private GenericAsyncTask genericAsyncTask;
        private String sort="",filter="";

    private boolean listend=false;

        public InfiniteScrollviewService(ServiceCategoryAdapter adapter, List<ServiceAlbum> albumList,String category)
        {

            this.adapter=adapter;
            this.albumList=albumList;
            this.category=category;
        }

    void updateSortandFilter(String sort,String filter)
    {
        this.sort=sort;
        this.filter=filter;
    }


        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            super.onScrolled(recyclerView, dx, dy);
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                        .getLayoutManager();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition();
                if (!loading&& totalItemCount <= (lastVisibleItem + visibleThreshold)&&!listend) {
                    //new GetJSON("http://rng.000webhostapp.com/viewads.php?category", lastVisibleItem+1, adapter, albumList).execute();
                    GenericAsyncTask genericAsyncTask=new GenericAsyncTask(null, Config.link+"showservice.php?category="+ URLEncoder.encode(category)+"&order="+sort+"&filter="+URLEncoder.encode(filter)+ "&OFF=" + Integer.toString(lastVisibleItem + 1), "", new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            String out=(String)output;
                            if(!out.equals("{\"result\":[]}"))
                            {
                                try {
                                    JSONObject jobj = new JSONObject(out);
                                    prepareAlbum(jobj.getJSONArray("result"));
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                listend=true;
                            }
                        }
                    });
                    genericAsyncTask.execute();
                    loading = true;

                }
            }
        }
    void stopAsyncTask() throws NullPointerException
    {
        if(genericAsyncTask!=null)
            if(genericAsyncTask.getStatus()== AsyncTask.Status.FINISHED)
                genericAsyncTask.cancel(true);
    }
    void prepareAlbum(JSONArray jarray)
    {
        loading=false;
        for(int i=0;i<jarray.length();i++)
        {
            try {
                JSONObject ad = jarray.getJSONObject(i);
                String name = ad.getString("SNAME");
                String sid = ad.getString("SID");
                int amount = Integer.parseInt(ad.getString("AMOUNT"));
                String timestamp=ad.getString("TIMESTAMP");
                String subcat=ad.getString("SUBCAT");
                String pid=ad.getString("PID");
                String city=ad.getString("CITY");
                albumList.add(new ServiceAlbum(pid,name,amount,R.drawable.broly,sid,timestamp,subcat,city));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
       adapter.notifyDataSetChanged();

    }

}
