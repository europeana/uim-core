/* GuardedQueueTest.java - created on Mar 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.collections.guarded;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 16, 2011
 */
public class GuardedQueueTest {

    /**
     * @throws MalformedURLException
     * @throws InterruptedException
     */
    @Test
    public void testSimpel() throws MalformedURLException, InterruptedException {
        GuardedQueue queue = new GuardedQueue(new TimedDifferenceCondition(100), 20, 20, 400, true);
        queue.offer(new GuardedUrl(new URL("http://www.test0.org/what-ever.html")));
        queue.offer(new GuardedUrl(new URL("http://www.test1.org/what-ever.html")));
        queue.offer(new GuardedUrl(new URL("http://www.test2.org/what-ever.html")));
        queue.offer(new GuardedUrl(new URL("http://www.test3.org/what-ever.html")));

        queue.offer(new GuardedUrl(new URL("http://www.test0.org/what-ever.html")));
        queue.offer(new GuardedUrl(new URL("http://www.test1.org/what-ever.html")));
        queue.offer(new GuardedUrl(new URL("http://www.test2.org/what-ever.html")));
        queue.offer(new GuardedUrl(new URL("http://www.test3.org/what-ever.html")));

        queue.offer(new GuardedUrl(new URL("http://www.test0.org:80/what-ever.html")));
        queue.offer(new GuardedUrl(new URL("http://www.test0.org:81/what-ever.html")));
        queue.offer(new GuardedUrl(new URL("http://www.test0.org:82/what-ever.html")));
        queue.offer(new GuardedUrl(new URL("http://www.test0.org:80/what-ever.html")));

        queue.offer(new GuardedUrl(new URL("http://www.test0.org/what-ever.html")));
        queue.offer(new GuardedUrl(new URL("http://www.test1.org/what-ever.html")));
        queue.offer(new GuardedUrl(new URL("http://www.test2.org/what-ever.html")));
        queue.offer(new GuardedUrl(new URL("http://www.test3.org/what-ever.html")));

        Map<GuardedKey<Long>, Long> last = new HashMap<GuardedKey<Long>, Long>();
        while (!queue.isEmpty()) {
            GuardedUrl guarded = (GuardedUrl)queue.poll(10, TimeUnit.MILLISECONDS);

            if (guarded != null) {
                GuardedKey<Long> key = guarded.getGuardKey();
                if (last.containsKey(key)) {
                    assertTrue(System.currentTimeMillis() - last.get(key) >= 100);
                }

                last.put(key, System.currentTimeMillis());
//                System.out.println("Got (" + System.currentTimeMillis() + "): " + guarded);
            }
        }
    }
}
