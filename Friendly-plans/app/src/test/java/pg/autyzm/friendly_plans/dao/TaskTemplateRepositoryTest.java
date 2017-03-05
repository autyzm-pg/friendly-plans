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

    @Before
    public void setUp() {
        Long randomId = new Random().nextLong();
        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setId(randomId);
        taskTemplate.setName(TASK_NAME);
        taskTemplate.setDurationTime(DURATION_TIME);

        when(daoSession.getTaskTemplateDao()).thenReturn(taskTemplateDao);
        when(taskTemplateDao.insert(any(TaskTemplate.class))).thenReturn(randomId);
        when(taskTemplateDao.load(randomId)).thenReturn(taskTemplate);
    }

    @Test
    public void When_CreatingATask_Expect_FetchItFromDb() {
        long id = taskTemplateRepository.create(TASK_NAME, DURATION_TIME);

        TaskTemplate taskTemplate = taskTemplateRepository.get(id);
        assertThat(taskTemplate.getName(), is(TASK_NAME));
        assertThat(taskTemplate.getDurationTime(), is(DURATION_TIME));
    }
}
