package pg.autyzm.friendly_plans.asset;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;

public enum AssetType {
    PICTURE("PICTURE"),
    SOUND("SOUND");

    private static final String PICTURE_EXTENSIONS = "jpg|jpeg|png|gif|bmp";
    private static final String SOUND_EXTENSIONS = "3gp|mp3|flac|wav|ogg|mkv";

    private static final Pattern PICTURE_PATTERN = Pattern.compile(PICTURE_EXTENSIONS);
    private static final Pattern SOUND_PATTERN = Pattern.compile(SOUND_EXTENSIONS);

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
        Matcher pictureMatcher = PICTURE_PATTERN.matcher(extension);
        Matcher soundMatcher = SOUND_PATTERN.matcher(extension);
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

    public String getPattern() {
        if (this.equals(PICTURE)) {
            return PICTURE_EXTENSIONS;
        } else {
            return SOUND_EXTENSIONS;
        }
    }
}
