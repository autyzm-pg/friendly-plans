package pg.autyzm.friendly_plans.asset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import pg.autyzm.friendly_plans.BuildConfig;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AssetsHelperTest {

    private static final String SEPARATOR = "_";
    private static final String ASSET_NAME = "asset";
    private static final String JPG = ".jpg";
    private static final String JPG_UPPER = ".JPG";
    private static final String JPEG = ".JPEG";
    private static final String PNG = ".png";
    private static final String PNG_UPPER = ".PNG";
    private static final String TEST_FILE_CONTENT = "Test";

    private AssetsHelper assetsHelper;
    private Context context;
    private String pathToInternalStorage;
    private String jpgFileSafeCopyNamePattern;
    private String pngFileSafeCopyNamePattern;
    private File jpgFile;
    private File jpgUpperFile;
    private File jpegFile;
    private File pngFile;

    private List<File> testFiles;

    @Before
    public void setUp() throws IOException {
        testFiles = new ArrayList<>();
        context = RuntimeEnvironment.application.getApplicationContext();
        createTestFiles();

        File filesDir = context.getFilesDir();
        pathToInternalStorage = filesDir.getAbsolutePath();
        jpgFileSafeCopyNamePattern = "^" + ASSET_NAME + SEPARATOR + "(.*)" + JPG;
        pngFileSafeCopyNamePattern = "^" + ASSET_NAME + SEPARATOR + "(.*)" + PNG;

        assetsHelper = new AssetsHelper(context);
    }

    @After
    public void tearDown() {
        removeTestFiles();
    }

    @Test
    public void whenMakingSafeCopyExpectNewFilePathBeReturned()
            throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(jpgFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(safeCopyName, matchesPattern(jpgFileSafeCopyNamePattern));
    }

    @Test
    public void whenMakingSafeCopyJPGExpectNewFilePathBeReturned()
            throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(jpgUpperFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(safeCopyName, matchesPattern(jpgFileSafeCopyNamePattern));
    }

    @Test
    public void whenMakingSafeCopyJPEGExpectNewFilePathBeReturned()
            throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(jpegFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(safeCopyName, matchesPattern(jpgFileSafeCopyNamePattern));
    }

    @Test
    public void whenMakingSafeCopyPNGExpectNewFilePathBeReturned()
            throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(pngFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(safeCopyName, matchesPattern(pngFileSafeCopyNamePattern));
    }

    @Test
    public void whenMakingSafeCopyExpectFileCopyHasBeenCreated()
            throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(jpgFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(new File(pathToInternalStorage, safeCopyName).exists(), is(true));
    }

    @Test
    public void whenMakingSafeCopyJPEGExpectFileCopyHasBeenCreated()
            throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(jpegFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(new File(pathToInternalStorage, safeCopyName).exists(), is(true));
    }

    @Test
    public void whenMakingSafeCopyPNGExpectFileCopyHasBeenCreated()
            throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(pngFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(new File(pathToInternalStorage, safeCopyName).exists(), is(true));
    }

    @Test
    public void whenMakingSafeCopyExpectEachFilePathBeDifferent()
            throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(jpgFile.getAbsolutePath());
        String secondSafeCopyName = assetsHelper.makeSafeCopy(jpgFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        addToTestFiles(secondSafeCopyName);
        assertThat(safeCopyName, is(not(equalTo(secondSafeCopyName))));
    }

    private void createTestFiles() throws IOException {
        File filesDir = context.getFilesDir();
        jpgFile = new File(filesDir, ASSET_NAME + JPG);
        jpgUpperFile = new File(filesDir, ASSET_NAME + JPG_UPPER);
        jpegFile = new File(filesDir, ASSET_NAME + JPEG);
        pngFile = new File(filesDir, ASSET_NAME + PNG_UPPER);

        FileUtils.writeStringToFile(jpgFile, TEST_FILE_CONTENT);
        FileUtils.writeStringToFile(jpgUpperFile, TEST_FILE_CONTENT);
        FileUtils.writeStringToFile(jpegFile, TEST_FILE_CONTENT);
        FileUtils.writeStringToFile(pngFile, TEST_FILE_CONTENT);

        testFiles.add(jpgFile);
        testFiles.add(jpgUpperFile);
        testFiles.add(jpegFile);
        testFiles.add(pngFile);
    }

    private void addToTestFiles(String fileName) {
        testFiles.add(new File(pathToInternalStorage, fileName));
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

}
