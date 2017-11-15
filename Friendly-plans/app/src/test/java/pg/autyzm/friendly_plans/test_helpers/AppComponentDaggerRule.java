package pg.autyzm.friendly_plans.test_helpers;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.DaggerAppComponent;

public class AppComponentDaggerRule implements TestRule {

    private RepositoryModuleMock repositoryModuleMock;
    private StringProviderModuleMock stringProviderModuleMock;
    private ValidationModuleMock validationModuleMock;
    private FilePickerModuleMock filePickerModuleMock;
    private MediaPlayerModuleMock mediaPlayerModuleMock;
    private ToastUserNotifierModuleMock toastUserNotifierModuleMock;
    private AssetsHelperModuleMock assetsHelperModuleMock;
    private AppComponent appComponent;

    public AppComponentDaggerRule() {
        initMockModules();
        appComponent = buildAppComponentWithMocks();
    }

    private void initMockModules() {
        repositoryModuleMock = new RepositoryModuleMock();
        stringProviderModuleMock = new StringProviderModuleMock();
        validationModuleMock = new ValidationModuleMock();
        filePickerModuleMock = new FilePickerModuleMock();
        mediaPlayerModuleMock = new MediaPlayerModuleMock();
        toastUserNotifierModuleMock = new ToastUserNotifierModuleMock();
        assetsHelperModuleMock = new AssetsHelperModuleMock();
    }

    private AppComponent buildAppComponentWithMocks() {
        DaoSessionComponentMock daoSessionComponentMock = DaggerDaoSessionComponentMock.builder()
            .daoSessionModuleMock(new DaoSessionModuleMock())
            .build();
        return DaggerAppComponent.builder()
            .daoSessionComponent(daoSessionComponentMock)
            .repositoryModule(repositoryModuleMock)
            .stringProviderModule(stringProviderModuleMock)
            .validationModule(validationModuleMock)
            .filePickerModule(filePickerModuleMock)
            .mediaPlayerModule(mediaPlayerModuleMock)
            .toastUserNotifierModule(toastUserNotifierModuleMock)
            .assetsHelperModule(assetsHelperModuleMock)
            .build();
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

            }
        };

    }

    public RepositoryModuleMock getRepositoryModuleMock() {
        return repositoryModuleMock;
    }

    public StringProviderModuleMock getStringProviderModuleMock() {
        return stringProviderModuleMock;
    }

    public ValidationModuleMock getValidationModuleMock() {
        return validationModuleMock;
    }

    public FilePickerModuleMock getFilePickerModuleMock() {
        return filePickerModuleMock;
    }

    public MediaPlayerModuleMock getMediaPlayerModuleMock() {
        return mediaPlayerModuleMock;
    }

    public ToastUserNotifierModuleMock getToastUserNotifierModuleMock() {
        return toastUserNotifierModuleMock;
    }

    public AssetsHelperModuleMock getAssetsHelperModuleMock() {
        return assetsHelperModuleMock;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}

