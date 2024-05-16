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
import cn.shaoxiongdu.constants.Constants;
import cn.shaoxiongdu.utils.Log;

import java.io.File;
import java.util.Arrays;


public class CleanTask implements Runnable{
    
    private final String IMAGE_DIR = Constants.WORK_SPACE_IMAGES_DIR;
    private final String POST_DIR = Constants.WORK_SPACE_POSTS_DIR;
    
    @Override
    public void run() {
        Log.info("开始清理目录任务");
        
        cleanEmptyFile(IMAGE_DIR);
        cleanEmptyFile(POST_DIR);
        
    }
    
    private void cleanEmptyFile(String rootDir) {
        Log.info("\t清理目录开始: 【{}】", rootDir);
        
        int count = 0;
        for (File file : FileUtil.ls(rootDir)) {
            if (!FileUtil.isEmpty(file)) continue;
            
            Log.info("\t\t空文件： 【{}】", file.getAbsolutePath());
            FileUtil.del(file);
            count++;
        }
        
        Log.info("\t清理目录结束: 【{}】 清理{}个文件", rootDir, count);
    }

}
