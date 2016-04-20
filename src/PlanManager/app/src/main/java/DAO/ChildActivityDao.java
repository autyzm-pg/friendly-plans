package DAO;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import model.ChildActivity;

/**
 * Created by Chris on 7/25/2014.
 */
public class ChildActivityDao implements IDao {

    private ContentResolver cr;
    public ChildActivityDao(Context context)
    {
        cr = context.getContentResolver();
    }

    public List<ChildActivity> getAllActivities(){
        List<ChildActivity> activities = new ArrayList<ChildActivity>();
        Cursor cursor = cr.query(Uri.parse(CONTENT_URI + ALL_ACTIVITIES), null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ChildActivity childActivity = cursorToActivity(cursor);
            activities.add(childActivity);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return activities;
    }

    private ChildActivity cursorToActivity(Cursor cursor) {
        ChildActivity childActivity = new ChildActivity();
        childActivity.setId(cursor.getLong(0));
        childActivity.setTitle(cursor.getString(1));

        childActivity.setSlides(new SlidesDao(cr).getAllSlides( childActivity.getId()));
        return childActivity;
    }
}
