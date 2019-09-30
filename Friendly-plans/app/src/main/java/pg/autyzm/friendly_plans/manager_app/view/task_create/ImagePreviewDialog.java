package pg.autyzm.friendly_plans.manager_app.view.task_create;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.io.File;
import pg.autyzm.friendly_plans.R;

public class ImagePreviewDialog extends DialogFragment {

    private static final String IMG_PATH = "imgPath";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.picture_full_size_preview_dialog, null);

        ImageView imagePreview = (ImageView) v.findViewById(R.id.iv_picture_preview_full);
        imagePreview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        builder.setView(v);

        String pathToImage = getArguments().getString(IMG_PATH);
        Picasso.with(getActivity().getApplicationContext())
                .load(new File(pathToImage))
                .into(imagePreview);

        return builder.create();
    }
}
