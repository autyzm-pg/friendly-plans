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
    private String jpgFileSafeCopyPathPattern;
    private String pngFileSafeCopyPathPattern;
    private File jpgFile;
    private File jpgUpperFile;
    private File jpegFile;
    private File pngFile;

    private List<File> testFiles;

    private AssetsHelper assetsHelper;

    @Before
    public void setUp() throws IOException {
        testFiles = new ArrayList<>();
        context = RuntimeEnvironment.application.getApplicationContext();
        createTestFiles();

        File filesDir = context.getFilesDir();
        String pathToInternalStorage = filesDir.getAbsolutePath();
        jpgFileSafeCopyPathPattern = "^" + pathToInternalStorage + "/" + ASSET_NAME + SEPARATOR + "(.*)" + JPG;
        pngFileSafeCopyPathPattern = "^" + pathToInternalStorage + "/" + ASSET_NAME + SEPARATOR + "(.*)" + PNG;

        assetsHelper = new AssetsHelper(context);
    }

    @After
    public void tearDown() {
        removeTestFiles();
    }

    @Test
    public void When_MakingSafeCopy_Expect_NewFilePathBeReturned()
        throws NoSuchAlgorithmException, IOException {
        String pathToSafeCopy = assetsHelper.makeSafeCopy(jpgFile.getAbsolutePath());
        addToTestFiles(pathToSafeCopy);
        assertThat(pathToSafeCopy, matchesPattern(jpgFileSafeCopyPathPattern));
    }

    @Test
    public void When_MakingSafeCopyJPG_Expect_NewFilePathBeReturned()
        throws NoSuchAlgorithmException, IOException {
        String pathToSafeCopy = assetsHelper.makeSafeCopy(jpgUpperFile.getAbsolutePath());
        addToTestFiles(pathToSafeCopy);
        assertThat(pathToSafeCopy, matchesPattern(jpgFileSafeCopyPathPattern));
    }

    @Test
    public void When_MakingSafeCopyJPEG_Expect_NewFilePathBeReturned()
        throws NoSuchAlgorithmException, IOException {
        String pathToSafeCopy = assetsHelper.makeSafeCopy(jpegFile.getAbsolutePath());
        addToTestFiles(pathToSafeCopy);
        assertThat(pathToSafeCopy, matchesPattern(jpgFileSafeCopyPathPattern));
    }

    @Test
    public void When_MakingSafeCopyPNG_Expect_NewFilePathBeReturned()
        throws NoSuchAlgorithmException, IOException {
        String pathToSafeCopy = assetsHelper.makeSafeCopy(pngFile.getAbsolutePath());
        addToTestFiles(pathToSafeCopy);
        assertThat(pathToSafeCopy, matchesPattern(pngFileSafeCopyPathPattern));
    }

    @Test
    public void When_MakingSafeCopy_Expect_FileCopyHasBeenCreated()
        throws NoSuchAlgorithmException, IOException {
        String pathToSafeCopy = assetsHelper.makeSafeCopy(jpgFile.getAbsolutePath());
        addToTestFiles(pathToSafeCopy);
        assertThat(new File(pathToSafeCopy).exists(), is(true));
    }

    @Test
    public void When_MakingSafeCopyJPEG_Expect_FileCopyHasBeenCreated()
        throws NoSuchAlgorithmException, IOException {
        String pathToSafeCopy = assetsHelper.makeSafeCopy(jpegFile.getAbsolutePath());
        addToTestFiles(pathToSafeCopy);
        assertThat(new File(pathToSafeCopy).exists(), is(true));
    }

    @Test
    public void When_MakingSafeCopyPNG_Expect_FileCopyHasBeenCreated()
        throws NoSuchAlgorithmException, IOException {
        String pathToSafeCopy = assetsHelper.makeSafeCopy(pngFile.getAbsolutePath());
        addToTestFiles(pathToSafeCopy);
        assertThat(new File(pathToSafeCopy).exists(), is(true));
    }

    @Test
    public void WhenMakingSafeCopy_Expect_EachFilePathBeDifferent()
        throws IOException, NoSuchAlgorithmException {
        String pathToSafeCopy = assetsHelper.makeSafeCopy(jpgFile.getAbsolutePath());
        String secondPathToSafeCopy = assetsHelper.makeSafeCopy(jpgFile.getAbsolutePath());
        addToTestFiles(pathToSafeCopy);
        addToTestFiles(secondPathToSafeCopy);
        assertThat(pathToSafeCopy, is(not(equalTo(secondPathToSafeCopy))));
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

    private void addToTestFiles(String filePath) {
        testFiles.add(new File(filePath));
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
