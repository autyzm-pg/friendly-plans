package DAO;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Slide;

/**
 * Created by Chris on 7/25/2014.
 */
public class SlidesDao implements IDao {
    private ContentResolver cr;
    public SlidesDao(Context context){
        cr = context.getContentResolver();
    }
    public SlidesDao(ContentResolver cr){
        this.cr = cr;
    }

    public List<Slide> getAllSlides(long id) {
        List<Slide> slides = new ArrayList<Slide>();
        Cursor cursor = null;
        try {
            cursor = cr.query(Uri.parse(CONTENT_URI + ACTIVITY_SLIDES+"/"+id),
                    null, null, null, null);
        }catch(Exception e){
            Log.i("SQLEX", e.getMessage());
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Slide slide = cursorToSlide(cursor);
            slides.add(slide);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return slides;
    }

    private Slide cursorToSlide(Cursor cursor) {
        Slide slide = new Slide();
        slide.setId(cursor.getLong(0));
        slide.setText(cursor.getString(2));
        slide.setAudioPath(cursor.getString(3));
        slide.setImagePath(cursor.getString(4));
        return slide;
    }
}
