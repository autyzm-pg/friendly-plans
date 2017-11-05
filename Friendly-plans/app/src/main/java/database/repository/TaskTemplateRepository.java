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

    public long create(String name, int durationTime, Long pictureId, Long soundId) {
        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setName(name);
        taskTemplate.setDurationTime(durationTime);
        taskTemplate.setPictureId(pictureId);
        taskTemplate.setSoundId(soundId);

        return daoSession.getTaskTemplateDao().insert(taskTemplate);
    }

    public void update(Long taskId, String name, int durationTime, Long pictureId, Long soundId) {
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

    public boolean isNameExists(Long taskId, String name) {
        List<TaskTemplate> taskTemplates = get(name);
        if (taskTemplates.size() == 1) {
            return !taskId.equals(taskTemplates.get(0).getId());
        }

        return taskTemplates.size() > 1;
    }

    public boolean isNameExists(String name) {
        return get(name).size() > 0;
    }
}
