/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer.mymodule.appmanager.Utils;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.przyjaznyplan.models.Activity;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class ActivitySimpleAdapter extends ArrayAdapter<Activity> {
    private List<Activity> objects;
    private LayoutInflater mInflater;
    private int mViewResourceId;
    private int backgroundColor;
    private int textColor;
    BitmapFactory.Options options;
    Context ctx;
    Bitmap mPlaceHolderBitmap;

    public ActivitySimpleAdapter(Context ctx, int textViewResourceId, int id, List<Activity> objects) {
        super(ctx, textViewResourceId, objects);
        this.ctx=ctx;
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
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Activity getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void changeChildActivity(Activity oldCA,Activity newCA){
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
            return decodeSampledBitmapFromResource(path, 0, 35, 35);
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
        if (cancelPotentialWork(path, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView,path);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(ctx.getResources(), mPlaceHolderBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute();
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

        public ViewHolder(
                TextView label,
                ImageView sound,
                ImageView activityImage,
                ImageView activityTimer,
                Activity activity) {
            this.label=label;
            this.sound=sound;
            this.activityImage=activityImage;
            this.activityTimer=activityTimer;
        }
    }

    @Override
    public View getView(int position, View v, ViewGroup parent){

        TextView label;
        ImageView sound;
        ImageView activityImage;
        ImageView activityTimer;

        Activity activity = objects.get(position);

        if(v==null){
            v = mInflater.inflate(mViewResourceId, null);
            label = (TextView) v.findViewById(R.id.label);
            sound=(ImageView)(v.findViewById(R.id.con3));
            activityImage=(ImageView)(v.findViewById(R.id.con));
            activityTimer=(ImageView)(v.findViewById(R.id.con2));
            v.setTag(new ViewHolder(label,sound,activityImage,activityTimer, activity));
        }
        else{
            ViewHolder vh=(ViewHolder)v.getTag();
            label = vh.label;
            sound= vh.sound;
            activityImage=vh.activityImage;
            activityTimer=vh.activityTimer;
        }

        if (activity != null) {
            if (label != null){
                label.setText(activity.getTitle());
                if(activity.getStatus() !=null && activity.getStatus().equals(Activity.ActivityStatus.FINISHED.toString())){
                    label.setTextColor(Color.BLACK);
                    v.setBackgroundColor(Color.LTGRAY);
                }
                else{
                    label.setTextColor(Color.WHITE);
                    v.setBackgroundColor(Color.TRANSPARENT);
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
                if(!(activity.getIconPath()==null || activity.getIconPath().equals(""))){
                    try {
                        loadBitmap(activity.getIconPath(),activityImage);
                        activityImage.setVisibility(View.VISIBLE);
                    }catch(Exception e){
                    }
                }
            }
            if (activityTimer != null){
                if(activity.getTime()==0){
                    activityTimer.setVisibility(View.INVISIBLE);
                }else{
                    activityTimer.setVisibility(View.VISIBLE);
                }
            }

        }
        return v;
    }

}
