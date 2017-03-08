package dao;

import database.DaoSession;
import database.TaskTemplate;
import database.TaskTemplateDao.Properties;
import java.util.List;

public class TaskTemplateRepository {

    private DaoSession daoSession;

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

    public List<TaskTemplate> get(String taskTemplateName) {
        return daoSession.getTaskTemplateDao()
                .queryBuilder()
                .where(Properties.Name.eq(taskTemplateName))
                .list();
    }

    public void delete(Long id) {
        daoSession.getTaskTemplateDao().deleteByKey(id);
    }

    public List<TaskTemplate> getAll() {
        return daoSession.getTaskTemplateDao().loadAll();
    }

}
