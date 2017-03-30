package database.entities;


import java.util.Arrays;
import java.util.List;

public class AssetType {

    public static final String PICTURE = "PICTURE";
    public static final String SOUND = "SOUND";

    public static final List<String> VALID_TYPES = Arrays.asList(AssetType.PICTURE, AssetType.SOUND);

}
