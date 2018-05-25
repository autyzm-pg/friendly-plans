package pg.autyzm.friendly_plans.view.view_fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import database.repository.AssetRepository;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.validation.ValidationResult;
import pg.autyzm.friendly_plans.validation.ValidationStatus;
import pg.autyzm.friendly_plans.view.components.SoundComponent;
import pg.autyzm.friendly_plans.view.task_create.ImagePreviewDialog;

public abstract class CreateFragment extends Fragment {

    @Inject
    protected ToastUserNotifier toastUserNotifier;
    @Inject
    public FilePickerProxy filePickerProxy;
    @Inject
    protected AssetRepository assetRepository;

    protected EditText soundFileName;
    protected Long soundId;
    protected ImageButton clearSound;
    protected SoundComponent soundComponent;
    protected EditText pictureFileName;
    protected Long pictureId;
    protected ImageView picturePreview;
    protected ImageButton clearPicture;


    protected boolean handleInvalidResult(EditText editText, ValidationResult validationResult) {
        if (validationResult.getValidationStatus().equals(ValidationStatus.INVALID)) {
            editText.setError(validationResult.getValidationInfo());
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (filePickerProxy.isFilePicked(resultCode)) {
            if (filePickerProxy.isPickFileRequested(requestCode, AssetType.PICTURE)) {
                handleAssetSelecting(data, AssetType.PICTURE);
            } else if (filePickerProxy.isPickFileRequested(requestCode, AssetType.SOUND)) {
                handleAssetSelecting(data, AssetType.SOUND);
            }
        }
    }

    protected void handleAssetSelecting(Intent data, AssetType assetType) {
        Context context = getActivity().getApplicationContext();
        String filePath = filePickerProxy.getFilePath(data);
        AssetsHelper assetsHelper = new AssetsHelper(context);
        try {
            String assetName = assetsHelper.makeSafeCopy(filePath);
            Long assetId = assetRepository
                    .create(AssetType.getTypeByExtension(assetName), assetName);
            setAssetValue(assetType, assetName, assetId);
        } catch (IOException e) {
            showToastMessage(R.string.picking_file_error);
        }
    }

    protected abstract void setAssetValue(AssetType assetType, String assetName, Long assetId);

    protected void showToastMessage(int resourceStringId) {
        toastUserNotifier.displayNotifications(
                resourceStringId,
                getActivity().getApplicationContext());
    }


    protected String retrieveImageFile(Long pictureId) {
        String imageFileName = assetRepository.get(pictureId).getFilename();
        String fileDir = getActivity().getApplicationContext().getFilesDir().toString();
        return fileDir + File.separator + imageFileName;
    }

    @Nullable
    protected Long handleSavingError(RuntimeException exception) {
        Log.e("Step Create View", "Error saving step", exception);
        showToastMessage(R.string.save_step_error_message);
        return null;
    }

    protected void showPreview(Long pictureId, ImageView picturePreview) {
        Picasso.with(getActivity().getApplicationContext())
                .load(new File(retrieveImageFile(pictureId)))
                .into(picturePreview);
    }

    protected void showPicture(Long pictureId) {
        ImagePreviewDialog preview = new ImagePreviewDialog();
        Bundle args = new Bundle();
        args.putString("imgPath", retrieveImageFile(pictureId));
        preview.setArguments(args);
        preview.show(getFragmentManager(), "preview");
    }

    protected void clearSound() {
        soundFileName.setText("");
        soundId = null;
        clearSound.setVisibility(View.INVISIBLE);
        soundComponent.setSoundId(null);
        soundComponent.stopActions();
    }

    protected void clearPicture() {
        pictureFileName.setText("");
        pictureId = null;
        picturePreview.setImageDrawable(null);
        clearPicture.setVisibility(View.INVISIBLE);
    }
}
