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
    public void When_GettingTypeByPictureExtension_Expect_PictureTypeBeReturned() {
        AssetType assetType = AssetType.getTypeByExtension(PICTURE_NAME);
        assertThat(assetType, is(equalTo(AssetType.PICTURE)));
    }

    @Test
    public void When_GettingTypeBySoundExtension_Expect_SoundTypeBeReturned() {
        AssetType assetType = AssetType.getTypeByExtension(SOUND_NAME);
        assertThat(assetType, is(equalTo(AssetType.SOUND)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void When_GettingTypeByWrongExtension_Expect_IllegalArgumentExceptionBeThrown() {
        AssetType.getTypeByExtension(TEXT_FILE_NAME);
    }

    @Test
    public void When_GettingTypeByPictureTypeName_Expect_PictureTypeBeReturned() {
        AssetType assetType = AssetType.getTypeByTypeName(PICTURE_TYPE_NAME);
        assertThat(assetType, is(equalTo(AssetType.PICTURE)));
    }

    @Test
    public void When_GettingTypeBySoundTypeName_Expect_SoundTypeBeReturned() {
        AssetType assetType = AssetType.getTypeByTypeName(SOUND_TYPE_NAME);
        assertThat(assetType, is(equalTo(AssetType.SOUND)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void When_GettingTypeByWrongTypeName_Expect_IllegalArgumentExceptionBeThrown() {
        AssetType.getTypeByTypeName(WRONG_TYPE_NAME);
    }

    @Test
    public void When_GettingPicturePattern_Expect_CorrectPatternToBeReturned() {
        String pattern = AssetType.PICTURE.getPattern();
        assertThat(pattern, containsString("jpg"));
        assertThat(pattern, containsString("png"));
        assertThat(pattern, containsString("gif"));
        assertThat(pattern, containsString("bmp"));
    }

    @Test
    public void When_GettingSoundPattern_Expect_CorrectPatternToBeReturned() {
        String pattern = AssetType.SOUND.getPattern();
        assertThat(pattern, containsString("mp3"));
        assertThat(pattern, containsString("wav"));
    }
}
