package com.androidbelieve.drawerwithswipetabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyServiceActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener{
    private SliderLayout mDemoSlider;
    private ArrayList<Bitmap> images=new ArrayList<>();
    private boolean imageshown=false;
    private ArrayList<String>links=new ArrayList<>();
    private ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    private ViewPager viewPager;
    private TextView name,desc,rent,date,subcat,age,projlinks,default_text,tv_city;
    private String sid;
    private MenuItem star;
    private Button rating_comments;
    private RatingBar ratingBar;
    private String canrent;
    private boolean set=false;
    private Toolbar toolbar;
    private RadioGroup radioGroup;
    private RadioButton less,more,equal;
    private boolean selected=false;
    private String rentperiod;
    private GenericAsyncTask genericAsyncTask;
    private HorizontalAdapter HorizontalAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_my_service);
        radioGroup= (RadioGroup) findViewById(R.id.rg_period);
        less= (RadioButton) findViewById(R.id.less);
        equal= (RadioButton) findViewById(R.id.equal);
        more= (RadioButton) findViewById(R.id.more);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        default_text = (TextView) findViewById(R.id.default_text);
        rating_comments= (Button) findViewById(R.id.btn_rate_comment);
        final TextView[] count=new TextView[5];
        images=new ArrayList<>();

        viewPager=(ViewPager)findViewById(R.id.pager);
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager(), images, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePager();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.rr);
        HorizontalAdapter=new MyServiceActivity.HorizontalAdapter(getApplicationContext(),images);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(HorizontalAdapter);

        count[0]=(TextView)findViewById(R.id.count1);
        count[1]=(TextView)findViewById(R.id.count2);
        count[2]=(TextView)findViewById(R.id.count3);
        count[3]=(TextView)findViewById(R.id.count4);
        count[4]=(TextView)findViewById(R.id.count5);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle("MANNNNNYNYYYYYY");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_name));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sid = getIntent().getStringExtra("sid");
        name=(TextView)findViewById(R.id.tv_sname);
        desc=(TextView)findViewById(R.id.tv_desc);
        rent=(TextView)findViewById(R.id.tv_rent);
        subcat=(TextView)findViewById(R.id.tv_subcat);
        tv_city=(TextView)findViewById(R.id.tv_city);
        //age=(TextView)findViewById(R.id.tv_prod_age);
        projlinks=(TextView)findViewById(R.id.tv_link);
        date=(TextView)findViewById(R.id.tv_date);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar1);
        ratingBar.setMax(5);
        ratingBar.setFocusable(false);
        ratingBar.setFocusableInTouchMode(false);
        ratingBar.setClickable(false);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching ad Please wait");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        GenericAsyncTask genericAsyncTaskgetservice=new GenericAsyncTask(this, Config.link+"getserviceforedit.php?sid=" + sid, "", new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                try {
                    Log.v("AB","CD");
                    fillAdd(new JSONObject((String)output).getJSONArray("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        genericAsyncTaskgetservice.execute();
        new GenericAsyncTask(this, Config.link + "sendcommentservice.php?sid=" + sid, "", new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                if(output!=null)
                {

                    String out=(String)output;
                    String[] delim=out.split(";;");
                    for(String x:delim)
                    {
                        String[] temp = x.split(",,");
                        try {
                            count[Integer.parseInt(temp[0])].setText(temp[1]);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            Log.v("Temp string",x);
                        }
                    }
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        //mDemoSlider.stopAutoCycle();
        super.onStop();
        //if(getAd!=null)
          //  if(!(getAd.getStatus()== AsyncTask.Status.FINISHED))
            //    getAd.cancel(true);
        //if(genericAsyncTask!=null)
          //  if(!(genericAsyncTask.getStatus()== AsyncTask.Status.FINISHED))
            //    genericAsyncTask.cancel(true);
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }*/


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onRent(final String message)
    {
        AsyncTask<String,String,String> s=new AsyncTask<String, String, String>() {
            String link=Config.link+"reqNoti.php?pid=&message="+message+" for "+rentperiod;
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(link+params[0]+"&sid="+params[1]);
                    URLConnection connection=url.openConnection();

                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line=null;
                    StringBuffer sb=new StringBuffer();
                    while((line=br.readLine())!=null)
                        sb.append(line);

                    Log.v("Result",sb.toString());
                    return sb.toString();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String result)
            {

            }
        };
        s.execute(AccessToken.getCurrentAccessToken().getUserId(),sid);
    }

    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    class GetAd extends AsyncTask<String, String, String> {
        String sid,pid;
        GetAd(String sid,String pid) {
            this.sid = sid;
            this.pid=pid;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {

                String link = Config.link+"getserviceforedit.php?sid=" + sid+"&pid="+pid;
                Log.v("link",link);
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                Log.v("INSIDE","GETAD");
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.v("Result", result);
            } catch (Exception e) {
                // Oops
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jobj = new JSONObject(result);
                fillAdd(jobj.getJSONArray("result"));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
    static String Month(Date d)
    {
        int i=d.getMonth();
        switch(i)
        {
            case 0: return "Jan";
            case 1: return "Feb";
            case 2: return "Mar";
            case 3: return "Apr";
            case 4: return "May";
            case 5: return "Jun";
            case 6: return "July";
            case 7: return "Aug";
            case 8: return "Sept";
            case 9: return "Oct";
            case 10: return "Nov";
            case 11: return "Dec";
            default:return "Dec";
        }

    }


    void fillAdd(JSONArray jarray)
    {

        try {
            JSONObject c = jarray.getJSONObject(0);

            String prod_name=c.getString("SNAME");
            String rent_name=c.getString("RENT");
            String desc_str=c.getString("DESC");
            String cat=c.getString("CAT");

            String timestamp=c.getString("TIMESTAMP");
            String city=c.getString("CITY");
            String subcat=c.getString("SUBCAT");

            tv_city.setText(city);
            JSONArray links=c.getJSONArray("SLINKS");
            String allprojlinks="";
            for(int i=0;i<links.length();i++) {
                this.links.add(links.getJSONObject(i).getString("link"));
                allprojlinks+=links.getJSONObject(i).getString("link")+"\n";
            }

            if(allprojlinks.isEmpty())
                projlinks.setText("No Links to Preview");
            else
                projlinks.setText(allprojlinks);

            JSONArray ilinks=c.getJSONArray("LINKS");
            ArrayList<String> alllinks=new ArrayList<>();
            for(int i=0;i<ilinks.length();i++)
                alllinks.add(ilinks.getJSONObject(i).getString("link"));

            //canrent=c.getString("CANRATE");

            Date today=new Date();
            String ddate;
            Date yesterday=new Date();
            yesterday.setTime(today.getTime()-((long)864E5));
            Date date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
            date.setTime(date.getTime()+19800000);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
            Log.v("timeStamp",timestamp);
            Log.v("date",date.toString());
            if(sdf.format(today).equals(sdf.format(date)))
                ddate="Today ";
            else if(sdf.format(yesterday).equals(sdf.format(date)))
                ddate="Yesterday ";
            else
            {

                //this.date=date.getDay()+" "+Month(date)+" ";
                ddate=new SimpleDateFormat("d MMMM").format(date);
                if(!(today.getYear()==date.getYear()))
                    ddate+=" "+date.getYear()+" ";
            }this.date.setText(ddate);
            this.subcat.setText(subcat);
            name.setText(prod_name);
            toolbar.setTitle(prod_name);
            desc.setText(desc_str);
            rent.setText("₹ "+ rent_name);
            final int length[]={alllinks.size()};
            if(alllinks.size()==0)
            {
                //Set photos to null
                recyclerView.setVisibility(View.GONE);
                default_text.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
            progressDialog.dismiss();

            /*for(String x:alllinks) {

                Picasso.with(this).load(x).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        if(length[0]--==1)
                            progressDialog.dismiss();

                        images.add(bitmap);
                        HorizontalAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
                Log.v("link in picasso",x);
            }
            */
            HorizontalAdapter.addLinks(alllinks);

        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
    public void onShare(/*View view*/){
        Intent intent= new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"http://www.rentandget.co.in/service/"+sid);

        startActivity(Intent.createChooser(intent,"Sharing Option"));
    }

    public void onRateAndComment(View view){
        Intent i=new Intent(this,RateActivity.class);
        i.putExtra("sid",sid);
        i.putExtra("CANRATE",canrent);
        startActivity(i);
    }

    /*void getMenus(Menu menu)
    {
        star=menu.findItem(R.id.action_wishlist);
        GenericAsyncTask g=new GenericAsyncTask(this, "http://rng.000webhostapp.com/checkwishlist.php?aid=" + sid + "&pid=" + AccessToken.getCurrentAccessToken().getUserId(), "", new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String out=(String)output;
                if(out.equals("1"))
                {
                    star.setIcon(android.R.drawable.btn_star_big_on);
                    set=!set;
                    Log.v("output of async",out);
                }
                else
                {
                    star.setIcon(android.R.drawable.btn_star_big_off);
                    set=false;
                    Log.v("output of async",out);
                }
            }
        });
        g.execute();
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_my_ad, menu);

        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                onShare();
                return true;
           /* case R.id.action_wishlist:
                GenericAsyncTask g=new GenericAsyncTask(this, "http://rng.000webhostapp.com/wishlist.php?aid=" + sid + "&pid=" + AccessToken.getCurrentAccessToken().getUserId(), "", new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if(set)
                            star.setIcon(android.R.drawable.btn_star_big_off);
                        else
                            star.setIcon(android.R.drawable.btn_star_big_on);

                        set=!set;
                    }
                });
                g.execute();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onUrgentRent(View view){
        if(!selected) {
            //radioGroup.requestFocusInWindow();
            radioGroup.requestFocus(View.LAYOUT_DIRECTION_LOCALE);
            return;
        }

        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        alertbox.setTitle("Urgent Rent");
        alertbox.setMessage("Do you want to urgently rent this time ?");

        alertbox.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        onRent("");
                    }
                });

        alertbox.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        onRent("urgently rent");
                    }
                });
        alertbox.show();
    }
    class HorizontalAdapter  extends RecyclerView.Adapter<MyServiceActivity.HorizontalAdapter.MyViewHolder> {
        private Context mContext;
        private ArrayList<Bitmap> images;
        private ArrayList<String> links=new ArrayList<>();
        private int thumbnail=0;
        public HorizontalAdapter(Context mContext, final ArrayList<Bitmap> images) {
            this.mContext = mContext;
            this.images=images;
            Log.v("Adapter created","Created");

        }
        void setPosition(int i)
        {
            thumbnail=i;
        }

        @Override
        public MyServiceActivity.HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_pic, parent, false);
            Log.v("oncreateViewholder","currect");
            return new MyServiceActivity.HorizontalAdapter.MyViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(final MyServiceActivity.HorizontalAdapter.MyViewHolder holder, final int position) {
            Target t=new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if(position<images.size())
                        images.add(position,bitmap);
                    else
                        images.add(bitmap);
                    holder.i.setImageBitmap(bitmap);
                    holder.i.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reInstantiatePager();
                            reInstantiatePager();
                            imageshown=true;
                            viewPager.setVisibility(View.VISIBLE);
                            viewPager.setCurrentItem(position);

                        }
                    });
                    holder.i.setAnimation(null);

                    Log.v("Bitmap set!","okay");
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    holder.i.setImageDrawable(errorDrawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    //holder.i.setImageDrawable(placeHolderDrawable);
                    holder.i.setImageDrawable(placeHolderDrawable);
                    RotateAnimation anim = new RotateAnimation(0f, 350f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
                    anim.setInterpolator(new LinearInterpolator());
                    anim.setRepeatCount(Animation.INFINITE);
                    anim.setDuration(700);
                    holder.i.startAnimation(anim);


                }
            };
            holder.i.setTag(t);

            Picasso.with(mContext).load(links.get(position)).error(R.drawable.car).placeholder(R.drawable.loadingpic).into(t);

            Log.v("inside","holder setting bitmap");
            /**
             *Set all onclicks here
             *
             */

            holder.relativeLayout.setBackgroundResource(R.drawable.empty);

            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    images.remove(position);
                    if(thumbnail==position) {
                        thumbnail = 0;
                    }
                    notifyDataSetChanged();
                    reInstantiatePager();                }
            });
        }
        @Override
        public int getItemCount() {
            return this.links.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            Button set;
            ImageView i;
            ImageButton del;
            RelativeLayout relativeLayout;
            public MyViewHolder(View view) {
                super(view);
                del = (ImageButton) view.findViewById(R.id.yes_bt);
                del.setVisibility(View.GONE);
                i = (ImageView) view.findViewById(R.id.act_image);
                relativeLayout=(RelativeLayout)view.findViewById(R.id.rel_lay);
                //set=(Button)view.findViewById(R.id.button);
            }
        }
        public void addLinks(ArrayList<String> newLinks)
        {
            this.links.addAll(newLinks);
            Log.v("links added","okay");
            HorizontalAdapter.notifyDataSetChanged();
        }
    }
    void reInstantiatePager()
    {
        viewPager.setAdapter(null);
        viewPager.setAdapter(new ImageFragmentPagerAdapter(getSupportFragmentManager(), images, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePager();
            }
        }));

    }
    void hidePager() {
        imageshown = false;
        Log.v("Clicked", "Hidden?");
        viewPager.setVisibility(View.GONE);

    }

}
