package pg.autyzm.friendly_plans.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import database.entities.DaoSession;
import database.entities.StepTemplate;
import database.entities.StepTemplateDao;
import database.repository.StepTemplateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StepTemplateRepositoryTest {

    private final static String STEP_NAME = "step_name";
    private final static int ORDER = 1;
    private final static Long PICTURE_ID = 2L;
    private final static Long SOUND_ID = 3L;
    private final static Long TASK_TEMPLATE_ID = 4L;
    private final static Long STEP_ID = 5L;

    @InjectMocks
    StepTemplateRepository stepTemplateRepository;
    @Mock
    private DaoSession daoSession;
    @Mock
    private StepTemplateDao stepTemplateDao;

    @Before
    public void setUp() {
        when(daoSession.getStepTemplateDao()).thenReturn(stepTemplateDao);
    }

    @Test
    public void whenCreatingAStepTemplateExpectInsertMethodBeCalled() {
        stepTemplateRepository.create(STEP_NAME, ORDER, PICTURE_ID, SOUND_ID, TASK_TEMPLATE_ID);

        ArgumentCaptor<StepTemplate> stepTemplateArgumentCaptor = ArgumentCaptor
                .forClass(StepTemplate.class);
        verify(stepTemplateDao).insert(stepTemplateArgumentCaptor.capture());

        StepTemplate stepTemplate = stepTemplateArgumentCaptor.getValue();
        assertThat(stepTemplate.getName(), is(STEP_NAME));
        assertThat(stepTemplate.getSoundId(), is(SOUND_ID));
        assertThat(stepTemplate.getPictureId(), is(PICTURE_ID));
        assertThat(stepTemplate.getOrder(), is(ORDER));
        assertThat(stepTemplate.getTaskTemplateId(), is(TASK_TEMPLATE_ID));
    }

    @Test
    public void whenDeletingAStepTemplateExpectDeleteMethodBeCalled() {
        stepTemplateRepository.delete(STEP_ID);

        verify(stepTemplateDao).deleteByKey(STEP_ID);
    }
}