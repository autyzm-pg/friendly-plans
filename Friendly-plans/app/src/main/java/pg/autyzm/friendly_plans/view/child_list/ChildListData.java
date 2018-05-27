package pg.autyzm.friendly_plans.view.child_list;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import pg.autyzm.friendly_plans.BR;


public class ChildListData extends BaseObservable {

    private String firstName;
    private String lastName;

    public ChildListData(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }
}
