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

package cn.shaoxiongdu.bean;

import cn.shaoxiongdu.utils.Log;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;

/**
 * @author: email@shaoxiongdu.cn
 * @date: 2024/5/14 : 15:17
 * @describe:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInfo {
    private String parentUrl;
    private String href;
    private String id;
    private String title;
    private String author;
    private Boolean isTopMark;
    
    public static PostInfo createFromHtmlTrElement(String parentUrl, Element trElement) {
        
        PostInfo postInfo = new PostInfo();
        try {
            postInfo.setParentUrl(parentUrl);
            Element titleElement = trElement.getElementsByTag("h3").get(0);
            postInfo.setTitle(titleElement.text());
            postInfo.setId(trElement.getElementsByTag("h3").get(0).getElementsByTag("a").get(0).attr("id"));
            postInfo.setAuthor(trElement.getElementsByClass("bl").get(0).text());
            postInfo.setIsTopMark(!trElement.getElementsByClass("s3").isEmpty());
            postInfo.setHref("https://cl.2612x.xyz/" + trElement.getElementsByTag("h3").get(0).getElementsByTag("a").attr("href"));
        }catch (Throwable t){
            t.printStackTrace();
            Log.info(trElement.toString());
            return null;
        }
        return postInfo;
    }
}
