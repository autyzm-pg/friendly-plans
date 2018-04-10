package pg.autyzm.friendly_plans.file_picker;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import android.content.Intent;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.internal.WhiteboxImpl;
import pg.autyzm.friendly_plans.asset.AssetType;

@RunWith(PowerMockRunner.class)
public class FilePickerProxyTest {

    private static final String FILE_PATH = "test_result_file_path";
    private FilePickerProxy filePickerProxy;

    @Mock
    private Intent intentMock;

    @Before
    public void setUp() {
        filePickerProxy = new FilePickerProxy();
        when(intentMock.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)).thenReturn(FILE_PATH);
    }

    @Test
    public void whenGettingPatternForPictureExpectPatternToMatchLowercasepng() throws Exception {
        Pattern pattern = getPattern(AssetType.PICTURE);

        assertTrue(pattern.matcher("test.png").matches());
    }

    @Test
    public void whenGettingPatternForPictureExpectPatternToMatchUppercasePNG() throws Exception {
        Pattern pattern = getPattern(AssetType.PICTURE);

        assertTrue(pattern.matcher("test.PNG").matches());
    }

    @Test
    public void whenGettingPatternForSoundExpectPatternToMatchMp3() throws Exception {
        Pattern pattern = getPattern(AssetType.SOUND);

        assertTrue(pattern.matcher("test.mp3").matches());
    }

    @Test
    public void whenGettingPatternForSoundExpectPatternToNotMatchOnlyExtension()
            throws Exception {
        Pattern pattern = getPattern(AssetType.SOUND);

        assertFalse(pattern.matcher(".mp3").matches());
    }

    @Test
    public void whenCheckingPickFileRequestedExpectCorrectResult() {
        assertTrue(filePickerProxy.isPickFileRequested(0, AssetType.PICTURE));
        assertTrue(filePickerProxy.isPickFileRequested(1, AssetType.SOUND));
    }

    @Test
    public void whenCheckingFilePickedWithOkExpectTrue() {
        assertTrue(filePickerProxy.isFilePicked(FilePickerActivity.RESULT_OK));
    }

    @Test
    public void whenCheckingFilePickedWithCanceledExpectFalse() {
        assertFalse(filePickerProxy.isFilePicked(FilePickerActivity.RESULT_CANCELED));
    }

    @Test
    public void whenGettingFilePathExpectCorrectResult() {
        assertThat(filePickerProxy.getFilePath(intentMock), is(FILE_PATH));
    }

    private Pattern getPattern(AssetType assetType) throws Exception {
        return WhiteboxImpl.invokeMethod(filePickerProxy, "getPattern", assetType);
    }
}
