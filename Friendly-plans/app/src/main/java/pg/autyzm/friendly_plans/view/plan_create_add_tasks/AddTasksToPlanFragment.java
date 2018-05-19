package pg.autyzm.friendly_plans.view.plan_create_add_tasks;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.FragmentAddTasksToPlanBinding;

public class AddTasksToPlanFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getAppComponent().inject(this);

        FragmentAddTasksToPlanBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_tasks_to_plan, container, false);

        View view = binding.getRoot();
        return view;
    }
}
