/*
 * Copyright 2011 Mikhail Lopatkin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bitbucket.mlopatkin.android.liblogcat.filters;

import org.bitbucket.mlopatkin.android.liblogcat.LogRecord;

/**
 * Performs filtering based on the message of the record.
 */
public class MessageFilter extends AbstractFilter implements LogRecordFilter {

    private String messageText;

    public MessageFilter(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public boolean include(LogRecord record) {
        String message = record.getMessage();
        return message.contains(messageText);
    }

    @Override
    public String toString() {
        return "Message containing '" + messageText + "'";
    }

    @Override
    protected void dumpFilter(FilterData data) {
        data.message = messageText;
    }

}
