package cn.shaoxiongdu;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.shaoxiongdu.constants.Constants;
import cn.shaoxiongdu.database.Database;
import cn.shaoxiongdu.task.DownloadPostTask;
import cn.shaoxiongdu.task.PostTask;
import cn.shaoxiongdu.utils.Log;

import javax.xml.crypto.Data;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    
    private static final ExecutorService postTaskExecutor = ThreadUtil.newFixedExecutor(Constants.POST_MAX_PAGE, "task-帖子-线程-", true);
    private static final ExecutorService downloadImageExecutor = ThreadUtil.newFixedExecutor(Constants.DOWNLOAD_THREAD_NUMBER, "task-download-线程-", true);
    
    public static void main(String[] args) throws InterruptedException {

        // 获取帖子列表
         handlerGetPost();
        
        // 下载
        handlerDownloadImage();
    }
    
    /**
     *  获取帖子
     * @throws InterruptedException
     */
    private static void handlerGetPost() throws InterruptedException {
        IntStream.range(0, Constants.POST_MAX_PAGE).forEach(page ->
                postTaskExecutor.submit(new PostTask(StrUtil.format(Constants.POST_URL_TEMPLATE, page))
        ));
        
        postTaskExecutor.shutdown();
        postTaskExecutor.awaitTermination(1, TimeUnit.DAYS);
        
    }
    
    private static void test() throws InterruptedException {
        PostTask task = new PostTask(StrUtil.format(Constants.POST_URL_TEMPLATE, 0));
        task.run();
        handlerDownloadImage();
    }
    
    /**
     * 下载图片
     * @throws InterruptedException
     */
    private static void handlerDownloadImage() throws InterruptedException {
        Log.info("帖子解析完成，开始下载... " + Database.getAllPostInfoList().size() + "个帖子");
        
        FileUtil.del(Constants.WORK_SPACE);
        
        FileUtil.mkdir(Constants.WORK_SPACE);
        FileUtil.mkdir(Constants.WORK_SPACE_IMAGES);

        Database.getAllPostInfoList().forEach(postInfo -> {
            downloadImageExecutor.submit(new DownloadPostTask(postInfo));
        });
        
        downloadImageExecutor.shutdown();
        downloadImageExecutor.awaitTermination(1, TimeUnit.DAYS);
    }

}
