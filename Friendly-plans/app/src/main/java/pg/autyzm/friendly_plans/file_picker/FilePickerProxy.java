package pg.autyzm.friendly_plans.file_picker;

import android.app.Fragment;
import android.content.Intent;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import java.util.regex.Pattern;
import pg.autyzm.friendly_plans.asset.AssetType;

public class FilePickerProxy {

    private static final String FILE_PATTERN = "([^\\s]+(\\.(?i)(%s))$)";

    public void openFilePicker(Fragment fragment, AssetType assetType) {
        new MaterialFilePicker()
                .withFragment(fragment)
                .withRequestCode(getRequestCode(assetType))
                .withFilter(getPattern(assetType))
                .start();
    }

    public boolean isPickFileRequested(int requestCode, AssetType assetType) {
        return requestCode == getRequestCode(assetType);
    }

    public boolean isFilePicked(int resultCode) {
        return resultCode == FilePickerActivity.RESULT_OK;
    }

    public String getFilePath(Intent data) {
        return data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
    }

    private Pattern getPattern(AssetType assetType) {
        return Pattern.compile(String.format(FILE_PATTERN, assetType.getPattern()));
    }

    private int getRequestCode(AssetType assetType) {
        return assetType.ordinal();
    }
}
