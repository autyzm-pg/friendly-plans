package pg.autyzm.friendly_plans.test_helpers;

import java.util.Random;

public class RandomGenerator {

    public static Long getId() {
        return new Random().nextLong();
    }
}
