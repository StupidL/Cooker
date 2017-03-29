package me.stupidme.cooker.db;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import me.stupidme.cooker.model.BookBean;

import static org.junit.Assert.assertEquals;

/**
 * Created by StupidL on 2017/3/29.
 */
public class DBManager2Test {

    private DBManager2 manager2 = DBManager2.getInstance();

    @Test
    public void insertCooker() throws Exception {

    }

    @Test
    public void insertCookers() throws Exception {

    }

    @Test
    public void deleteCooker() throws Exception {

    }

    @Test
    public void deleteCookers() throws Exception {

    }

    @Test
    public void queryCooker() throws Exception {

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
    public void insertBook() throws Exception {
        BookBean bookBean = new BookBean();
        bookBean.setBookId(0x2123456L)
                .setCookerId(0xabc23L)
                .setCookerName("CookerA")
                .setCookerLocation("LocationA")
                .setCookerStatus("free")
                .setRiceWeight(500)
                .setPeopleCount(5)
                .setTaste("soft")
                .setTime("18:30");
        manager2.insertBook(bookBean);

        BookBean bookBean1 = manager2.queryBook(0x2123456L);

        assertEquals(bookBean.getBookId(), bookBean1.getBookId());
    }

    @Test
    public void insertBooks() throws Exception {

        List<BookBean> list = new ArrayList<>();
        for (long i = 0; i < 10; i++) {
            BookBean bookBean = new BookBean();
            long bId = 0x2123456L + i;
            long cId = 0xabc23L + i;
            Log.v("DBManager2Test", "bId = " + bId);
            Log.v("DBManager2Test", "cId = " + cId);
            bookBean.setBookId(bId)
                    .setCookerId(cId)
                    .setCookerName("Cooker" + i)
                    .setCookerLocation("Location" + i)
                    .setCookerStatus(i % 2 == 0 ? "free" : "booking")
                    .setRiceWeight(500 + i)
                    .setPeopleCount((int) (5 + i))
                    .setTaste(i % 2 == 0 ? "soft" : "hard")
                    .setTime("18:30");
            list.add(bookBean);
        }
        manager2.insertBooks(list);

        assertEquals(manager2.queryBooks().size(), list.size());
    }

    @Test
    public void queryBook() throws Exception {

    }

    @Test
    public void queryBooks() throws Exception {
        assertEquals(0x2123456L, manager2.queryBook(0x2123456L).getBookId());
    }

    @Test
    public void updateBook() throws Exception {
        BookBean bookBean = new BookBean();
        bookBean.setBookId(0x2123456L)
                .setCookerId(0xabc23L)
                .setCookerName("CookerAAA")
                .setCookerLocation("LocationA")
                .setCookerStatus("free")
                .setRiceWeight(500)
                .setPeopleCount(5)
                .setTaste("soft")
                .setTime("18:30");
        manager2.updateBook(bookBean);

        assertEquals("CookerAAA", manager2.queryBook(0x2123456L).getCookerName());
    }

    @Test
    public void updateBooks() throws Exception {

    }

    @Test
    public void deleteBook() throws Exception {
//        manager2.deleteBook(0x2123456L);
//
//        assertEquals(0, manager2.queryBook(0x2123456L).getBookId());
    }

    @Test
    public void deleteBooks() throws Exception {
//        manager2.deleteBooks();
//
//        assertEquals(0, manager2.queryBooks().size());
    }

}