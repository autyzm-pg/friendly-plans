package pg.autyzm.friendly_plans;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AssetsHelperTest {

    private static final String SEPARATOR = "_";
    private static final String ASSET_NAME = "asset";
    private static final String JPG = ".jpg";
    private static final String JPG_UPPER = ".JPG";
    private static final String JPEG = ".JPEG";
    private static final String PNG = ".png";
    private static final String PNG_UPPER = ".PNG";

    private Context context;
    private String jpgFileSafeCopyNamePattern;
    private String pngFileSafeCopyNamePattern;
    private File jpgFile;
    private File jpgUpperFile;
    private File jpegFile;
    private File pngFile;
    private String pathToInternalStorage;

    private List<File> testFiles;

    private AssetsHelper assetsHelper;

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
    public void When_MakingSafeCopy_Expect_NewFilePathBeReturned()
        throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(jpgFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(safeCopyName, matchesPattern(jpgFileSafeCopyNamePattern));
    }

    @Test
    public void When_MakingSafeCopyJPG_Expect_NewFilePathBeReturned()
        throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(jpgUpperFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(safeCopyName, matchesPattern(jpgFileSafeCopyNamePattern));
    }

    @Test
    public void When_MakingSafeCopyJPEG_Expect_NewFilePathBeReturned()
        throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(jpegFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(safeCopyName, matchesPattern(jpgFileSafeCopyNamePattern));
    }

    @Test
    public void When_MakingSafeCopyPNG_Expect_NewFilePathBeReturned()
        throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(pngFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(safeCopyName, matchesPattern(pngFileSafeCopyNamePattern));
    }

    @Test
    public void When_MakingSafeCopy_Expect_FileCopyHasBeenCreated()
        throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(jpgFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(new File(pathToInternalStorage, safeCopyName).exists(), is(true));
    }

    @Test
    public void When_MakingSafeCopyJPEG_Expect_FileCopyHasBeenCreated()
        throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(jpegFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(new File(pathToInternalStorage, safeCopyName).exists(), is(true));
    }

    @Test
    public void When_MakingSafeCopyPNG_Expect_FileCopyHasBeenCreated()
        throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(pngFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        assertThat(new File(pathToInternalStorage, safeCopyName).exists(), is(true));
    }

    @Test
    public void When_MakingSafeCopy_Expect_EachFilePathBeDifferent()
        throws IOException {
        String safeCopyName = assetsHelper.makeSafeCopy(jpgFile.getAbsolutePath());
        String secondSafeCopyName = assetsHelper.makeSafeCopy(jpgFile.getAbsolutePath());
        addToTestFiles(safeCopyName);
        addToTestFiles(secondSafeCopyName);
        assertThat(safeCopyName, is(not(equalTo(secondSafeCopyName))));
    }

    private void createTestFiles() throws IOException {
        File filesDir = context.getFilesDir();
        jpgFile = new File(filesDir, ASSET_NAME+JPG);
        jpgUpperFile = new File(filesDir, ASSET_NAME+JPG_UPPER);
        jpegFile = new File(filesDir, ASSET_NAME+JPEG);
        pngFile = new File(filesDir, ASSET_NAME+ PNG_UPPER);
        FileUtils.writeStringToFile(jpgFile, "Test");
        FileUtils.writeStringToFile(jpgUpperFile, "Test");
        FileUtils.writeStringToFile(jpegFile, "Test");
        FileUtils.writeStringToFile(pngFile, "Test");
        testFiles.add(jpgFile);
        testFiles.add(jpgUpperFile);
        testFiles.add(jpegFile);
        testFiles.add(pngFile);
    }

    private void addToTestFiles(String fileName) {
        testFiles.add(new File(pathToInternalStorage, fileName));
    }

    private void removeTestFiles() {
        for(File testFile : testFiles) {
            try {
                if(testFile.exists()) {
                    FileUtils.forceDelete(testFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
