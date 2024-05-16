package cn.shaoxiongdu;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.shaoxiongdu.constants.Constants;
import cn.shaoxiongdu.database.Database;
import cn.shaoxiongdu.task.DownloadPostTask;
import cn.shaoxiongdu.task.CrawlingPostTask;
import cn.shaoxiongdu.utils.Log;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        CaoLiuJob.run();
    }
    

}
