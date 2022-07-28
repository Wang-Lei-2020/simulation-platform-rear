package com.bjtu.simulation_platform_rear.service;


import com.bjtu.simulation_platform_rear.dto.ContentPage;
import com.bjtu.simulation_platform_rear.entity.Book;
import com.github.pagehelper.PageInfo;

import java.io.UnsupportedEncodingException;

public interface BookService {
    void add(Book book);

    void update(Book book);

    void del(Integer id);

    Book findById(Integer id);

    PageInfo getBookList(Integer pageNum,Integer pageSize);

    void setContent(Integer id) throws UnsupportedEncodingException;

    void readTxtFile(String filePath) throws UnsupportedEncodingException;

    int getPages();

    String getContent(int page) throws UnsupportedEncodingException;

    ContentPage getContentPage(Integer id, int page) throws UnsupportedEncodingException;
}
