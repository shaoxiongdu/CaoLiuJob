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

import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.HttpUtil;
import cn.shaoxiongdu.bean.PostInfo;
import cn.shaoxiongdu.database.Database;
import cn.shaoxiongdu.utils.Log;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: email@shaoxiongdu.cn
 * @date: 2024/5/14 : 15:17
 * @describe:
 */
@AllArgsConstructor
public class CrawlingPostTask implements Runnable{
    
    private final String url;
    
    @Override
    public void run() {
        Log.info(this.getClass(), "开始解析页面  url: {} ", url);
        List<PostInfo> postInfoList = getPostInfo();
        Log.info(this.getClass(), "页面解析完成 {} 共{}条帖子", url, postInfoList.size());
        Database.addAll(postInfoList);
    }
    
    private List<PostInfo> getPostInfo() {
        
        return Jsoup.parse(HttpUtil.get(url)).getElementById("ajaxtable")
                .getElementsByClass("tr3 t_one tac")
                .stream()
                .filter(e -> e.childNodeSize() == 11)
                .map(trElement -> PostInfo.createFromHtmlTrElement(url, trElement))
                .filter(ObjUtil::isNotEmpty)
                .collect(Collectors.toList());
                
    }
    
}
