package pg.autyzm.friendly_plans.test_helpers;

import org.mockito.Mockito;
import pg.autyzm.friendly_plans.file_picker.FilePickerModule;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;

public class FilePickerModuleMock extends FilePickerModule {

    private FilePickerProxy filePickerProxy;

    public FilePickerModuleMock() {
        filePickerProxy = Mockito.mock(FilePickerProxy.class);
    }

    @Override
    public FilePickerProxy getFilePickerProxy() {
        return filePickerProxy;
    }
}
