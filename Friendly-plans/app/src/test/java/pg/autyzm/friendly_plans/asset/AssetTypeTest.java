package pg.autyzm.friendly_plans.asset;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssetTypeTest {

    private static final String PICTURE_NAME = "picture.jpg";
    private static final String SOUND_NAME = "sound.mp3";
    private static final String TEXT_FILE_NAME = "wrong.txt";

    private static final String PICTURE_TYPE_NAME = "PICTURE";
    private static final String SOUND_TYPE_NAME = "SOUND";
    private static final String WRONG_TYPE_NAME = "WRONG";

    @Test
    public void whenGettingTypeByPictureExtensionExpectPictureTypeBeReturned() {
        AssetType assetType = AssetType.getTypeByExtension(PICTURE_NAME);
        assertThat(assetType, is(equalTo(AssetType.PICTURE)));
    }

    @Test
    public void whenGettingTypeBySoundExtensionExpectSoundTypeBeReturned() {
        AssetType assetType = AssetType.getTypeByExtension(SOUND_NAME);
        assertThat(assetType, is(equalTo(AssetType.SOUND)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenGettingTypeByWrongExtensionExpectIllegalArgumentExceptionBeThrown() {
        AssetType.getTypeByExtension(TEXT_FILE_NAME);
    }

    @Test
    public void whenGettingTypeByPictureTypeNameExpectPictureTypeBeReturned() {
        AssetType assetType = AssetType.getTypeByTypeName(PICTURE_TYPE_NAME);
        assertThat(assetType, is(equalTo(AssetType.PICTURE)));
    }

    @Test
    public void whenGettingTypeBySoundTypeNameExpectSoundTypeBeReturned() {
        AssetType assetType = AssetType.getTypeByTypeName(SOUND_TYPE_NAME);
        assertThat(assetType, is(equalTo(AssetType.SOUND)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenGettingTypeByWrongTypeNameExpectIllegalArgumentExceptionBeThrown() {
        AssetType.getTypeByTypeName(WRONG_TYPE_NAME);
    }

    @Test
    public void whenGettingPicturePatternExpectCorrectPatternToBeReturned() {
        String pattern = AssetType.PICTURE.getPattern();
        assertThat(pattern, containsString("jpg"));
        assertThat(pattern, containsString("png"));
        assertThat(pattern, containsString("gif"));
        assertThat(pattern, containsString("bmp"));
    }

    @Test
    public void whenGettingSoundPatternExpectCorrectPatternToBeReturned() {
        String pattern = AssetType.SOUND.getPattern();
        assertThat(pattern, containsString("mp3"));
        assertThat(pattern, containsString("wav"));
    }
}
