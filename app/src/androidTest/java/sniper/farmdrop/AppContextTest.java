package sniper.farmdrop;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Created by sniper on 16-Jan-2017.
 */

@RunWith(AndroidJUnit4.class)
public class AppContextTest {
    /**
     * Test to check the main application package name
     * @throws Exception
     */
    @Test
    public void useAppContext() throws Exception {
        final String appPackage = BuildConfig.DEBUG ? "sniper.farmdrop.debug" : "sniper.farmdrop";
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(appPackage, appContext.getPackageName());
    }
}
