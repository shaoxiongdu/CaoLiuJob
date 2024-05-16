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

import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.HttpUtil;
import cn.shaoxiongdu.constants.Constants;
import cn.shaoxiongdu.utils.Log;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: email@shaoxiongdu.cn
 * @date: 2024/5/14 : 15:17
 * @describe:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInfo {
    
    private static final List<String> TITLE_WHITE_LIST = List.of(
      "技术","图床","官方", "會員", "评分","VPN", "版规", "教程"
    );
    
    private String parentUrl;
    private String href;
    private String id;
    private String title;
    private String author;
    private Boolean isTopMark;
    private Boolean isDamage;
    
    @AllArgsConstructor
    @Data
    public static class Image{
        private String remoteUrl;
        private String fileName;
        private String localUrl;
        private Boolean isDamage;
    }
    
    private List<Image> imageList = new ArrayList<>();
    
    public static PostInfo createFromHtmlTrElement(String parentUrl, Element trElement) {
        
        PostInfo postInfo = new PostInfo();
        try {
            postInfo.setParentUrl(parentUrl);
            postInfo.setIsDamage(false);
            Element titleElement = trElement.getElementsByTag("h3").get(0);
            postInfo.setTitle(titleElement.text());
            postInfo.setId(trElement.getElementsByTag("h3").get(0).getElementsByTag("a").get(0).attr("id"));
            postInfo.setAuthor(trElement.getElementsByClass("bl").get(0).text());
            postInfo.setIsTopMark(!trElement.getElementsByClass("s3").isEmpty());
            postInfo.setHref("https://cl.2612x.xyz/" + trElement.getElementsByTag("h3").get(0).getElementsByTag("a").attr("href"));
        }catch (Throwable t){
            t.printStackTrace();
            Log.info(PostInfo.class, trElement.toString());
            return null;
        }

        if (postInfo.isSkip()) return null;

        // 解析招聘列表
        postInfo.analysisContextList();
        Log.info(PostInfo.class, " 帖子解析完成【{}】 图片共{}张", postInfo.id, postInfo.getImageList().size());
        
        return postInfo;
    }
    
    public void analysisContextList() {
        
        if (isSkip()) return;
        
        String response = HttpUtil.get(this.href);
        Element rootElement = Jsoup.parse(response).getElementById("conttpc");
        if (ObjUtil.isEmpty(rootElement)) return;
        
        List<Image> imageList = rootElement.getElementsByTag("img").stream().map(element -> element.attr("ess-data"))
                .map(imageRemoteUrl -> {
                    
                    String imageFileName = imageRemoteUrl.substring(imageRemoteUrl.lastIndexOf("/") + 1, imageRemoteUrl.length());
                    
                    return new Image(imageRemoteUrl, imageFileName, Constants.WORK_SPACE_IMAGES_DIR + imageFileName, false);
                }).collect(Collectors.toList());
        this.imageList.addAll(imageList);
    }
    
    public boolean isSkip() {
        return TITLE_WHITE_LIST.stream().anyMatch(whileTitle -> title.contains(whileTitle));
    }

    public File getMdFile() {
        File file = new File(Constants.WORK_SPACE_POSTS_DIR + "/" + title + ".md");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

}
