package pg.autyzm.friendly_plans;


import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class AssetsHelper {

    private static final String SEPARATOR = "_";
    private static final String EXTENSION_SEPARATOR = ".";
    private static final String JPG_EXTENSION = "jpg";
    private static final String JPEG_EXTENSION = "jpeg";

    private Context context;

    public AssetsHelper(Context context) {
        this.context = context;
    }

    public String makeSafeCopy(String pathToAsset) throws NoSuchAlgorithmException, IOException {
        String assetName = FilenameUtils.getBaseName(pathToAsset);
        String assetExtension = mapExtension(FilenameUtils.getExtension(pathToAsset));
        String safeCopyName = assetName + SEPARATOR + getRandomValue() + EXTENSION_SEPARATOR + assetExtension;
        File safeCopy = new File(context.getFilesDir(), safeCopyName);
        FileUtils.copyFile(new File(pathToAsset), safeCopy);
        return safeCopy.getAbsolutePath();
    }

    public Long saveAssetToDb(String pathToAsset) {
        return null;
    }

    private int getRandomValue() {
        Random generator = new Random();
        return generator.nextInt();
    }

    private String mapExtension(String extension) {
        String lowerCaseExtension = extension.toLowerCase();
        if(lowerCaseExtension.equals(JPEG_EXTENSION)) {
            lowerCaseExtension = JPG_EXTENSION;
        }
        return lowerCaseExtension;
    }


}
