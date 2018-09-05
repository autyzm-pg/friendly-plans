package pg.autyzm.friendly_plans.manager_app.view.step_list;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import database.entities.StepTemplate;
import database.repository.StepTemplateRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.FragmentStepListBinding;
import pg.autyzm.friendly_plans.item_touch_helper.SimpleItemTouchHelperCallback;
import pg.autyzm.friendly_plans.notifications.DialogUserNotifier;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.manager_app.view.main_screen.MainActivity;
import pg.autyzm.friendly_plans.manager_app.view.step_create.StepCreateFragment;

public class StepListFragment extends Fragment implements StepListEvents {

    @Inject
    StepTemplateRepository stepTemplateRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;

    StepListRecyclerViewAdapter.StepItemClickListener stepItemClickListener =
            new StepListRecyclerViewAdapter.StepItemClickListener() {

                public boolean removedStep = false;
                @Override
                public void onRemoveStepClick(final long itemId) {
                    DialogUserNotifier dialog = new DialogUserNotifier(
                            getActivity(),
                            getResources().getString(R.string.step_removal_confirmation_title),
                            getResources().getString(R.string.step_removal_confirmation_message)
                    );
                    dialog.setPositiveButton(
                            getResources().getString(R.string.step_removal_confirmation_positive_button),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    removeStep(itemId);
                                    removedStep = true;
                                    dialog.dismiss();
                                }
                            });
                    dialog.setNegativeButton(
                            getResources().getString(R.string.step_removal_confirmation_negative_button),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.showDialog();
                }

                @Override
                public void onMoveItem() {
                    Boolean reordered = false;
                    for(int i = 0; i < stepListRecyclerViewAdapter.getItemCount(); i++){
                        StepTemplate stepItem =  stepListRecyclerViewAdapter.getStepTemplate(i);
                        if(i != stepItem.getOrder()) {
                            stepTemplateRepository.update(stepItem.getId(), stepItem.getName(), i, stepItem.getPictureId(), stepItem.getSoundId(), stepItem.getTaskTemplateId());
                            reordered = true;
                        }
                        if(!removedStep && reordered){
                            toastUserNotifier.displayNotifications(
                                    R.string.steps_reordered_message,
                                    getActivity().getApplicationContext());
                        }
                    }
                }

                @Override
                public void onShowStepClick(long itemId) {
                    StepCreateFragment fragment = new StepCreateFragment();
                    Bundle args = new Bundle();
                    args.putLong(ActivityProperties.TASK_ID, task_id);
                    args.putLong(ActivityProperties.STEP_ID, itemId);
                    fragment.setArguments(args);
                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.task_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            };

    private StepListRecyclerViewAdapter stepListRecyclerViewAdapter;

    private long task_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getAppComponent().inject(this);

        Bundle args = getArguments();
        task_id = args.getLong(ActivityProperties.TASK_ID, 0);

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

    @Override
    public void eventCreateStep(View view) {
        showStepCreate();
    }

    private void setUpListContent(long taskId) {
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_step_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        stepListRecyclerViewAdapter = new StepListRecyclerViewAdapter(stepItemClickListener);
        recyclerView.setAdapter(stepListRecyclerViewAdapter);

        stepListRecyclerViewAdapter.setStepItemListItems(stepTemplateRepository.getAll(taskId));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(stepListRecyclerViewAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showStepCreate() {
        StepCreateFragment fragment = new StepCreateFragment();
        Bundle args = new Bundle();
        args.putLong(ActivityProperties.TASK_ID, task_id);
        fragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.task_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void eventSaveAndFinish(View view) {
        toastUserNotifier.displayNotifications(
                R.string.task_with_steps_saved_message,
                getActivity().getApplicationContext());
        getActivity().getFragmentManager().popBackStack();
    }

    private void removeStep(long stepId){
        stepTemplateRepository.delete(stepId);
        stepListRecyclerViewAdapter
                .setStepItemListItems(stepTemplateRepository.getAll(task_id));
        toastUserNotifier.displayNotifications(
                R.string.step_removed_message,
                getActivity().getApplicationContext());
    }
}
