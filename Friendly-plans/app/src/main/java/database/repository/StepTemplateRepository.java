package database.repository;

import database.entities.DaoSession;
import database.entities.StepTemplate;
import database.entities.StepTemplateDao;
import database.entities.StepTemplateDao.Properties;
import java.util.List;

public class StepTemplateRepository {

    private DaoSession daoSession;

    public StepTemplateRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public long create(String name, int order, Long pictureId, Long soundId, Long taskTemplateId) {
        StepTemplate stepTemplate = new StepTemplate();
        stepTemplate.setName(name);
        stepTemplate.setOrder(order);
        stepTemplate.setSoundId(soundId);
        stepTemplate.setPictureId(pictureId);
        stepTemplate.setTaskTemplateId(taskTemplateId);

        return daoSession.getStepTemplateDao().insert(stepTemplate);
    }

    public void update(Long stepId, String name, int order, Long pictureId, Long soundId, Long taskTemplateId) {
        StepTemplate stepTemplate = new StepTemplate();
        stepTemplate.setId(stepId);
        stepTemplate.setName(name);
        stepTemplate.setOrder(order);
        stepTemplate.setSoundId(soundId);
        stepTemplate.setPictureId(pictureId);
        stepTemplate.setTaskTemplateId(taskTemplateId);

        daoSession.getStepTemplateDao().update(stepTemplate);
    }

    public List<StepTemplate> get(String stepTemplateName) {
        return daoSession.getStepTemplateDao()
                .queryBuilder()
                .where(StepTemplateDao.Properties.Name.eq(stepTemplateName))
                .list();
    }

    public List<StepTemplate> get(String stepTemplateName, Long taskId) {
        return daoSession.getStepTemplateDao()
                .queryBuilder()
                .where(StepTemplateDao.Properties.Name.eq(stepTemplateName))
                .where(Properties.TaskTemplateId.eq(taskId))
                .list();
    }

    public StepTemplate get(Long id) {
        return daoSession.getStepTemplateDao().load(id);
    }


    public List<StepTemplate> getAll(Long taskTemplateId) {
        return daoSession.getStepTemplateDao()
                .queryBuilder()
                .where(Properties.TaskTemplateId.eq(taskTemplateId))
                .orderAsc(Properties.Order)
                .list();
    }

    public void delete(Long stepId) {
        daoSession.getStepTemplateDao()
                .deleteByKey(stepId);
    }
}
