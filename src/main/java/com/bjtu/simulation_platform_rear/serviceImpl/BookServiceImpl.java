package com.bjtu.simulation_platform_rear.serviceImpl;

import com.bjtu.simulation_platform_rear.dao.IBookDao;
import com.bjtu.simulation_platform_rear.dto.ContentPage;
import com.bjtu.simulation_platform_rear.entity.Book;
import com.bjtu.simulation_platform_rear.service.BookService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Resource
    private IBookDao bookDao;

    //每页字节数
    @Value("2048")
    private int PAGE_BYTES;
    //总页数
    private int pages;
    //总内容
    private byte[] content;
    //总字节数
    private int bytes;
    //总字数
    private int num;
    private String contents;

    @Override
    public void add(Book book) {
        bookDao.addBook(book);
    }

    @Override
    public void update(Book book) {

    }

    @Override
    public void del(Integer id) {
        bookDao.delBook(id);
    }

    @Override
    public Book findById(Integer id) {
        return bookDao.findById(id);
    }

    @Override
    public PageInfo getBookList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize); // 设定当前页码，以及当前页显示的条数
        //PageHelper.offsetPage(pageNum, pageSize);也可以使用此方式进行设置
        List<Book> list = bookDao.getBookList();
        PageInfo<Book> pageInfo = new PageInfo<Book>(list);
        return pageInfo;
    }

    @Override
    //根据书籍id设置文件
    public void setContent(Integer id) throws UnsupportedEncodingException {
        Book book = bookDao.findById(id);
        String localPath = new File("").getAbsolutePath();
        String relativePath = book.getFilepath();
        readTxtFile(localPath + relativePath);
    }

    @Override
    //读取文件得到总内容与总字节
    public void readTxtFile(String filePath) throws UnsupportedEncodingException {
        File textFile = new File(filePath);
        bytes = (int) textFile.length();
//        System.out.println(bytes);
        content = new byte[(int) textFile.length()];
        try (FileInputStream fileInputStream = new FileInputStream(textFile)) {
            fileInputStream.read(content);
//            System.out.println(new String(content, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        contents = new String(content, "UTF-8");
    }

    @Override
    //得到总页数
    public int getPages() {
        num = contents.length();
        pages = num / PAGE_BYTES + 1;
        return pages;
    }

    @Override
    //根据页数返回内容
    public String getContent(int page) throws UnsupportedEncodingException {
        String contents = new String(content, "UTF-8");
        String contxt = "";
        if (page == getPages()) {
            contxt = contents.substring((page - 1) * PAGE_BYTES, num);
        } else {
            contxt = contents.substring((page - 1) * PAGE_BYTES, page * PAGE_BYTES);
        }
        return contxt;
    }

    @Override
    public ContentPage getContentPage(Integer id, int page) throws UnsupportedEncodingException {
        setContent(id);
        int count = getPages();
        ContentPage contentPage = new ContentPage();
        contentPage.setBookMsg(findById(id));
        contentPage.setContent(getContent(page));
        contentPage.setFirstPage(page == 1);
        contentPage.setLastPage(page == count);
        contentPage.setNextPage(page+1);
        contentPage.setPrePage(page-1);
        contentPage.setPageNum(page);
        contentPage.setPages(getPages());
        int navigateFirstPage = (page-4)>0 ? (page-4):1;
        int navigateLastPage = (page+4)>count ? count:(page+4);
        contentPage.setNavigateFirstPage(navigateFirstPage);
        contentPage.setNavigateLastPage(navigateLastPage);

        return contentPage;
    }
}
