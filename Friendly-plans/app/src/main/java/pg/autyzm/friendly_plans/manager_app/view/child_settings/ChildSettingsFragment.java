package pg.autyzm.friendly_plans.manager_app.view.child_settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import database.entities.Asset;
import database.entities.Child;
import database.entities.ChildPlan;
import database.entities.PlanTemplate;
import database.repository.AssetRepository;
import database.repository.ChildPlanRepository;
import database.repository.ChildRepository;
import database.repository.PlanTemplateRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.databinding.ActivityChildSettingsAlertBinding;
import pg.autyzm.friendly_plans.databinding.FragmentChildSettingsBinding;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.manager_app.view.child_list.ChildListActivity;
import pg.autyzm.friendly_plans.manager_app.view.components.SoundComponent;
import pg.autyzm.friendly_plans.manager_app.view.main_screen.MainActivity;
import pg.autyzm.friendly_plans.manager_app.view.view_fragment.CreateFragment;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class ChildSettingsFragment extends CreateFragment implements ChildSettingsActivityEvents {

    @Inject
    PlanTemplateRepository planTemplateRepository;

    @Inject
    MediaPlayer mediaPlayer;

    @Inject
    ToastUserNotifier toastUserNotifier;

    @Inject
    public FilePickerProxy filePickerProxy;
    @Inject
    protected AssetRepository assetRepository;

    @Inject
    ChildRepository childRepository;

    @Inject
    ChildPlanRepository childPlanRepository;

    private Child activeChildProfile;
    private ActivePlanRecyclerViewAdapter planListAdapter;
    protected EditText soundFileName;
    protected Long soundId;
    protected ImageButton clearSound;
    protected SoundComponent soundComponent;
    protected ImageButton playSoundIcon;
    private static final String REGEX_TRIM_NAME = "_([\\d]*)(?=\\.)";
    private ChildSettingsData childSettingsData;
    private List<Integer> selectedPlanPositions = new ArrayList<>();
    private List<Long> childPlansIds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        AppComponent appComponent = ((App) getActivity().getApplication()).getAppComponent();

        appComponent.inject(this);
        if (childRepository.getByIsActive().size() > 0) {
            FragmentChildSettingsBinding binding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_child_settings, container, false);
            View view = binding.getRoot();
            setViewComponent(view);
            soundComponent = SoundComponent.getSoundComponent(
                    soundId, playSoundIcon, getActivity().getApplicationContext(), appComponent);
            binding.setSoundComponent(soundComponent);
            setUpViews(view);

            planListAdapter.setPlanItems(planTemplateRepository.getAll());
            activeChildProfile = childRepository.getByIsActive().get(0);
            setChildPlans();

            childSettingsData = new ChildSettingsData(
                    activeChildProfile.getName(), activeChildProfile.getSurname());
            displayDataFromDb(childSettingsData);
            binding.setChildSettingsData(childSettingsData);
            binding.setChildSettingsEvents(this);
            return view;
        } else {
            ActivityChildSettingsAlertBinding binding = DataBindingUtil.inflate(
                    inflater, R.layout.activity_child_settings_alert, container, false);
            binding.setChildSettingsEvents(this);
            View view = binding.getRoot();
            return view;
        }
    }

    private void setChildPlans() {
        List<ChildPlan> childPlans = childRepository.getChildPlans(activeChildProfile.getId());
        List<PlanTemplate> planTemplates = planTemplateRepository.getAll();
        planListAdapter.setChildPlansIds(childPlans);
        for (ChildPlan plan : childPlans) {
            selectedPlanPositions.add(planTemplates.indexOf(plan.getPlanTemplate()));
        }
        planListAdapter.setSelectedPlanPosition(selectedPlanPositions);
    }

    private void setViewComponent(View view) {
        playSoundIcon = (ImageButton) view.findViewById(R.id.id_btn_settings_play_sound);
        soundFileName = (EditText) view.findViewById(R.id.id_et_timer_sound);
        clearSound = (ImageButton) view.findViewById(R.id.id_ib_settings_clear_sound_btn);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.post(new Runnable() {  // Set assets only when the layout is completely built
            @Override
            public void run() {
                if (activeChildProfile != null) {
                    Asset sound = activeChildProfile.getSound();
                    if (sound != null) {
                        setAssetValue(sound.getFilename(), sound.getId());
                    }
                }
            }
        });
    }


    protected void setAssetValue(String assetName, Long assetId) {
        String assetNameTrimed = assetName.replaceAll(REGEX_TRIM_NAME, "");
        childSettingsData.setTimerSound(assetNameTrimed);
        soundId = assetId;
        soundComponent.setSoundId(assetId);
        clearSound.setVisibility(View.VISIBLE);

    }

    private void displayDataFromDb(ChildSettingsData childSettingsData) {
        setFontSizeCheckbox(childSettingsData);
        setStepsModeCheckbox(childSettingsData);
        setTasksModeCheckbox(childSettingsData);
    }

    private void setFontSizeCheckbox(ChildSettingsData childSettingsData) {
        childSettingsData.setFontSize(activeChildProfile.getFontSize());
        switch (activeChildProfile.getFontSize()) {
            case "Big":
                childSettingsData.setBigFont(true);
                break;
            case "Medium":
                childSettingsData.setMediumFont(true);
                break;
            default:
                childSettingsData.setSmallFont(true);
                break;
        }
    }

    private void setTasksModeCheckbox(ChildSettingsData childSettingsData) {
        childSettingsData.setTasksMode(activeChildProfile.getTasksDisplayMode());
        switch (activeChildProfile.getTasksDisplayMode()) {
            case "List":
                childSettingsData.setTaskModeList(true);
                break;
            default:
                childSettingsData.setTaskModeSlide(true);
                break;
        }
    }

    private void setStepsModeCheckbox(ChildSettingsData childSettingsData) {
        childSettingsData.setStepsMode(activeChildProfile.getStepsDisplayMode());
        switch (activeChildProfile.getStepsDisplayMode()) {
            case "List":
                childSettingsData.setStepsModeList(true);
                break;
            default:
                childSettingsData.setStepsModeSlide(true);
                break;
        }
    }


    private void setUpViews(View view) {
        RecyclerView recyclerView = (RecyclerView) view
                .findViewById(R.id.rv_child_settings_plan_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ActivePlanRecyclerViewAdapter.PlanItemClickListener planItemClickListener =
                new ActivePlanRecyclerViewAdapter.PlanItemClickListener() {
                    @Override
                    public void onPlanItemClick(int position) {

                        if (selectedPlanPositions.contains(position)) {
                            selectedPlanPositions.remove(new Integer(position));
                        } else {
                            selectedPlanPositions.add(position);
                        }
                        planListAdapter.setSelectedPlanPosition(selectedPlanPositions);
                    }
                };
        planListAdapter = new ActivePlanRecyclerViewAdapter(planItemClickListener);
        recyclerView.setAdapter(planListAdapter);

    }

    @Override
    public void eventSelectActiveChild(View view) {
        Intent intent = new Intent(getActivity(), ChildListActivity.class);
        startActivity(intent);
    }

    @Override
    public void eventSaveSettings(ChildSettingsData childSettingsData) {
        soundComponent.stopActions();
        activeChildProfile.setName(childSettingsData.getFirstName());
        activeChildProfile.setSurname(childSettingsData.getLastName());
        activeChildProfile.setFontSize(childSettingsData.getFontSize());
        activeChildProfile.setStepsDisplayMode(childSettingsData.getStepsMode());
        activeChildProfile.setTasksDisplayMode(childSettingsData.getTasksMode());
        activeChildProfile.setTimerSoundId(soundId);
        childRepository.update(activeChildProfile);

        for (ChildPlan position : childRepository.getChildPlans(activeChildProfile.getId())) {
            childPlanRepository.delete(position.getId());
        }
        childRepository.reset(activeChildProfile);
        for (Integer position : selectedPlanPositions) {
            PlanTemplate plan = planListAdapter.getPlanItem(position);
            childPlanRepository.create(activeChildProfile.getId(), plan.getId());
        }
        childRepository.refresh(activeChildProfile);
        showMainMenu();
    }

    @Override
    public void eventSetFontSize(ChildSettingsData childSettingsData, String fontSize) {
        childSettingsData.setFontSize(fontSize);
    }

    @Override
    public void eventSetTasksMode(ChildSettingsData childSettingsData, String tasksMode) {
        childSettingsData.setTasksMode(tasksMode);
    }

    @Override
    public void eventSetStepsMode(ChildSettingsData childSettingsData, String stepsMode) {
        childSettingsData.setStepsMode(stepsMode);
    }

    @Override
    public void selectSound() {
        filePickerProxy.openFilePicker(this, AssetType.SOUND);
    }

    @Override
    public void clearSound() {
        soundFileName.setText("");
        soundId = null;
        clearSound.setVisibility(View.INVISIBLE);
        soundComponent.setSoundId(null);
        soundComponent.stopActions();
    }

    private void showMainMenu() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void setAssetValue(AssetType assetType, String assetName, Long assetId) {
        String assetNameTrimed = assetName.replaceAll(REGEX_TRIM_NAME, "");
        childSettingsData.setTimerSound(assetNameTrimed);
        soundId = assetId;
        soundComponent.setSoundId(assetId);
        clearSound.setVisibility(View.VISIBLE);
    }
}
