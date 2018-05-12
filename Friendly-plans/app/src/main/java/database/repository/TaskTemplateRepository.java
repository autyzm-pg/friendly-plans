package database.repository;

import database.entities.DaoSession;
import database.entities.TaskTemplate;
import database.entities.TaskTemplateDao.Properties;
import java.util.List;

public class TaskTemplateRepository {

    private DaoSession daoSession;

    public TaskTemplateRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public long create(String name, Integer durationTime, Long pictureId, Long soundId) {
        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setName(name);
        taskTemplate.setDurationTime(durationTime);
        taskTemplate.setPictureId(pictureId);
        taskTemplate.setSoundId(soundId);

        return daoSession.getTaskTemplateDao().insert(taskTemplate);
    }

    public void update(Long taskId, String name, Integer durationTime, Long pictureId, Long soundId) {
        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setId(taskId);
        taskTemplate.setName(name);
        taskTemplate.setDurationTime(durationTime);
        taskTemplate.setPictureId(pictureId);
        taskTemplate.setSoundId(soundId);

        daoSession.getTaskTemplateDao().update(taskTemplate);
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

    public List<TaskTemplate> getAll() {
        return daoSession.getTaskTemplateDao().loadAll();
    }

    public void delete(Long id) {
        daoSession.getTaskTemplateDao().deleteByKey(id);
    }

    public void deleteAll() {
        daoSession.getTaskTemplateDao().deleteAll();
    }
}
