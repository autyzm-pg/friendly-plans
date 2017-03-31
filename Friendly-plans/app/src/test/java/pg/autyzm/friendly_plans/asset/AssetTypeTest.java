package pg.autyzm.friendly_plans.asset;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import pg.autyzm.friendly_plans.asset.AssetType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssetTypeTest {

    private static final String PICTURE_NAME = "picture.jpg";
    private static final String SOUND_NAME = "sound.mp3";
    private static final String TEXT_FILE_NAME = "wrong.txt";

    @Test
    public void When_GettingTypeByPictureExtension_Expect_PictureTypeBeReturned() {
        String assetType = AssetType.getTypeByExtension(PICTURE_NAME);
        assertThat(assetType, is(equalTo(AssetType.PICTURE)));
    }

    @Test
    public void When_GettingTypeBySoundExtension_Expect_SoundTypeBeReturned() {
        String assetType = AssetType.getTypeByExtension(SOUND_NAME);
        assertThat(assetType, is(equalTo(AssetType.SOUND)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void When_GettingTypeByWrongExtension_Expect_IllegalArgumentExceptionBeThrown() {
        AssetType.getTypeByExtension(TEXT_FILE_NAME);
    }

}
