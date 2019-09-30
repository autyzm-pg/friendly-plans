package pg.autyzm.friendly_plans.asset;

import android.content.Context;
import database.entities.Asset;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class AssetsHelper {

    private static final String SEPARATOR = "_";
    private static final String EXTENSION_SEPARATOR = ".";
    private static final String JPG_EXTENSION = "jpg";
    private static final String JPEG_EXTENSION = "jpeg";

    private Context context;
    private Random generator;

    public AssetsHelper(Context context) {
        this.context = context;
        generator = new Random();
    }

    public String makeSafeCopy(String pathToAsset) throws IOException {
        String assetName = FilenameUtils.getBaseName(pathToAsset);
        String assetExtension = mapExtension(FilenameUtils.getExtension(pathToAsset));
        String safeCopyName =
                assetName + SEPARATOR + getRandomValue() + EXTENSION_SEPARATOR + assetExtension;
        File safeCopy = new File(context.getFilesDir(), safeCopyName);
        FileUtils.copyFile(new File(pathToAsset), safeCopy);
        return safeCopy.getName();
    }

    private int getRandomValue() {
        return generator.nextInt(Integer.MAX_VALUE - 1);
    }

    private String mapExtension(String extension) {
        String lowerCaseExtension = extension.toLowerCase();
        if (lowerCaseExtension.equals(JPEG_EXTENSION)) {
            lowerCaseExtension = JPG_EXTENSION;
        }
        return lowerCaseExtension;
    }

    public String getFileFullPath(Asset asset) {
        return context.getFilesDir().toString() + File.separator + asset.getFilename();
    }
}
