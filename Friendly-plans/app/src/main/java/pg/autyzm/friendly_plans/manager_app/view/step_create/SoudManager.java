//package pg.autyzm.friendly_plans.manager_app.view.step_create;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import pg.autyzm.friendly_plans.manager_app.view.components.SoundComponent;
//
//public class SoudManager {
//    private final EditText soundFileName;
//    private final ImageButton clearSound;
//
//    private Long soundId;
//    private SoundComponent soundComponent;
//
//
//    public SoudManager(EditText soundFileName,
//            ImageButton clearSound,
//            ImageButton playSoundIcon,
//            Context applicationContext) {
//        this.soundFileName = soundFileName;
//        this.clearSound = clearSound;
//
//        soundComponent = SoundComponent.getSoundComponent(
//                soundId, playSoundIcon, applicationContext, appComponent);
//    }
//
//    protected void clearSound() {
//        soundFileName.setText("");
//        soundId = null;
//        clearSound.setVisibility(View.INVISIBLE);
//        soundComponent.setSoundId(null);
//        soundComponent.stopActions();
//    }
//
//}
