package pg.autyzm.friendly_plans.manager_app.view.step_create;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import database.entities.Asset;
import database.entities.StepTemplate;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.test_helpers.RandomGenerator;

public class StepCreateDataTest {

    private static final Long TASK_ID = RandomGenerator.getId();
    private static final Long PICTURE_ID = RandomGenerator.getId();
    private static final Long SOUND_ID = RandomGenerator.getId();
    private static final int DURATION_TIME = 30;
    private static final String STEP_NAME = "step name";
    private static final String PICTURE_NAME = "picture name_234254664.jpg";
    private static final String TRANSFORMED_PICTURE_NAME = "picture name.jpg";
    private static final String SOUND_NAME = "sound name_231848922.mp3";
    private static final String TRANSFORMED_SOUND_NAME = "sound name.mp3";

    private Asset pictureAsset;
    private Asset soundAsset;

    @Before
    public void setUp() {
        pictureAsset = new Asset();
        pictureAsset.setFilename(PICTURE_NAME);
        pictureAsset.setId(PICTURE_ID);
        pictureAsset.setType(AssetType.PICTURE.toString());

        soundAsset = new Asset();
        soundAsset.setFilename(SOUND_NAME);
        soundAsset.setId(SOUND_ID);
        soundAsset.setType(AssetType.SOUND.toString());
    }

    @Test
    public void whenCreatingStepCreateDataFromStepTemplateExpectValuesReturned() {
        StepCreateData stepCreateData = new StepCreateData(TASK_ID);
        StepTemplate expectedStepTemplate = new StepTemplate();
        expectedStepTemplate.setName(STEP_NAME);
        expectedStepTemplate.setDurationTime(DURATION_TIME);
        expectedStepTemplate.setPicture(pictureAsset);
        expectedStepTemplate.setSound(soundAsset);

        stepCreateData.setStepTemplate(expectedStepTemplate);

        assertThat(stepCreateData.getTaskId(), is(TASK_ID));
        assertThat(stepCreateData.getStepName(), is(STEP_NAME));
        assertThat(stepCreateData.getStepDuration(), is(String.valueOf(DURATION_TIME)));
        assertThat(stepCreateData.getPictureName(), is(TRANSFORMED_PICTURE_NAME));
        assertThat(stepCreateData.getSoundName(), is(TRANSFORMED_SOUND_NAME));
    }

    @Test
    public void whenCreatingStepCreateDataWithEmptyValueExpectEmptyValuesReturned() {
        StepCreateData stepCreateData = new StepCreateData(TASK_ID);
        StepTemplate expectedStepTemplate = new StepTemplate();

        stepCreateData.setStepTemplate(expectedStepTemplate);

        assertThat(stepCreateData.getPictureName(), is(StringUtils.EMPTY));
        assertThat(stepCreateData.getSoundName(), is(StringUtils.EMPTY));
        assertThat(stepCreateData.getStepName(), nullValue());
        assertThat(stepCreateData.getStepDuration(), is(StringUtils.EMPTY));
    }
}
