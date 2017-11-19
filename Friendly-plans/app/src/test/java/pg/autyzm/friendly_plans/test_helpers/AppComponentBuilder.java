package pg.autyzm.friendly_plans.test_helpers;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;

import android.media.MediaPlayer;
import database.entities.DaoSession;
import database.repository.AssetRepository;
import database.repository.DaoSessionModule;
import database.repository.RepositoryModule;
import database.repository.StepTemplateRepository;
import database.repository.TaskTemplateRepository;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.DaggerAppComponent;
import pg.autyzm.friendly_plans.MediaPlayerModule;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.asset.AssetsHelperModule;
import pg.autyzm.friendly_plans.file_picker.FilePickerModule;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifierModule;
import pg.autyzm.friendly_plans.string_provider.StringProviderModule;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;
import pg.autyzm.friendly_plans.validation.TaskValidation;
import pg.autyzm.friendly_plans.validation.ValidationModule;

public class AppComponentBuilder {

    private DaoSessionModule daoSessionModule;
    private RepositoryModule repositoryModule;
    private StringProviderModule stringProviderModule;
    private ValidationModule validationModule;
    private FilePickerModule filePickerModule;
    private MediaPlayerModule mediaPlayerModule;
    private ToastUserNotifierModule toastUserNotifierModule;
    private AssetsHelperModule assetsHelperModule;

    private DaoSession daoSession;
    private TaskTemplateRepository taskTemplateRepository;
    private AssetRepository assetRepository;
    private StepTemplateRepository stepTemplateRepository;
    private StringsProvider stringsProvider;
    private TaskValidation taskValidation;
    private FilePickerProxy filePickerProxy;
    private MediaPlayer mediaPlayer;
    private ToastUserNotifier toastUserNotifier;
    private AssetsHelper assetsHelper;

    public static AppComponentBuilder builder() {
        return new AppComponentBuilder();
    }

    private AppComponentBuilder() {
        initMockModules();
        initWithDefaultMocks();
    }

    public AppComponent buildAppComponent() {
        setUpModuleMocks();

        return DaggerAppComponent.builder()
                .daoSessionModule(daoSessionModule)
                .repositoryModule(repositoryModule)
                .stringProviderModule(stringProviderModule)
                .validationModule(validationModule)
                .filePickerModule(filePickerModule)
                .mediaPlayerModule(mediaPlayerModule)
                .toastUserNotifierModule(toastUserNotifierModule)
                .assetsHelperModule(assetsHelperModule)
                .build();
    }

    private void setUpModuleMocks() {
        when(daoSessionModule.getDaoSession()).thenReturn(daoSession);
        when(repositoryModule.getTaskTemplateRepository(any(DaoSession.class))).thenReturn(
                taskTemplateRepository);
        when(repositoryModule.getAssetRepostiory(any(DaoSession.class))).thenReturn(assetRepository);
        when(repositoryModule.getStepTemplateRepository(any(DaoSession.class))).thenReturn(
                stepTemplateRepository);
        when(stringProviderModule.getStringProvider()).thenReturn(stringsProvider);
        when(validationModule
                .getTaskValidation(any(StringsProvider.class), any(TaskTemplateRepository.class))).thenReturn(
                taskValidation);
        when(filePickerModule.getFilePickerProxy()).thenReturn(filePickerProxy);
        when(mediaPlayerModule.getMediaPlayer()).thenReturn(mediaPlayer);
        when(toastUserNotifierModule.getToastUserNotifier()).thenReturn(toastUserNotifier);
        when(assetsHelperModule.getAssetsHelper()).thenReturn(assetsHelper);
    }

    public AppComponentBuilder daoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        return this;
    }

    public AppComponentBuilder taskTemplateRepository(
            TaskTemplateRepository taskTemplateRepository) {
        this.taskTemplateRepository = taskTemplateRepository;
        return this;
    }

    public AppComponentBuilder assetRepository(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
        return this;
    }

    public AppComponentBuilder stepTemplateRepository(
            StepTemplateRepository stepTemplateRepository) {
        this.stepTemplateRepository = stepTemplateRepository;
        return this;
    }

    public AppComponentBuilder stringsProvider(
            StringsProvider stringsProvider) {
        this.stringsProvider = stringsProvider;
        return this;
    }

    public AppComponentBuilder taskValidation(TaskValidation taskValidation) {
        this.taskValidation = taskValidation;
        return this;
    }

    public AppComponentBuilder mediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        return this;
    }

    public AppComponentBuilder toastUserNotifier(
            ToastUserNotifier toastUserNotifier) {
        this.toastUserNotifier = toastUserNotifier;
        return this;
    }

    public AppComponentBuilder assetsHelper(AssetsHelper assetsHelperMock) {
        this.assetsHelper = assetsHelperMock;
        return this;
    }

    public AppComponentBuilder filePickerProxy(FilePickerProxy filePickerProxyMock) {
        this.filePickerProxy = filePickerProxyMock;
        return this;
    }

    private void initMockModules() {
        daoSessionModule = mock(DaoSessionModule.class);
        repositoryModule = mock(RepositoryModule.class);
        stringProviderModule = mock(StringProviderModule.class);
        validationModule = mock(ValidationModule.class);
        filePickerModule = mock(FilePickerModule.class);
        mediaPlayerModule = mock(MediaPlayerModule.class);
        toastUserNotifierModule = mock(ToastUserNotifierModule.class);
        assetsHelperModule = mock(AssetsHelperModule.class);
    }

    private void initWithDefaultMocks() {
        daoSession = mock(DaoSession.class);
        taskTemplateRepository = mock(TaskTemplateRepository.class);
        assetRepository = mock(AssetRepository.class);
        stepTemplateRepository = mock(StepTemplateRepository.class);
        stringsProvider = mock(StringsProvider.class);
        taskValidation = mock(TaskValidation.class);
        filePickerProxy = mock(FilePickerProxy.class);
        mediaPlayer = mock(MediaPlayer.class);
        toastUserNotifier = mock(ToastUserNotifier.class);
        assetsHelper = mock(AssetsHelper.class);
    }
}

