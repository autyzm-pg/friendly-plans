package dao;

import database.DaoSession;
import database.TaskTemplate;
import javax.inject.Inject;

public class TaskTemplateRepository {

    private DaoSession daoSession;

    @Inject
    public TaskTemplateRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public long create(String name, int durationTime) {
        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setName(name);
        taskTemplate.setDurationTime(durationTime);

        return daoSession.getTaskTemplateDao().insert(taskTemplate);
    }

    public TaskTemplate get(Long id) {
        return daoSession.getTaskTemplateDao().load(id);
    }
}
