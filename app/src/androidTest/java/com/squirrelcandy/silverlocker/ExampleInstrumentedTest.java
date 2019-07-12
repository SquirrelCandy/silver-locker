package com.squirrelcandy.silverlocker;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.squirrelcandy.silverlocker.db.ItemDAO;
import com.squirrelcandy.silverlocker.models.Item;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.squirrelcandy.silverlocker", appContext.getPackageName());
    }

    @Test
    public void dbWriteTest() {
        Item item = new Item();
        item.setName("Test Name");
        item.setUsername("Tester");
        item.setEmail("test@test.com");
        item.setPassword("password");

        ItemDAO dao = new ItemDAO(InstrumentationRegistry.getTargetContext());
        int uid = (int) dao.saveItem(item);
        System.out.println("New UID = " + uid);
        assertTrue(uid > 0);
    }
}
