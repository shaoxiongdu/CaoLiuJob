package cn.shaoxiongdu;


import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.shaoxiongdu.bean.PostInfo;
import cn.shaoxiongdu.database.Database;
import cn.shaoxiongdu.task.CaoLiuTask;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    
    
    private static final int TOTAL_PAGE = 779;
    
    private static final String URL_TEMPLATE = "https://cl.2612x.xyz/thread0806.php?fid=16&page={}";
    
    private static final ExecutorService executorService = ThreadUtil.newFixedExecutor(TOTAL_PAGE, "task-", true);
    
    public static void main(String[] args) throws InterruptedException {
        
        IntStream.range(0, TOTAL_PAGE)
                .forEach(page -> executorService.submit(new CaoLiuTask(StrUtil.format(URL_TEMPLATE, page))));
        
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);
        
        List<PostInfo> allPostInfoList = Database.getAllPostInfoList();
        System.out.println();
        
        //        CaoLiuTask task = new CaoLiuTask(StrUtil.format(URL_TEMPLATE, 0));
        //        task.run();
        
    }
}
