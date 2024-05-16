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

package cn.shaoxiongdu.database;

import cn.shaoxiongdu.bean.PostInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: email@shaoxiongdu.cn
 * @date: 2024/5/14 : 16:04
 * @describe:
 */
@Data
public class Database {
    
    @Setter
    @Getter
    private static List<PostInfo> allPostInfoList = new ArrayList<>();
    
    public static synchronized void addAll(List<PostInfo> postInfoList) {
        allPostInfoList.addAll(postInfoList);
    }
    
}
