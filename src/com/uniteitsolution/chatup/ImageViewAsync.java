package com.uniteitsolution.chatup;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageViewAsync {
	final static int TIMEOUT = 15000;
	ImageView imageview;
	Bitmap imgBitmap = null;
	
	public ImageViewAsync(String url, ImageView imageview) {
		this.imageview = imageview;
		new LoadImageAsync().execute(url);
	}
	
	class LoadImageAsync extends AsyncTask<String, Integer, Long> {
		 
        @Override
        protected Long doInBackground(String... params) {
                        // TODO Auto-generated method stub
                        final String imageUrl = params[0];
                        Bitmap bitmap = null;
                        try {
                        	URL url = new URL(imageUrl.trim());
                			InputStream input = null;
                			URLConnection conn = url.openConnection();
                			
                			HttpURLConnection httpConn = (HttpURLConnection)conn;
                			httpConn.setRequestMethod("GET");
                			httpConn.setReadTimeout(TIMEOUT); 
                			httpConn.connect(); 
                	 
                			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                				input = httpConn.getInputStream();
                			}
                        	bitmap = BitmapFactory.decodeStream(input);
                        	httpConn.disconnect();
                        	
                        	//bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
                        } catch (MalformedURLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        imgBitmap = bitmap;
                        return null;
        }
        @Override
        protected void onPostExecute(Long result) {
        	// TODO Auto-generated method stub
            // set bitmap to imageView
            imageview.setImageBitmap(imgBitmap);
            super.onPostExecute(result);
        }
	}
	
}

