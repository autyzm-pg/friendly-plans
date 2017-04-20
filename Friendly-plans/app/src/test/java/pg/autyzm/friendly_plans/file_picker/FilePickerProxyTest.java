package pg.autyzm.friendly_plans.file_picker;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.internal.WhiteboxImpl;
import pg.autyzm.friendly_plans.asset.AssetType;

@RunWith(PowerMockRunner.class)
public class FilePickerProxyTest {

    private FilePickerProxy filePickerProxy;

    @Before
    public void setUp() {
        filePickerProxy = new FilePickerProxy();
    }

    @Test
    public void When_GettingPatternForPicture_Expect_PatternToMatchLowercasepng() throws Exception {
        Pattern pattern = WhiteboxImpl
                .invokeMethod(filePickerProxy, "getPattern", AssetType.PICTURE);

        assertTrue(pattern.matcher("test.png").matches());
    }

    @Test
    public void When_GettingPatternForPicture_Expect_PatternToMatchUppercasePNG() throws Exception {
        Pattern pattern = WhiteboxImpl
                .invokeMethod(filePickerProxy, "getPattern", AssetType.PICTURE);

        assertTrue(pattern.matcher("test.PNG").matches());
    }

    @Test
    public void When_GettingPatternForSound_Expect_PatternToMatchMp3() throws Exception {
        Pattern pattern = WhiteboxImpl.invokeMethod(filePickerProxy, "getPattern", AssetType.SOUND);

        assertTrue(pattern.matcher("test.mp3").matches());
    }

    @Test
    public void When_GettingPatternForSound_Expect_PatternToNotMatchOnlyExtension()
            throws Exception {
        Pattern pattern = WhiteboxImpl.invokeMethod(filePickerProxy, "getPattern", AssetType.SOUND);

        assertFalse(pattern.matcher(".mp3").matches());
    }
}
