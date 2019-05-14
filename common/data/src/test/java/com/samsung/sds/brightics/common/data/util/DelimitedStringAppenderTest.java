/*
    Copyright 2019 Samsung SDS
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.samsung.sds.brightics.common.data.util;

import org.junit.Test;

import com.samsung.sds.brightics.common.data.util.DelimitedStringAppender;

public class DelimitedStringAppenderTest {
    @Test
    public void testString(){
        DelimitedStringAppender appender = new DelimitedStringAppender("[", "]", ",");
        appender.append("My name is jb");
        appender.append("My role is r&d");
        appender.append("I wanna go home");
        System.out.println(appender);
    }
}
