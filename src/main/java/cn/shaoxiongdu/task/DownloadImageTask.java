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

import cn.shaoxiongdu.bean.PostInfo;
import cn.shaoxiongdu.utils.Log;
import lombok.AllArgsConstructor;

/**
 * @author: email@shaoxiongdu.cn
 * @date: 2024/5/14 : 17:54
 * @describe:
 */
@AllArgsConstructor
public class DownloadImageTask implements Runnable{
    
    private PostInfo postInfo;
    
    @Override
    public void run() {
        
        Log.info("\t开始下载帖子 {}, 其中图片{}张", postInfo.getId(), postInfo.getImageList().size());
        
        
    
    }
}
