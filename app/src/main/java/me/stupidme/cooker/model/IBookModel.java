package me.stupidme.cooker.model;

import java.util.List;

/**
 * Created by StupidL on 2017/3/8.
 */

public interface IBookModel {

    void addBookToDataBase(BookBean bookBean);

    List<String> getAllCookersName();

}
