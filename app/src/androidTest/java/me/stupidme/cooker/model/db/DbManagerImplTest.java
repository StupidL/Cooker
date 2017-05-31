package me.stupidme.cooker.model.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.util.SharedPreferenceUtil;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by stupidl on 17-5-31.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DbManagerImplTest {

    private DbManagerImpl manager;
    private Context context;
    private Long userId;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        DbManagerImpl.init(context);
        manager = DbManagerImpl.getInstance();
        userId = SharedPreferenceUtil.getAccountUserId(0L);
    }

    @Test
    public void init() throws Exception {
        assertNotNull(context);
        assertNotNull(manager);
    }

    @Test
    public void getInstance() throws Exception {
        assertNotNull(manager);
    }

    @Test
    public void insertCooker() throws Exception {
        CookerBean cookerBean = new CookerBean();
        cookerBean.setCookerStatus("Free");
        cookerBean.setCookerName("Cooker0");
        cookerBean.setCookerId(new Random().nextLong());
        cookerBean.setCookerLocation("Location0");
        cookerBean.setUserId(userId);
        boolean success = manager.insertCooker(cookerBean);
        assertTrue(success);
    }

    @Test
    public void insertCookers() throws Exception {
        Random random = new Random();
        List<CookerBean> cookerBeanList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            CookerBean cookerBean = new CookerBean();
            cookerBean.setCookerStatus(i % 2 == 0 ? "Free" : "Booking");
            cookerBean.setCookerName("Cooker" + i);
            cookerBean.setCookerId(random.nextLong());
            cookerBean.setCookerLocation("Location" + i);
            cookerBeanList.add(cookerBean);
            cookerBean.setUserId(userId);
        }
        assertTrue(manager.insertCookers(cookerBeanList));
    }

    @Test
    public void queryCooker() throws Exception {

    }

    @Test
    public void queryCooker1() throws Exception {

    }

    @Test
    public void queryCookers() throws Exception {

    }

    @Test
    public void updateCooker() throws Exception {

    }

    @Test
    public void updateCookers() throws Exception {

    }

    @Test
    public void deleteCookers() throws Exception {

    }

    @Test
    public void deleteCooker() throws Exception {

    }

    @Test
    public void insertBook() throws Exception {

    }

    @Test
    public void insertBooks() throws Exception {

    }


    @Test
    public void queryBook() throws Exception {

    }

    @Test
    public void queryBooks() throws Exception {

    }

    @Test
    public void updateBook() throws Exception {

    }

    @Test
    public void updateBooks() throws Exception {

    }

    @Test
    public void deleteBook() throws Exception {

    }

    @Test
    public void deleteBooks() throws Exception {

    }

    @Test
    public void queryCookerNamesAll() throws Exception {

    }

    @Test
    public void insertBookHistory() throws Exception {

    }

    @Test
    public void insertBooksHistory() throws Exception {

    }

    @Test
    public void queryBookHistory() throws Exception {

    }

    @Test
    public void queryBooksHistory() throws Exception {

    }

    @Test
    public void updateBookHistory() throws Exception {

    }

    @Test
    public void updateBooksHistory() throws Exception {

    }

    @Test
    public void deleteBookHistory() throws Exception {

    }

    @Test
    public void deleteBooksHistory() throws Exception {

    }

}
