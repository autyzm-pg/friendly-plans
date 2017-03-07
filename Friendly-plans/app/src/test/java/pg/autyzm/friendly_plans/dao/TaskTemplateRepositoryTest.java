package pg.autyzm.friendly_plans.dao;

import database.DaoSession;
import database.TaskTemplateDao;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

import dao.TaskTemplateRepository;
import database.TaskTemplate;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskTemplateRepositoryTest {

    public static final String TASK_NAME = "taskName";
    public static final int DURATION_TIME = 3;

    @Mock
    private DaoSession daoSession;

    @Mock
    private TaskTemplateDao taskTemplateDao;

    @InjectMocks
    TaskTemplateRepository taskTemplateRepository;
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
        taskTemplateRepository.create(TASK_NAME, DURATION_TIME);
        verify(taskTemplateDao, times(1)).insert(any(TaskTemplate.class));
    }

    @Test
    public void When_CreatingATaskTemplate_Expect_NewIdBeReturned() {
        long id = taskTemplateRepository.create(TASK_NAME, DURATION_TIME);
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
}
