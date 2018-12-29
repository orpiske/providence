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

package org.providence.common.exceptions;

public class ProvidenceException extends RuntimeException {
    public ProvidenceException() {
        super();
    }

    public ProvidenceException(final String message, final Object...args) {
        super(String.format(message, args));
    }

    public ProvidenceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ProvidenceException(final Throwable cause) {
        super(cause);
    }

    protected ProvidenceException(final String message, final Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
