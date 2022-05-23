package pg.autyzm.friendly_plans.asset;

import android.content.Context;
import android.net.Uri;

import database.entities.Asset;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    public String makeSafeCopy(Uri fileUri, String fileName) throws IOException {
        String assetName = FilenameUtils.getBaseName(fileName);
        String assetExtension = mapExtension(FilenameUtils.getExtension(fileName));
        String safeCopyName =
                assetName + SEPARATOR + getRandomValue() + EXTENSION_SEPARATOR + assetExtension;
        InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
        int size = inputStream.available();
        byte[] bytes = new byte[size];
        inputStream.read(bytes);

        OutputStream output = new FileOutputStream(context.getFilesDir().toString()+'/'+ safeCopyName);
        output.write(bytes);
        output.flush();

        inputStream.close();

//        File safeCopy = new File(context.getFilesDir(), safeCopyName);
//        String a = fileUri.getPath();
//        FileUtils.copyFile(new File(fileUri.getPath()), safeCopy);
        return safeCopyName;
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
