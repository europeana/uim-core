/*
 * Copyright 2001-2007 by Andreas Juffinger, comtoo.org       
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
package org.theeuropeanlibrary.collections.guarded;

/**
 * <p>
 * </p>
 * 
 * @author ajuffing
 * @param <T>
 *            the condition value type
 * @created Mar 1, 2005
 * 
 */
public interface GuardedKey<T> extends Comparable<GuardedKey<T>> {

    /**
     * Tests for equal guard keys. This should return true if the elements should share the same
     * queue.
     */
    @Override
    public boolean equals(Object o);

    /**
     * Returns if the given object has the same or different value. This value is used for priority
     * queue.
     */
    @Override
    public int compareTo(GuardedKey<T> o);

    /**
     * @return the condition value
     */
    public T getConditionValue();

    /**
     * inform keay about a successful delivery
     */
    public void delivered();

}
