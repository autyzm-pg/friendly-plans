package pg.autyzm.friendly_plans;

import android.app.Fragment;
import android.content.Intent;
import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import database.repository.StepTemplateRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.databinding.FragmentStepListBinding;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class StepListFragment extends Fragment implements StepListEvents {

    public static final String TASK_ID = "task_id";

    @Inject
    StepTemplateRepository stepTemplateRepository;

    StepListRecyclerViewAdapter.StepItemClickListener stepItemClickListener =
            new StepListRecyclerViewAdapter.StepItemClickListener() {
                @Override
                public void onRemoveStepClick(long itemId) {
                    stepTemplateRepository.delete(itemId);
                    stepListRecyclerViewAdapter
                            .setStepItemListItems(stepTemplateRepository.getAll(task_id));
                }
            };

    private StepListRecyclerViewAdapter stepListRecyclerViewAdapter;
    private long task_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getAppComponent().inject(this);

        Bundle args = getArguments();
        task_id = args.getLong(TASK_ID, 0);

        FragmentStepListBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_list, container, false);

        View view = binding.getRoot();
        binding.setStepListEvents(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setUpListContent(task_id);
    }

    private void setUpListContent(long taskId) {
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_step_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        stepListRecyclerViewAdapter = new StepListRecyclerViewAdapter(stepItemClickListener);
        recyclerView.setAdapter(stepListRecyclerViewAdapter);

        stepListRecyclerViewAdapter.setStepItemListItems(stepTemplateRepository.getAll(taskId));
    }

    @Override
    public void eventCreateStep(View view) {
        showStepCreate();
    }

    private void showStepCreate() {
        StepCreateFragment fragment = new StepCreateFragment();

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.task_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void eventSaveAndFinish(View view) {
        ToastUserNotifier.displayNotifications(
            R.string.task_with_steps_saved_message,
            getActivity().getApplicationContext());
        showMainMenu();
    }

    private void showMainMenu() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}
