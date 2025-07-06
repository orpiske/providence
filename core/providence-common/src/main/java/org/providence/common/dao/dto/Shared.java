/*
 * Copyright 2018 Otavio Rodolfo Piske
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.providence.common.dao.dto;

import java.util.Date;

public class Shared {
    private int sharedId;
    private long sharedHash;
    private String sharedSource;
    private String sharedFormat;
    private Date sharedDate;
    private String sharedText;

    public int getSharedId() {
        return sharedId;
    }

    public void setSharedId(int sharedId) {
        this.sharedId = sharedId;
    }

    public long getSharedHash() {
        return sharedHash;
    }

    public void setSharedHash(long sharedHash) {
        this.sharedHash = sharedHash;
    }

    public String getSharedSource() {
        return sharedSource;
    }

    public void setSharedSource(String sharedSource) {
        this.sharedSource = sharedSource;
    }

    public String getSharedFormat() {
        return sharedFormat;
    }

    public void setSharedFormat(String sharedFormat) {
        this.sharedFormat = sharedFormat;
    }

    public Date getSharedDate() {
        return sharedDate;
    }

    public void setSharedDate(Date sharedDate) {
        this.sharedDate = sharedDate;
    }

    public String getSharedText() {
        return sharedText;
    }

    public void setSharedText(String sharedText) {
        this.sharedText = sharedText;
    }
}
