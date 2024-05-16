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

package cn.shaoxiongdu.constants;

/**
 * @author: email@shaoxiongdu.cn
 * @date: 2024/5/14 : 17:21
 * @describe:
 */
public interface Constants {
    
    String BASE_URL = "https://cl.2612x.xyz/";
    
    String POST_URL_ = "thread0806.php";
    
    String POST1_URL_TEMPLATE = BASE_URL + POST_URL_ + "?fid=16&page={}";
    String POST2_URL_TEMPLATE = BASE_URL + POST_URL_ + "?fid=8&page={}";
    
    int POST_MAX_PAGE = 100;
    int DOWNLOAD_THREAD_NUMBER = 10000;
    String WORK_SPACE_POSTS_DIR = System.getProperty("user.dir") + "/cao-liu/posts";

    String WORK_SPACE_IMAGES_DIR = System.getProperty("user.dir") + "/cao-liu/images/";
    String WORK_SPACE = System.getProperty("user.dir") + "/cao-liu";
    String POST_INFO_LIST_JSON_FILE_NAME = "post-list.json";

}
