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
    
    String POST_URL_TEMPLATE = BASE_URL + POST_URL_ + "?fid=16&page={}";
    
    int POST_MAX_PAGE = 10;
    int DOWNLOAD_THREAD_NUMBER = 10000;
    String WORK_SPACE = System.getProperty("user.dir") + "/posts";

    String WORK_SPACE_IMAGES = System.getProperty("user.dir") + "/posts/images";

}
