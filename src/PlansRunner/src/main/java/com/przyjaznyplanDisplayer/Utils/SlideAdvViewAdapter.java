/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.przyjaznyplan.R;
import com.przyjaznyplan.models.Slide;

import java.lang.ref.WeakReference;
import java.util.List;

public class SlideAdvViewAdapter extends ArrayAdapter<Slide> {
    private List<Slide> objects;
    private LayoutInflater mInflater;
    private int mViewResourceId;
    private int backgroundColor;
    private int textColor;
    public int currentPosition;
    BitmapFactory.Options options;
    Context ctx;
    Bitmap mPlaceHolderBitmap;
    private LruCache<String, Bitmap> mMemoryCache;
    private OnTimerClickInterface controller;

    public SlideAdvViewAdapter(Context ctx, int textViewResourceId, int id, List<Slide> objects, int current, OnTimerClickInterface controller) {
        super(ctx, textViewResourceId, objects);
        this.ctx=ctx;
        this.controller = controller;
        this.currentPosition = current;
        mInflater = (LayoutInflater)ctx.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.objects = objects;
        mViewResourceId = textViewResourceId;
        TypedArray array = ctx.getTheme().obtainStyledAttributes(new int[] {
                android.R.attr.colorBackground,
                android.R.attr.textColorPrimary,
        });
        backgroundColor = array.getColor(0,Color.TRANSPARENT);
        textColor = array.getColor(1, Color.BLACK);
        array.recycle();
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(ctx.getResources(), R.id.con, options);
        mPlaceHolderBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.t1);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Slide getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void changeChildActivity(Slide oldCA,Slide newCA){
        int index=objects.indexOf(oldCA);
        if(index>-1){
            objects.remove(index);
            objects.add(index, newCA);
            this.notifyDataSetChanged();
        }
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String path;

        public BitmapWorkerTask(ImageView imageView, String path) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.path = path;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            final Bitmap bitmap = decodeSampledBitmapFromResource(path, 0, 100, 100);
            addBitmapToMemoryCache(String.valueOf(path), bitmap);
            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask =
                        getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public static boolean cancelPotentialWork(String path, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.path;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == null || bitmapData.equals("") || bitmapData!=path) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    public void loadBitmap(String path, ImageView imageView) {
        final String imageKey = path;

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            if (cancelPotentialWork(path, imageView)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(imageView,path);
                final AsyncDrawable asyncDrawable =
                        new AsyncDrawable(ctx.getResources(), mPlaceHolderBitmap, task);
                imageView.setImageDrawable(asyncDrawable);
                task.execute();
            }
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String path, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }


    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    private static class ViewHolder {

        public final TextView label;
        public final ImageView sound;
        public final ImageView activityImage;
        public final ImageView activityTimer;
        public final TextView activityTimerLabel;

        public ViewHolder(
                TextView label,
                ImageView sound,
                ImageView activityImage,
                ImageView activityTimer,
                TextView activityTimerLabel,
                Slide activity) {
            this.label=label;
            this.sound=sound;
            this.activityImage=activityImage;
            this.activityTimer=activityTimer;
            this.activityTimerLabel = activityTimerLabel;
        }
    }

    @Override
    public View getView(int position, View v, ViewGroup parent){

        TextView label;
        ImageView sound;
        ImageView activityImage;
        ImageView activityTimer;
        TextView activityTimerLabel;
        Slide activity = objects.get(position);

        if(v==null){
            v = mInflater.inflate(mViewResourceId, null);
            label = (TextView) v.findViewById(R.id.label);
            sound=(ImageView)(v.findViewById(R.id.con3));
            activityImage=(ImageView)(v.findViewById(R.id.con));
            activityTimer=(ImageView)(v.findViewById(R.id.con2));
            activityTimer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    controller.onLongTimerClickFromAdapter(view);
                    return true;
                }
            });
            activityTimerLabel = (TextView) v.findViewById(R.id.ad_v_timer);
            v.setTag(new ViewHolder(label,sound,activityImage,activityTimer,activityTimerLabel, activity));
        }
        else{
            ViewHolder vh=(ViewHolder)v.getTag();
            label = vh.label;
            sound= vh.sound;
            activityImage=vh.activityImage;
            activityTimer=vh.activityTimer;
            activityTimerLabel = vh.activityTimerLabel;

        }

        //v = mInflater.inflate(mViewResourceId, null);

        if (activity != null) {
            if (label != null){
                label.setText(activity.getText());
                label.setTextColor(Color.BLACK);
                if(activity.getStatus()==Slide.SlideStatus.FINISHED.getValue()){
                    label.setPaintFlags(label.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    v.setBackgroundColor(Color.LTGRAY);
                }
                else{
                    label.setPaintFlags(label.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    v.setBackgroundColor(Color.WHITE);
                }
            }
            if (sound != null){
                if(activity.getAudioPath()==null || activity.getAudioPath().equals("")){
                    sound.setVisibility(View.INVISIBLE);
                }else{
                    sound.setVisibility(View.VISIBLE);
                    sound.setTag(activity.getAudioPath());
                }
            }
            if(activityImage != null){
                activityImage.setVisibility(View.INVISIBLE);
                if(!(activity.getImagePath()==null || activity.getImagePath().equals(""))){
                    try {
                        //Bitmap bmp = BitmapFactory.decodeFile(activity.getIconPath(),options);
                        //Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 100, 100, false);
                        //activityImage.setImageBitmap(
                        //      decodeSampledBitmapFromResource(activity.getIconPath(), 0, 100, 100));
                        loadBitmap(activity.getImagePath(), activityImage);
                        activityImage.setVisibility(View.VISIBLE);
                    }catch(Exception e){
                    }
                }
            }
            if (activityTimer != null){
                if(activity.getTime()==0){
                    activityTimerLabel.setText("");
                    activityTimerLabel.setVisibility(View.INVISIBLE);
                    activityTimer.setVisibility(View.INVISIBLE);
                }else{
                    if(activity.getTime()==-1){
                        activityTimerLabel.setText("");
                        activityTimerLabel.setVisibility(View.INVISIBLE);
                        activityTimer.setVisibility(View.VISIBLE);
                    } else {
                        String st=(String)activityTimer.getTag();
                        if(st!=null && !st.equals("")) {
                            activityTimerLabel.setText("" + st+ "s");
                        }else
                        {
                            activityTimerLabel.setText("" +activity.getTime()+"s");
                        }
                        activityTimerLabel.setVisibility(View.VISIBLE);
                        activityTimer.setVisibility(View.VISIBLE);
                    }
                }
            }
            if(this.currentPosition!=-1&&this.currentPosition==position){
                v.setBackgroundColor(Color.GREEN);
            }
        }
        //this.notifyDataSetChanged();
        return v;
    }

}
