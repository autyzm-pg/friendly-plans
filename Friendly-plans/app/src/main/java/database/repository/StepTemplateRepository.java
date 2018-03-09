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

    public List<StepTemplate> get(String stepTemplateName) {
        return daoSession.getStepTemplateDao()
                .queryBuilder()
                .where(StepTemplateDao.Properties.Name.eq(stepTemplateName))
                .list();
    }


    public List<StepTemplate> getAll(Long taskTemplateId) {
        return daoSession.getStepTemplateDao()
                .queryBuilder()
                .where(Properties.TaskTemplateId.eq(taskTemplateId))
                .list();
    }

    public void delete(Long stepId) {
        daoSession.getStepTemplateDao()
                .deleteByKey(stepId);
    }
}
