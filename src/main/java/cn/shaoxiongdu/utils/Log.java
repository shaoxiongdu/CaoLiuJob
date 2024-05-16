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

package cn.shaoxiongdu.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;

/**
 * @author: email@shaoxiongdu.cn
 * @date: 2024/5/14 : 15:00
 * @describe:
 */
public class Log {
    
    private static final cn.hutool.log.Log log = LogFactory.get();
    
    public static void info(String template, Object... args) {
        log.info(Thread.currentThread().getName() + "\t => \t" + StrUtil.format(template, args));
    }
    
}
