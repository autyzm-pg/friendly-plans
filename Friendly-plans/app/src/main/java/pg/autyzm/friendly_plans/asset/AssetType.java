package pg.autyzm.friendly_plans.asset;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;

public class AssetType {

    public static final String PICTURE = "PICTURE";
    public static final String SOUND = "SOUND";

    public static final List<String> VALID_TYPES = Arrays
            .asList(AssetType.PICTURE, AssetType.SOUND);

    private static final String IMAGE_PATTERN = "^(jpg|jpeg|png|gif|bmp)$";
    private static final String SOUND_PATTERN = "^(3gp|mp3|flac|wav|ogg|mkv)$";

    public static String getTypeByExtension(String pathToAsset) {
        String extension = FilenameUtils.getExtension(pathToAsset);
        Pattern picturePattern = Pattern.compile(IMAGE_PATTERN);
        Matcher pictureMatcher = picturePattern.matcher(extension);
        Pattern soundPattern = Pattern.compile(SOUND_PATTERN);
        Matcher soundMatcher = soundPattern.matcher(extension);
        if (pictureMatcher.matches()) {
            return AssetType.PICTURE;
        } else if (soundMatcher.matches()) {
            return AssetType.SOUND;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private AssetType() {

    }

}
