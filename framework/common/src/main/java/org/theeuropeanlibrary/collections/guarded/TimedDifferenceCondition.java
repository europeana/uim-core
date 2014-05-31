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
 * A guarded condition that is based on a time difference.
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Aug 22, 2011
 */
public class TimedDifferenceCondition implements GuardCondition<Long> {

    /**
     * long difference
     */
    protected long difference = 1000;

    /**
     * Creates a new instance of this class.
     *
     * @param difference
     */
    public TimedDifferenceCondition(long difference) {
        this.difference = difference;
    }

    @Override
    public boolean allow(GuardedKey<Long> object) {
        long compare = System.currentTimeMillis();
        long objkey = object.getConditionValue();

        long diff = compare - objkey;

        boolean allow = diff > difference;
        return allow;
    }
}
