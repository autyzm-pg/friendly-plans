package pg.autyzm.friendly_plans.asset;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;

public enum AssetType {
    PICTURE ("PICTURE"),
    SOUND ("SOUND");

    private static final String IMAGE_PATTERN = "^(jpg|jpeg|png|gif|bmp)$";
    private static final String SOUND_PATTERN = "^(3gp|mp3|flac|wav|ogg|mkv)$";

    private final String typeName;

    AssetType(String s) {
        typeName = s;
    }

    public String toString() {
        return this.typeName;
    }

    public boolean equalsName(String typeName) {
        return this.typeName.equals(typeName);
    }

    public static AssetType getTypeByExtension(String pathToAsset) {
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

    public static AssetType getTypeByTypeName(String typeName) {
        if (AssetType.PICTURE.equalsName(typeName)) {
            return AssetType.PICTURE;
        } else if (AssetType.SOUND.equalsName(typeName)) {
            return AssetType.SOUND;
        } else {
            throw new IllegalArgumentException();
        }
    }

}
