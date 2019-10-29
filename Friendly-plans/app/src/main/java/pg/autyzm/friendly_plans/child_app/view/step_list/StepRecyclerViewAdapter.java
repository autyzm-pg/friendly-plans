package pg.autyzm.friendly_plans.child_app.view.step_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import database.entities.StepTemplate;
import pg.autyzm.friendly_plans.R;

public class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.StepRecyclerViewHolder> {
    private List<StepTemplate> steps;
    private String imageDirectory;

    static class StepRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView stepName;
        ImageView stepImage;
        String imageDirectory;
        StepRecyclerViewHolder(View itemView, String imageDirectory) {
            super(itemView);
            this.stepName = (TextView) itemView.findViewById(R.id.id_tv_step_name);
            this.stepImage = (ImageView) itemView.findViewById(R.id.id_iv_step_image);
            this.imageDirectory = imageDirectory;
        }

        void setUpHolder(StepTemplate step) {
            stepName.setText(step.getName());
            if (step.getPicture() != null) {
                String imageName = step.getPicture().getFilename();
                Picasso.get()
                        .load(new File(imageDirectory + File.separator + imageName))
                        .into(stepImage);
            } else {
                stepImage.setVisibility(View.INVISIBLE);
            }
        }
    }

    StepRecyclerViewAdapter(List<StepTemplate> steps, String imageDirectory) {
        this.steps = steps;
        this.imageDirectory = imageDirectory;
    }

    @Override
    public StepRecyclerViewAdapter.StepRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                             int viewType) {
        View stepView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_app_item_step, parent, false);
        return new StepRecyclerViewAdapter.StepRecyclerViewHolder(stepView, imageDirectory);
    }

    @Override
    public void onBindViewHolder(StepRecyclerViewAdapter.StepRecyclerViewHolder holder, int position) {
        if (steps != null && !steps.isEmpty()) {
            StepTemplate step = steps.get(position);
            holder.setUpHolder(step);
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }
}
