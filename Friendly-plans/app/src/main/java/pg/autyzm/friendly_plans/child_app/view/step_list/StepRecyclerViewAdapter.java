package pg.autyzm.friendly_plans.child_app.view.step_list;

import android.graphics.Color;
import android.graphics.Paint;
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
import pg.autyzm.friendly_plans.child_app.utility.Consts;
import pg.autyzm.friendly_plans.child_app.view.common.ChildActivityList;
import pg.autyzm.friendly_plans.child_app.view.common.ChildActivityState;

public class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.StepRecyclerViewHolder> implements ChildActivityList {
    private StepItemClickListener stepItemClickListener;
    private List<StepTemplate> steps;
    private String imageDirectory;

    private Integer currentStepPosition = 0;
    Integer getCurrentStepPosition() { return currentStepPosition; }
    private ChildActivityState currentStepState = ChildActivityState.PENDING_START;
    ChildActivityState getCurrentStepState() { return currentStepState; }
    void setCurrentStepState(ChildActivityState state) { this.currentStepState = state; }

    protected interface StepItemClickListener {
        void selectStepListener(int position, TextView stepName);
    }

    static class StepRecyclerViewHolder extends RecyclerView.ViewHolder {
        StepItemClickListener stepItemClickListener;
        TextView stepName;

        ImageView stepImage;
        String imageDirectory;

        TextView durationLabel;

        StepRecyclerViewHolder(View itemView, String imageDirectory, StepItemClickListener stepItemClickListener) {
            super(itemView);
            this.stepName = (TextView) itemView.findViewById(R.id.id_tv_step_name);
            this.stepImage = (ImageView) itemView.findViewById(R.id.id_iv_step_image);
            this.durationLabel = (TextView) itemView.findViewById(R.id.id_tv_step_duration_time);
            this.imageDirectory = imageDirectory;
            this.stepItemClickListener = stepItemClickListener;
            itemView.setOnClickListener(stepItemListener);
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

        View.OnClickListener stepItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepItemClickListener.selectStepListener(getAdapterPosition(), durationLabel);
            }
        };
    }

    StepRecyclerViewAdapter(List<StepTemplate> steps, String imageDirectory, StepItemClickListener stepItemClickListener)
    {
        this.steps = steps;
        this.imageDirectory = imageDirectory;
        this.stepItemClickListener = stepItemClickListener;
    }

    @Override
    public StepRecyclerViewAdapter.StepRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                             int viewType) {
        View stepView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_app_item_step, parent, false);
        return new StepRecyclerViewAdapter.StepRecyclerViewHolder(stepView, imageDirectory, stepItemClickListener);
    }

    @Override
    public void onBindViewHolder(StepRecyclerViewAdapter.StepRecyclerViewHolder holder, int position) {
        if (steps != null && !steps.isEmpty()) {
            StepTemplate step = steps.get(position);
            holder.setUpHolder(step);
        }
        holder.itemView.setBackgroundColor(Color.TRANSPARENT);

        if (currentStepPosition != null) {
            if (position == currentStepPosition)
                holder.itemView.setBackgroundColor(Color.parseColor(Consts.GREEN));
            else if (position < currentStepPosition) {
                holder.stepName.setPaintFlags(holder.stepName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.itemView.setBackgroundColor(Color.parseColor(Consts.GREY));
            }
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    @Override
    public void activityCompleted() {
        currentStepState = ChildActivityState.FINISHED;
    }

    void setCurrentStep(int position){
        currentStepPosition = position;
        currentStepState = ChildActivityState.PENDING_START;
        notifyDataSetChanged();
    }
}
