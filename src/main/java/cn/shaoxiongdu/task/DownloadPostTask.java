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

package cn.shaoxiongdu.task;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.shaoxiongdu.bean.PostInfo;
import cn.shaoxiongdu.constants.Constants;
import cn.shaoxiongdu.utils.Log;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

/**
 * @author: email@shaoxiongdu.cn
 * @date: 2024/5/14 : 17:54
 * @describe:
 */
@AllArgsConstructor
public class DownloadPostTask implements Runnable{
    
    private PostInfo postInfo;
    
    @Override
    public void run() {
        
        Log.info(" 开始下载帖子 {}, 图片共{}张", postInfo.getId(), postInfo.getImageUrlList().size() - 1);

        File mdFile = postInfo.getMdFile();

        for (int i = 0; i < postInfo.getImageUrlList().size(); i++) {
            String url = postInfo.getImageUrlList().get(i);

            String imageFileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            Log.info("  开始下载图片  帖子[{} 第 {} 张照片 共{}张 ", postInfo.getId(),i + 1, postInfo.getImageUrlList().size() - 1);

            try {
                File imageFile = new File(Constants.WORK_SPACE_IMAGES_DIR + "/" + imageFileName);
                FileOutputStream imageOutput = new FileOutputStream(imageFile);
                HttpUtil.download(url, imageOutput, true);
                imageOutput.close();
                
                if (FileUtil.isEmpty(imageFile)) {
                    FileUtil.del(imageFile);
                    Log.info("帖子{}的图片{}格式损坏，已删除", postInfo.getId(), imageFile.getAbsolutePath());
                    continue;
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            
            FileUtil.appendString(StrUtil.format("- ![](./images/{})\n", imageFileName), mdFile, Charset.defaultCharset());
            Log.info("帖子 {} 的第 {} 张照片下载完成 共{}张 ", postInfo.getId(),i, postInfo.getImageUrlList().size() - 1);
            
        }
        if (FileUtil.isEmpty(mdFile)) {
            FileUtil.del(mdFile);
            Log.info("帖子{}已损坏，已删除", postInfo.getId());
        }

    }
}
