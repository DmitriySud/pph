/*
 * Copyright 2018 SerVB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.servb.pph.util.helpertype;

/**
 * Emulates constant arrays from C++. Provides the access by any enumeration extending EnumC.
 *
 * @author SerVB
 * @param <E> Type of elements. Has to be immutable type.
 */
public final class SimpleConstArray<E> extends ConstArray<EnumC, E> {

    /**
     * Constructs the object.
     *
     * @param storage Elements.
     */
    public SimpleConstArray(final E[] storage) {
        super(storage);
    }

}