package pg.autyzm.friendly_plans.manager_app.view.step_create;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import database.entities.StepTemplate;
import database.repository.AssetRepository;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.manager_app.view.task_create.ImagePreviewDialog;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class PictureManager {

    @Inject
    AssetRepository assetRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;

    private final ImageButton clearPicture;
    private final ImageView picturePreview;
    private final Context context;
    private final StepCreateData stepData;

    public static PictureManager getPictureManager(StepCreateData stepDate, ImageButton clearPicture, ImageView picturePreview,
            Context context, AppComponent appComponent) {
        final PictureManager pictureManager = new PictureManager(stepDate, clearPicture, picturePreview, context);
        appComponent.inject(pictureManager);
        return pictureManager;
    }

    private PictureManager(StepCreateData stepDate, ImageButton clearPicture, ImageView picturePreview, Context context) {
        this.stepData = stepDate;
        this.clearPicture = clearPicture;
        this.picturePreview = picturePreview;
        this.context = context;
    }

    public void showPicture() {
        if (isPictureSet()) {
            clearPicture.setVisibility(View.VISIBLE);
            showPreview();
        } else {
            clearPicture.setVisibility(View.INVISIBLE);
            picturePreview.setImageDrawable(null);
        }
    }

    public void showPicturePreview(StepTemplate stepTemplate,
            Context applicationContext,
            FragmentManager fragmentManager) {
        ImagePreviewDialog preview = new ImagePreviewDialog();
        Bundle args = new Bundle();
        args.putString("imgPath", retrieveImageFile(stepTemplate.getPictureId(), applicationContext));

        preview.setArguments(args);
        preview.show(fragmentManager, "preview");
    }

    public void clearPicture() {
        stepData.setPicture(null);
        showPicture();
    }

    public void handleAssetSelecting(String filePath) {
        AssetsHelper assetsHelper = new AssetsHelper(context);
        try {
            String assetName = assetsHelper.makeSafeCopy(filePath);
            Long assetId = assetRepository
                    .create(AssetType.getTypeByExtension(assetName), assetName);
            stepData.setPicture(assetRepository.get(assetId));
            showPicture();
        } catch (IOException e) {
            toastUserNotifier.displayNotifications(R.string.picking_file_error, context);
        }
    }

    private boolean isPictureSet() {
        return stepData.getStepTemplate().getPictureId() != null;
    }

    private void showPreview() {
        Picasso.with(context)
                .load(new File(retrieveImageFile(stepData.getStepTemplate().getPictureId(), context)))
                .resize(0, picturePreview.getHeight())
                .into(picturePreview);
    }

    private String retrieveImageFile(Long pictureId, Context applicationContext) {
        String imageFileName = assetRepository.get(pictureId).getFilename();
        String fileDir = applicationContext.getFilesDir().toString();
        return fileDir + File.separator + imageFileName;
    }
}
