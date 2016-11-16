package com.androidbelieve.drawerwithswipetabs;
/**
 *Contains link!!
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by karthik on 12/10/16.
 */

class Newaddupload extends AsyncTask<String,Integer,String> {

    String pid,prod_name,description,prod_age,category,rent,prod_deposit,f1,f2,city;
    private ArrayList<Bitmap> images;
    private Context context;
    ProgressDialog progress;
    Newaddupload(String pid, String prod_name, String description, String prod_age, String category, String rent, String prod_deposit, ArrayList<Bitmap> images,Context c,String f1,String f2,String city)
    {
        this.pid=pid;
        this.prod_name= URLEncoder.encode(prod_name);
        this.description= URLEncoder.encode(description);
        this.prod_age=prod_age;
        this.category=category;
        this.rent=rent;
        this.prod_deposit=prod_deposit;
        this.images=images;
        this.context=c;
        this.f1=f1;
        this.f2=f2;
        this.city=city;
    }



    @Override
    protected void onPreExecute()
    {
        progress = new ProgressDialog(context);
        progress.setMessage("Uploading Images");
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setMax(images.size());
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String link = "http://rng.000webhostapp.com/newad.php?pid="+pid+"&prod_name="+prod_name+"&description="+description+"&prod_age="+prod_age+"&category="+category+"&rent="+rent+"&prod_deposit="+prod_deposit+"&num="+Integer.toString(images.size())+"&crent="+f2+"&maxrent="+f1+"&city="+city;
            // String link = "http://dharam.hostfree.pw/newad.php?pid="+pid+"&prod_name="+prod_name+"&description="+description+"&prod_age="+prod_age+"&category="+category+"&rent="+rent+"&prod_deposit="+prod_deposit+"&num="+Integer.toString(images.size());
            Log.v("link",link);
            URL url = new URL(link);

            URLConnection con = url.openConnection();

            con.setDoOutput(true);

            String data = "";
            for (int i = 0; i < images.size(); i++) {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                images.get(i).compress(Bitmap.CompressFormat.PNG, 90, stream);
                String encodedString = Base64.encodeToString(stream.toByteArray(), 0);
                Log.v("EncodedString", encodedString);
                Log.v("image", "image" + Integer.toString(i));

                data += URLEncoder.encode("image" + Integer.toString(i + 1), "UTF-8") + "=" + URLEncoder.encode(encodedString, "UTF-8") + "&";
            }
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                Log.v("line",line);
            }
            Log.v("sb",sb.toString());

            return sb.toString();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        progress.dismiss();
        if(result!=null)
        {

            /*try {
            Integer.parseInt(result);
            new AdFragment.UploadAd(images,result).execute();

        }
        catch (Exception e)
        {

            e.printStackTrace();

        }
        */
            //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
            Log.v("Success","Success");
        }


    }
}