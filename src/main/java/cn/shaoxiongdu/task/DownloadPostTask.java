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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        
        Log.info("\t开始下载帖子 {}, 其中图片{}张", postInfo.getId(), postInfo.getContextList().size() - 1);

        File mdFile = postInfo.getMdFile();

        for (int i = 0; i < postInfo.getContextList().size(); i++) {
            String url = postInfo.getContextList().get(i);

            String imageFileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            Log.info("\t\t开始下载帖子 {} 的第 {} 张照片 共{}张 ", postInfo.getId(),i, postInfo.getContextList().size() - 1);

            try {
                FileOutputStream imageOutput = new FileOutputStream(Constants.WORK_SPACE_IMAGES + "/" + imageFileName);
                HttpUtil.download(url, imageOutput, true);
                imageOutput.close();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            FileUtil.appendString(StrUtil.format("- ![](./images/{})\n", imageFileName), mdFile, Charset.defaultCharset());
            Log.info("\t\t帖子 {} 的第 {} 张照片下载完成 共{}张 ", postInfo.getId(),i, postInfo.getContextList().size() - 1);
        }

    }
}
