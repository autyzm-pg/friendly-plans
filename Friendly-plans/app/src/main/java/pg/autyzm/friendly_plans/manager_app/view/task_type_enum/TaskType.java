package pg.autyzm.friendly_plans.manager_app.view.task_type_enum;

import pg.autyzm.friendly_plans.R;

public enum TaskType {
    TASK(1, R.string.create_plan_tasks_list_info_type_1, R.string.create_plan_add_tasks_info_type_1,
            R.string.create_plan_add_tasks_type_1, R.id.id_navigation_plan_tasks),
    PRIZE(2, R.string.create_plan_tasks_list_info_type_2,
            R.string.create_plan_add_tasks_info_type_2,
            R.string.create_plan_add_tasks_type_2, R.id.id_navigation_plan_prizes),
    INTERACTION(3, R.string.create_plan_tasks_list_info_type_3,
            R.string.create_plan_add_tasks_info_type_3, R.string.create_plan_add_tasks_type_3,
            R.id.id_navigation_plan_interactions);

    private final int taskListInfoLabel;
    private final int addTaskInfoLabel;
    private final int addLabel;
    private final int typeId;
    private final int navigationButtonId;


    TaskType(Integer typeId, Integer taskListInfoLabel, Integer addTaskInfoLabel,
            Integer addLabel, Integer navigationButtonId) {
        this.typeId = typeId;
        this.taskListInfoLabel = taskListInfoLabel;
        this.addTaskInfoLabel = addTaskInfoLabel;
        this.addLabel = addLabel;
        this.navigationButtonId = navigationButtonId;
    }

    public Integer getTaskListInfoLabel() {
        return this.taskListInfoLabel;
    }

    public Integer getAddTaskInfoLabel() {
        return this.addTaskInfoLabel;
    }

    public Integer getAddLabel() {
        return this.addLabel;
    }

    public Integer getId() {
        return this.typeId;
    }

    public Integer getNavigationButtonId() {
        return this.navigationButtonId;
    }

    public static TaskType getTaskType(Integer typeId) {
        switch (typeId) {
            case 1:
                return TaskType.TASK;
            case 2:
                return TaskType.PRIZE;
            default:
                return TaskType.INTERACTION;
        }
    }
}
