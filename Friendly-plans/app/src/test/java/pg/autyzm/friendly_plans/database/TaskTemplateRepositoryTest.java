package pg.autyzm.friendly_plans.database;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import database.entities.DaoSession;
import database.entities.TaskTemplate;
import database.entities.TaskTemplateDao;
import database.repository.TaskTemplateRepository;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TaskTemplateRepositoryTest {

    private static final String TASK_NAME = "taskName";
    private static final int DURATION_TIME = 3;
    private static final Long PICTURE_ID = 32L;
    private static final Long SOUND_ID = 31L;

    @InjectMocks
    TaskTemplateRepository taskTemplateRepository;
    @Mock
    private DaoSession daoSession;
    @Mock
    private TaskTemplateDao taskTemplateDao;

    private Long randomId;

    @Before
    public void setUp() {
        randomId = new Random().nextLong();
        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setId(randomId);
        taskTemplate.setName(TASK_NAME);
        taskTemplate.setDurationTime(DURATION_TIME);

        when(daoSession.getTaskTemplateDao()).thenReturn(taskTemplateDao);
        when(taskTemplateDao.insert(any(TaskTemplate.class))).thenReturn(randomId);
        when(taskTemplateDao.load(randomId)).thenReturn(taskTemplate);
    }

    @Test
    public void When_CreatingATaskTemplate_Expect_InsertMethodBeCalled() {
        taskTemplateRepository.create(TASK_NAME, DURATION_TIME, PICTURE_ID, SOUND_ID);
        verify(taskTemplateDao, times(1)).insert(any(TaskTemplate.class));
    }

    @Test
    public void When_CreatingATaskTemplate_Expect_NewIdBeReturned() {
        long id = taskTemplateRepository.create(TASK_NAME, DURATION_TIME, PICTURE_ID, SOUND_ID);
        assertThat(id, is(randomId));
    }

    @Test
    public void When_CreatingATaskWithoutSoundAndPicture_Expect_NewIdToBeReturned() {
        long id = taskTemplateRepository.create(TASK_NAME, DURATION_TIME, null, null);
        assertThat(id, is(randomId));
    }

    @Test
    public void When_GettingATaskTemplate_Expect_LoadMethodBeCalled() {
        taskTemplateRepository.get(randomId);
        verify(taskTemplateDao, times(1)).load(randomId);
    }

    @Test
    public void When_GettingATaskTemplate_Expect_TaskTemplateToBeReturned() {
        TaskTemplate taskTemplate = taskTemplateRepository.get(randomId);
        assertThat(taskTemplate.getName(), is(TASK_NAME));
        assertThat(taskTemplate.getDurationTime(), is(DURATION_TIME));
    }

    @Test
    public void When_DeletingATaskTemplateByName_Expect_DeleteByKeyMethodBeCalled() {
        taskTemplateRepository.delete(randomId);
        verify(taskTemplateDao, times(1)).deleteByKey(randomId);
    }

    @Test
    public void When_GettingAllTaskTemplate_Expect_LoadAllMethodBeCalled() {
        taskTemplateRepository.getAll();
        verify(taskTemplateDao, times(1)).loadAll();
    }
}
