/*
 *   Copyright ShaoxiongDu <email@shaoxiongdu.cn>
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package cn.shaoxiongdu;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.shaoxiongdu.bean.PostInfo;
import cn.shaoxiongdu.constants.Constants;
import cn.shaoxiongdu.database.Database;
import cn.shaoxiongdu.task.CrawlingPostTask;
import cn.shaoxiongdu.task.DownloadPostTask;
import cn.shaoxiongdu.utils.Log;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author: email@shaoxiongdu.cn
 * @date: 2024/5/16 : 10:24
 * @describe:
 */
public class CaoLiuJob {
    
    private static final ExecutorService postTaskExecutor = ThreadUtil.newFixedExecutor(Constants.POST_MAX_PAGE, "task-爬取帖子-线程-", true);
    private static final ExecutorService downloadImageExecutor = ThreadUtil.newFixedExecutor(Constants.DOWNLOAD_THREAD_NUMBER, "task-下载帖子-线程-", true);
    
    static void run() throws InterruptedException {

        // 爬
        handlerCrawlingPost();

        // 下
        handlerDownloadPost();

        // 校准数据
        calibrationData();

        // 存
        savePostInfoList2Json();
    }
    
    /**
     * 帖子信息保存到json文件
     */
    private static void savePostInfoList2Json() {
        FileUtil.appendString(JSONUtil.toJsonStr(Database.getAllPostInfoList()), Constants.WORK_SPACE + "/" + Constants.POST_INFO_LIST_JSON_FILE_NAME, StandardCharsets.UTF_8);
    }
    
    /**
     * 校验数据 去除空帖子
     */
    private static void calibrationData() {
        
        List<PostInfo> successPostList = new ArrayList<>(Database.getAllPostInfoList().size());
        int count = 0;
        for (PostInfo postInfo : Database.getAllPostInfoList()) {
            if (postInfo.getIsDamage()) {
                count++;
                continue;
            }
            
            List<PostInfo.Image> successImageList = postInfo.getImageList().stream().filter(image -> !image.getIsDamage())
                    .collect(Collectors.toList());
            postInfo.setImageList(successImageList);
            
            successPostList.add(postInfo);
        }
        Log.info(CaoLiuJob.class, "数据校准完成 废弃帖子{}个" + count);
        Database.setAllPostInfoList(successPostList);
    }
    
    /**
     * 爬帖子
     * @throws InterruptedException
     */
    private static void handlerCrawlingPost() throws InterruptedException {
        handlerCrawlingPost(Constants.POST1_URL_TEMPLATE);
        handlerCrawlingPost(Constants.POST2_URL_TEMPLATE);
        postTaskExecutor.shutdown();
        postTaskExecutor.awaitTermination(1, TimeUnit.DAYS);
    }
    
    private static void handlerCrawlingPost(String postUrl) throws InterruptedException {
        IntStream.range(0, Constants.POST_MAX_PAGE).forEach(page ->
                postTaskExecutor.submit(new CrawlingPostTask(StrUtil.format(postUrl, page))
                ));
    }
    
    /**
     * 下载图片
     * @throws InterruptedException
     */
    private static void handlerDownloadPost() throws InterruptedException {
        Log.info(CaoLiuJob.class, "帖子解析完成，开始下载... " + Database.getAllPostInfoList().size() + "个帖子");
        
        FileUtil.del(Constants.WORK_SPACE);
        
        FileUtil.mkdir(Constants.WORK_SPACE_POSTS_DIR);
        FileUtil.mkdir(Constants.WORK_SPACE_IMAGES_DIR);
        
        Database.getAllPostInfoList().forEach(postInfo -> {
            downloadImageExecutor.submit(new DownloadPostTask(postInfo));
        });
        
        downloadImageExecutor.shutdown();
        downloadImageExecutor.awaitTermination(1, TimeUnit.DAYS);
    }
    
}
