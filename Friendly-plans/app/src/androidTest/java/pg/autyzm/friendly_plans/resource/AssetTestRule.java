package pg.autyzm.friendly_plans.resource;


import android.content.Context;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import database.entities.Asset;
import database.repository.AssetRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.rules.ExternalResource;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateFragment;

public class AssetTestRule extends ExternalResource {

    private static final String TEST_PICTURE_NAME = "picture.jpg";
    private static final String TEST_SOUND_NAME = "sound.mp3";
    private static final String TEST_FILE_CONTENT = "Test";
    private static final long CHOOSE_FILE_TIMEOUT = 1000;

    private final DaoSessionResource daoSessionResource;
    private final ActivityTestRule activityRule;

    private final List<File> testFiles;
    private AssetRepository assetRepository;
    private File internalStorage;

    private boolean isTestAssetSet;

    public AssetTestRule(DaoSessionResource daoSessionResource, ActivityTestRule activityRule) {
        this.daoSessionResource = daoSessionResource;
        this.activityRule = activityRule;
        testFiles = new ArrayList<>();
        isTestAssetSet = false;
    }

    @Override
    protected void after() {
        if (isTestAssetSet) {
            removeTestFiles();
            removeTestAssets();
            isTestAssetSet = false;
        }
    }

    public void setTestPicture() throws IOException, InterruptedException {
        setTestAsset(TEST_PICTURE_NAME, AssetType.PICTURE);
    }

    public void setTestSound() throws IOException, InterruptedException {
        setTestAsset(TEST_SOUND_NAME, AssetType.SOUND);
    }

    private void setTestAsset(String testPictureName, AssetType picture)
            throws IOException, InterruptedException {
        initContextDependentVariables();
        File testPicture = createTestAsset(testPictureName);
        chooseTestAsset(testPicture, picture);
        addSafeCopyToTestFiles();
        isTestAssetSet = true;
    }

    private void initContextDependentVariables() {
        Context context = activityRule.getActivity().getApplicationContext();
        assetRepository = new AssetRepository(daoSessionResource.getSession(context));
        internalStorage = context.getFilesDir();
    }

    private File createTestAsset(String assetName) throws IOException {
        File testPicture = new File(internalStorage, assetName);
        FileUtils.writeStringToFile(testPicture, TEST_FILE_CONTENT);
        testFiles.add(testPicture);
        return testPicture;
    }

    private void chooseTestAsset(File testAsset, final AssetType assetType)
            throws InterruptedException {
        final Intent data = new Intent();
        data.putExtra(FilePickerActivity.RESULT_FILE_PATH, testAsset.getAbsolutePath());
        final TaskCreateFragment fragment = getFragment();
        activityRule.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                fragment.onActivityResult(assetType.ordinal(),
                        FilePickerActivity.RESULT_OK, data);
            }
        });
        Thread.sleep(CHOOSE_FILE_TIMEOUT);
    }

    private void addSafeCopyToTestFiles() {
        List<Asset> assets = assetRepository.getAll();
        String fileName = assets.get(0).getFilename();
        testFiles.add(new File(internalStorage, fileName));
    }

    private TaskCreateFragment getFragment() {
        return (TaskCreateFragment) activityRule.getActivity().getFragmentManager()
                .findFragmentById(R.id.task_container);
    }

    private void removeTestFiles() {
        for (File testFile : testFiles) {
            try {
                if (testFile.exists()) {
                    FileUtils.forceDelete(testFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeTestAssets() {
        for (Asset asset : assetRepository.getAll()) {
            assetRepository.delete(asset.getId());
        }
    }
}
