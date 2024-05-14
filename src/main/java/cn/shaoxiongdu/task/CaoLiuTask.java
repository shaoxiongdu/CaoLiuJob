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

import cn.hutool.http.HttpUtil;
import cn.shaoxiongdu.bean.PostInfo;
import cn.shaoxiongdu.database.Database;
import cn.shaoxiongdu.utils.Log;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: email@shaoxiongdu.cn
 * @date: 2024/5/14 : 15:17
 * @describe:
 */
@AllArgsConstructor
public class CaoLiuTask implements Runnable{
    
    private final String url;
    
    @Override
    public void run() {
        Log.info(url);
//        List<PostInfo> postInfoList = getPostInfo();
//        Log.info("{} {}", url, postInfoList.size());
//        Database.addAll(postInfoList);
    }
    
    private List<PostInfo> getPostInfo() {
        
        return Jsoup.parse(HttpUtil.get(url)).getElementById("ajaxtable")
                .getElementsByClass("tr3 t_one tac")
                .stream()
                .filter(e -> e.childNodeSize() == 11)
                .map(this::tr2Post)
                .collect(Collectors.toList());
                
    }
    
    private PostInfo tr2Post(Element trElement) {
        
        PostInfo postInfo = new PostInfo();
        postInfo.setParentUrl(url);
        
        try {
            Element titleElement = trElement.getElementsByTag("h3").get(0);
            postInfo.setTitle(titleElement.text());
            postInfo.setId(trElement.getElementsByTag("h3").get(0).getElementsByTag("a").get(0).attr("id"));
            
            postInfo.setAuthor(trElement.getElementsByClass("bl").get(0).text());
            
            postInfo.setIsTopMark(!trElement.getElementsByClass("s3").isEmpty());
            
            postInfo.setReadings(trElement.getElementsByTag("td").get(0).text());
            
        }catch (Throwable t){
            Log.info(t.toString());
            t.printStackTrace();
            System.out.println(trElement);
            throw new RuntimeException();
        }
        
        
        return postInfo;
    }
}
