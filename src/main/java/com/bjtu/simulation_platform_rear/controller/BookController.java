package com.bjtu.simulation_platform_rear.controller;

import com.bjtu.simulation_platform_rear.common.Result;
import com.bjtu.simulation_platform_rear.dto.ContentPage;
import com.bjtu.simulation_platform_rear.entity.Book;
import com.bjtu.simulation_platform_rear.service.BookService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.UUID;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping(value = "/read")
    public Result<ContentPage> getBook(@RequestParam(value = "bookId") Integer bookId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
        try {
            return Result.success(bookService.getContentPage(bookId,pageNum));
        } catch (Exception exception) {
            exception.printStackTrace();
            return Result.error("542","获取内容失败！");
        }

    }

    @PostMapping(value = "/addbook")
    public Result addBook(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "bookName") String bookName, @RequestParam(value = "bookDesc") String bookDesc) throws Exception {
//        System.out.println(bookName + "\t" + bookDesc);
        if (file.isEmpty()) {
            return Result.error("512", "文件上传失败！");
        } else {
            //文件名
            String fileName = file.getOriginalFilename();
            //后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            if (!suffixName.equals(".txt")) {
                return Result.error("513", "文件类型错误！");
            }
            // 新文件名
            fileName = UUID.randomUUID() + suffixName;
            //获取系统时间
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int date = calendar.get(Calendar.DATE);
//            System.out.println(year + "/" + month + "/" + date);
            //创建文件夹
            String localPath = new File("").getAbsolutePath();
            String relativePath = "\\src\\main\\resources\\books\\" + year + "\\" + month + "\\" + date;
            localPath = localPath + relativePath;
//            System.out.println(filePath);
            File tempFile = new File(localPath, fileName);
            //判断路径是否存在，不存在则创建一个
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            //转存文件
            try {
                file.transferTo(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Book book = new Book();
            book.setName(bookName);
            book.setDescription(bookDesc);
            book.setFilepath(relativePath + "\\" + fileName);
            bookService.add(book);
            return Result.success();
        }
    }

    @PostMapping("/booklist")
    public Result<PageInfo<Book>> getBookList(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = bookService.getBookList(pageNum,pageSize);
        return Result.success(pageInfo);
    }

    @PostMapping("/delbook")
    public Result delBook(@RequestParam Integer bookId){
        try{
            Book existing = bookService.findById(bookId);
            if (existing == null) {
                return Result.error("576","书本不存在！");
            }

            //定义文件路径
            String filePath = existing.getFilepath();
            //这里因为我文件是相对路径 所以需要在路径前面加一个点
            File file = new File("." + filePath);
            if (file.exists()) {//文件是否存在
                file.delete();//删除文件
            }

            bookService.del(bookId);
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("324","删除书本失败！");
        }
    }

    @PostMapping(value = "/download")
    public ResponseEntity<byte[]> downloadBook(@RequestParam(value = "bookId") int bookId, @RequestHeader("User-Agent") String userAgent) throws Exception {
        Book book = bookService.findById(bookId);
        String localPath = new File("").getAbsolutePath();
        String relativePath = book.getFilepath();
        String filename = relativePath.substring(relativePath.lastIndexOf("\\") + 1);
        // 构建File
        File file = new File(localPath + relativePath);
        // ok表示Http协议中的状态 200
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        // 内容长度
        builder.contentLength(file.length());
        // application/octet-stream ： 二进制流数据（最常见的文件下载）
        builder.contentType(MediaType.APPLICATION_OCTET_STREAM);
        // 使用URLDecoder.decode对文件名进行解码
        filename = URLEncoder.encode(filename, "UTF-8");
        // 设置实际的响应文件名，告诉浏览器文件要用于【下载】、【保存】attachment 以附件形式
        // 不同的浏览器，处理方式不同，要根据浏览器版本进行区别判断
        if (userAgent.indexOf("MSIE") > 0) {
            // 如果是IE，只需要用UTF-8字符集进行URL编码即可
            builder.header("Content-Disposition", "attachment; filename=" + filename);
        } else {
            // 而FireFox、Chrome等浏览器，则需要说明编码的字符集
            // 注意filename后面有个*号，在UTF-8后面有两个单引号！
            builder.header("Content-Disposition", "attachment; filename*=UTF-8''" + filename);
        }

        return builder.body(FileUtils.readFileToByteArray(file));
    }
}
