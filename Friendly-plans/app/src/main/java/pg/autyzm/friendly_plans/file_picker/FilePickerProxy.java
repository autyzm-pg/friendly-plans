package pg.autyzm.friendly_plans.file_picker;

import android.app.Fragment;
import android.content.Intent;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import java.util.regex.Pattern;

public class FilePickerProxy {

    public static final int PICK_FILE_REQUEST = 1;
    private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$)";

    public void openImageFilePicker(Fragment fragment) {
        new MaterialFilePicker()
                .withFragment(fragment)
                .withRequestCode(PICK_FILE_REQUEST)
                .withFilter(Pattern.compile(IMAGE_PATTERN))
                .start();
    }

    public boolean isPickFileRequested(int requestCode) {
        return requestCode == PICK_FILE_REQUEST;
    }

    public boolean isFilePicked(int resultCode) {
        return resultCode == FilePickerActivity.RESULT_OK;
    }

    public String getFilePath(Intent data) {
        return data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
    }

}
