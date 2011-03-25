/* ArrayUtils.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 20, 2011
 */
public class ArrayUtils {

    public static List<long[]> batches(long[] data, int batchsize) {
        ArrayList<long[]> result = new ArrayList<long[]>();

        int batches = (int)Math.ceil(1.0 * data.length / batchsize);
        for (int i = 0; i < batches; i++) {
            int end = Math.min(data.length, (i + 1) * batchsize);
            int start = i * batchsize;

            long[] batch = new long[end - start];
            System.arraycopy(data, start, batch, 0, end - start);
            result.add(batch);
        }
        return result;
    }

    public static List<String[]> batches(String[] data, int batchsize) {
        ArrayList<String[]> result = new ArrayList<String[]>();

        int batches = (int)Math.ceil(1.0 * data.length / batchsize);
        for (int i = 0; i < batches; i++) {
            int end = Math.min(data.length, (i + 1) * batchsize);
            int start = i * batchsize;

            String[] batch = new String[end - start];
            System.arraycopy(data, start, batch, 0, end - start);
            result.add(batch);
        }
        return result;
    }

}
