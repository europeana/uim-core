/* ArrayUtils.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/** Utility functions to split arrays into a list of smaller arrays
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 20, 2011
 */
public class ArrayUtils {

    /** 
     * Split arrays into a list of smaller arrays
     * @param data the array to be splitted
     * @param batchsize the default size of the batch
     * @return a list of arrays
     */
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


    /** 
     * Split arrays into a list of smaller arrays
     * @param data the array to be splitted
     * @param batchsize the default size of the batch
     * @return a list of arrays
     */
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


    /** 
     * Split arrays into a list of smaller arrays
     * @param data the array to be splitted
     * @param batchsize the default size of the batch
     * @return a list of arrays
     */  
    public static List<Long[]> batches(Long[] data, int batchsize) {
        ArrayList<Long[]> result = new ArrayList<Long[]>();

        int batches = (int)Math.ceil(1.0 * data.length / batchsize);
        for (int i = 0; i < batches; i++) {
            int end = Math.min(data.length, (i + 1) * batchsize);
            int start = i * batchsize;

            Long[] batch = new Long[end - start];
            System.arraycopy(data, start, batch, 0, end - start);
            result.add(batch);
        }
        return result;
    }


    /** 
     * Split arrays into a list of smaller arrays
     * @param data the array to be splitted
     * @param batchsize the default size of the batch
     * @return a list of arrays
     */
    public static List<Integer[]> batches(Integer[] data, int batchsize) {
        ArrayList<Integer[]> result = new ArrayList<Integer[]>();

        int batches = (int)Math.ceil(1.0 * data.length / batchsize);
        for (int i = 0; i < batches; i++) {
            int end = Math.min(data.length, (i + 1) * batchsize);
            int start = i * batchsize;

            Integer[] batch = new Integer[end - start];
            System.arraycopy(data, start, batch, 0, end - start);
            result.add(batch);
        }
        return result;
    }

    /** 
     * Split arrays into a list of smaller arrays
     * @param <T> 
     * @param data the array to be splitted
     * @param batchsize the default size of the batch
     * @return a list of arrays
     */
    public  static <T> List<T[]> batches(T[] data, int batchsize) {
        ArrayList<T[]> result = new ArrayList<T[]>();

        int batches = (int)Math.ceil(1.0 * data.length / batchsize);
        for (int i = 0; i < batches; i++) {
            int end = Math.min(data.length, (i + 1) * batchsize);
            int start = i * batchsize;

            @SuppressWarnings("unchecked")
            T[] batch = (T[])Array.newInstance(data[0].getClass(), end-start);
            System.arraycopy(data, start, batch, 0, end - start);
            result.add(batch);
        }
        return result;
    }

}
