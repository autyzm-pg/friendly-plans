package pg.autyzm.friendly_plans.resource;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

public class ChildViewClicker implements ViewAction {
    private final int id;

    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void perform(UiController uiController, View view) {
        View v = view.findViewById(id);
        v.performClick();
    }

    public ChildViewClicker(int id){
        this.id = id;
    }
}
