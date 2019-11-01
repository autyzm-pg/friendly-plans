package pg.autyzm.friendly_plans.child_app.view.step_list;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

import database.entities.StepTemplate;
import pg.autyzm.friendly_plans.R;

public class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.StepRecyclerViewHolder> {
    private List<StepTemplate> steps;
    private String imageDirectory;
    public Integer currentStepPosition = -1;
    private StepItemClickListener stepItemClickListener;

    protected interface StepItemClickListener {
        void selectStepListener(int position, TextView stepName);
    }

    public void test(int position){
        if (position == currentStepPosition + 1)
            currentStepPosition = position;
        notifyDataSetChanged();
    }
//
//    private StepItemClickListener stepItemClickListener = new StepItemClickListener(){
//
//        @Override
//        public void selectStepListener(int position) {
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                public void run() {
//                    while(currentStepPosition != 3){
//                        try {
//                            Thread.sleep(1500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        currentStepPosition++;
//                        notifyDataSetChanged();
//                    }
//                }
//            });
//            if (position == currentStepPosition + 1)
//                currentStepPosition = position;
//            notifyDataSetChanged();
//            Executors.newSingleThreadExecutor().execute(new Runnable() {
//                @Override
//                public void run() {
//                    while(currentStepPosition != 3){
//                        try {
//                            Thread.sleep(1500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        currentStepPosition++;
//                        notifyDataSetChanged();
//                    }
//                }
//            });
//        }
//    };

    static class StepRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView stepName;
        ImageView stepImage;
        String imageDirectory;
        StepItemClickListener stepItemClickListener;

        StepRecyclerViewHolder(View itemView, String imageDirectory, StepItemClickListener stepItemClickListener) {
            super(itemView);
            this.stepName = (TextView) itemView.findViewById(R.id.id_tv_step_name);
            this.stepImage = (ImageView) itemView.findViewById(R.id.id_iv_step_image);
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
                stepItemClickListener.selectStepListener(getAdapterPosition(), stepName);
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
        if (currentStepPosition != null && position == currentStepPosition)
            holder.itemView.setBackgroundColor(Color.parseColor("#8BF600"));
        else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        if (position < currentStepPosition)
            holder.stepName.setPaintFlags(holder.stepName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }
}
