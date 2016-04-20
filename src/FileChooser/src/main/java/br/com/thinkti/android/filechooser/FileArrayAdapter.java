//From: https://github.com/mypapit/android-file-chooser

package br.com.thinkti.android.filechooser;

import java.lang.ref.WeakReference;
import java.util.List;

import br.com.thinkti.android.filechooser.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileArrayAdapter extends ArrayAdapter<Option> {

	private Context c;
	private int id;
	private List<Option> items;
    private Bitmap mPlaceHolderBitmap;
    private Context ctx;

	public FileArrayAdapter(Context context, int textViewResourceId,
			List<Option> objects) {
		super(context, textViewResourceId, objects);
		c = context;
		id = textViewResourceId;
		items = objects;
        mPlaceHolderBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.t1);
        ctx = context;
	}

	public Option getItem(int i) {
		return items.get(i);
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
            return decodeSampledBitmapFromResource(path, 0, 100, 100);
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(id, null);
		}
		final Option o = items.get(position);
		if (o != null) {
			ImageView im = (ImageView) v.findViewById(R.id.img1);
			TextView t1 = (TextView) v.findViewById(R.id.TextView01);
			TextView t2 = (TextView) v.findViewById(R.id.TextView02);
			
			if(o.getData().equalsIgnoreCase("folder")){
				im.setImageResource(R.drawable.folder);
			} else if (o.getData().equalsIgnoreCase("parent directory")) {
				im.setImageResource(R.drawable.back32);
			} else {
				String name = o.getName().toLowerCase();
				if (name.endsWith(".xls") ||  name.endsWith(".xlsx"))
					im.setImageResource(R.drawable.xls);
				else if (name.endsWith(".doc") ||  name.endsWith(".docx"))
					im.setImageResource(R.drawable.doc);
				else if (name.endsWith(".ppt") ||  o.getName().endsWith(".pptx"))
					im.setImageResource(R.drawable.ppt);
				else if (name.endsWith(".pdf"))
					im.setImageResource(R.drawable.pdf);
				else if (name.endsWith(".apk"))
					im.setImageResource(R.drawable.android32);
				else if (name.endsWith(".txt"))
					im.setImageResource(R.drawable.txt32);
				else if (name.endsWith(".jpg") || name.endsWith(".jpeg"))
                    loadBitmap(o.getPath(), im);
				else if (name.endsWith(".png"))
                    loadBitmap(o.getPath(), im);
				else if (name.endsWith(".zip"))
					im.setImageResource(R.drawable.zip32);
				else if (name.endsWith(".rtf"))
					im.setImageResource(R.drawable.rtf32);
				else if (name.endsWith(".gif"))
					im.setImageResource(R.drawable.gif32);
                else if (name.endsWith(".mp3"))
                    im.setImageResource(R.drawable.mp3);
				else
					im.setImageResource(R.drawable.whitepage32);					
			}

			if (t1 != null)
				t1.setText(o.getName());
			if (t2 != null)
				t2.setText(o.getData());				

		}
		return v;
	}

}
