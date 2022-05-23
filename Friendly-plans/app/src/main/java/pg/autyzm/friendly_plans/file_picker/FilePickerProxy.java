package pg.autyzm.friendly_plans.file_picker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.net.URI;
import java.util.regex.Pattern;

import pg.autyzm.friendly_plans.asset.AssetType;

public class FilePickerProxy {

    private static final String FILE_PATTERN = "([^\\s]+(\\.(?i)(%s))$)";
    private Fragment fragment;

    public void openFilePicker(Fragment fragment, AssetType assetType) {
        this.fragment = fragment;
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(fragment.getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
        } else {
//            new MaterialFilePicker()
//                    .withFragment(fragment)
//                    .withRequestCode(1)
//                    .withHiddenFiles(true)
//                    .withFilterDirectories(true)
////                    .withFilter(getPattern(assetType))
//                    .start();
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
//            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

            fragment.startActivityForResult(intent,1);
        }
    }

    public boolean isPickFileRequested(int requestCode, AssetType assetType) {
        int a = getRequestCode(assetType);
        return requestCode == getRequestCode(assetType);
    }

    public boolean isFilePicked(int resultCode) {
        return true;
//        return !FilePickerActivity.RESULT_FILE_PATH.isEmpty();
    }

      public Uri getFilePath(Intent data) {
        if(data != null) {
//            return data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
//            String nazwa =getFileName(data.getData());
//            Uri uri = data.getData();
//            Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
//                    DocumentsContract.getTreeDocumentId(uri));
//            String path = getPath(this, docUri);
            return data.getData();
        }else {
            return null;

        }
    }

    private Pattern getPattern(AssetType assetType) {
        return Pattern.compile(String.format(FILE_PATTERN, assetType.getPattern()));
    }

    private int getRequestCode(AssetType assetType) {
        return assetType.ordinal();
    }
}
