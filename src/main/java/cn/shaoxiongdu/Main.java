package cn.shaoxiongdu;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.shaoxiongdu.constants.Constants;
import cn.shaoxiongdu.database.Database;
import cn.shaoxiongdu.task.CleanTask;
import cn.shaoxiongdu.task.DownloadPostTask;
import cn.shaoxiongdu.task.CrawlingPostTask;
import cn.shaoxiongdu.utils.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    
    private static final ExecutorService postTaskExecutor = ThreadUtil.newFixedExecutor(Constants.POST_MAX_PAGE, "task-爬取帖子-线程-", true);
    private static final ExecutorService downloadImageExecutor = ThreadUtil.newFixedExecutor(Constants.DOWNLOAD_THREAD_NUMBER, "task-下载帖子-线程-", true);
    
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
                postTaskExecutor.submit(new CrawlingPostTask(StrUtil.format(Constants.POST2_URL_TEMPLATE, page))
        ));
        
        postTaskExecutor.shutdown();
        postTaskExecutor.awaitTermination(1, TimeUnit.DAYS);
        
    }
    
    private static void test() throws InterruptedException {
        CrawlingPostTask task = new CrawlingPostTask(StrUtil.format(Constants.POST2_URL_TEMPLATE, 0));
        task.run();
        handlerDownloadImage();
    }
    
    /**
     * 下载图片
     * @throws InterruptedException
     */
    private static void handlerDownloadImage() throws InterruptedException {
        Log.info("帖子解析完成，开始下载... " + Database.getAllPostInfoList().size() + "个帖子");
        
        FileUtil.del(Constants.WORK_SPACE_POSTS_DIR);
        
        FileUtil.mkdir(Constants.WORK_SPACE_POSTS_DIR);
        FileUtil.mkdir(Constants.WORK_SPACE_IMAGES_DIR);

        Database.getAllPostInfoList().forEach(postInfo -> {
            downloadImageExecutor.submit(new DownloadPostTask(postInfo));
        });
        
        downloadImageExecutor.shutdown();
        downloadImageExecutor.awaitTermination(1, TimeUnit.DAYS);
    }

}
