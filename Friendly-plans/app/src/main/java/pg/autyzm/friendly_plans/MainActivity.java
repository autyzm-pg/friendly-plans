package pg.autyzm.friendly_plans;

import android.os.Bundle;
import dao.DaggerDaoSessionComponent;
import dao.DaoSessionComponent;
import dao.DaoSessionModule;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DaoSessionComponent daoSessionComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        daoSessionComponent = DaggerDaoSessionComponent.builder()
                .daoSessionModule(new DaoSessionModule(this))
                .build();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_management);
    }
}
