package org.theeuropeanlibrary.uim.repository.convert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;

import org.theeuropeanlibrary.repository.convert.Converter;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;

import eu.europeana.uim.store.bean.RequestBean;

/**
 * Converts between provider bean and byte array.
 * 
 * @author Andreas Juffinger <andreas.juffinger@kb.nl>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Feb 18, 2011
 */
@SuppressWarnings("rawtypes")
public class RequestBeanBytesConverter extends Converter<byte[], RequestBean> {
    /**
     * Single convenience instance of a string type converter
     */
    public static final RequestBeanBytesConverter INSTANCE = new RequestBeanBytesConverter();

    private static final int                      ID       = 1;
    private static final int                      DATE     = 2;
    private static final int                      FROM     = 3;
    private static final int                      TILL     = 4;
    private static final int                      FAILED   = 5;
    private static final int                      VALUES   = 6;

    @Override
    public Class<byte[]> getEncodeType() {
        return byte[].class;
    }

    @Override
    public Class<RequestBean> getDecodeType() {
        return RequestBean.class;
    }

    @Override
    public RequestBean<Long> decode(byte[] data) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");

        RequestBean<Long> bean = new RequestBean<Long>();
        CodedInputStream input = CodedInputStream.newInstance(data);
        int tag;
        try {
            while ((tag = input.readTag()) != 0) {
                int field = WireFormat.getTagFieldNumber(tag);
                switch (field) {
                case ID:
                    bean.setId(input.readFixed64());
                    break;
                case DATE:
                    bean.setDate(df.parse(input.readString()));
                    break;
                case FROM:
                    String string = input.readString();
                    if (string != null && string.length() > 0) {
                        bean.setDataFrom(df.parse(input.readString()));
                    }
                    break;
                case TILL:
                    string = input.readString();
                    if (string != null && string.length() > 0) {
                        bean.setDataTill(df.parse(input.readString()));
                    }
                    break;
                case FAILED:
                    bean.setFailed(input.readBool());
                    break;
                case VALUES:
                    Map<String, String> map = CollectionBeanBytesConverter.decodeMap(input.readString());
                    for (Entry<String, String> entry : map.entrySet()) {
                        bean.putValue(entry.getKey(), entry.getValue());
                    }
                    input.resetSizeCounter();
                    break;
                default:
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read RequestBean from byte array!", e);
        } catch (ParseException e) {
            throw new RuntimeException("Could not parse date!", e);
        } finally {
        }
        return bean;
    }

    @SuppressWarnings("unchecked")
    @Override
    public byte[] encode(RequestBean data) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CodedOutputStream output = CodedOutputStream.newInstance(bout);
        try {
            if (data.getId() != null) {
                output.writeFixed64(ID, (Long)data.getId());
            }
            if (data.getDate() != null) {
                output.writeString(DATE, df.format(data.getDate()));
            }
            if (data.getDataFrom() != null) {
                output.writeString(FROM, df.format(data.getDataFrom()));
            }
            if (data.getDataFrom() != null) {
                output.writeString(TILL, df.format(data.getDataTill()));
            }
            output.writeBool(FAILED, data.isFailed());
            if (data.values() != null && !data.values().isEmpty()) {
                output.writeString(VALUES, CollectionBeanBytesConverter.encodeMap(data.values()));
            }
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("Could not write RequestBean '" + data.getId() +
                                       "' to byte array!", e);
        } finally {
            try {
                bout.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close output stream!", e);
            }
        }
        return bout.toByteArray();
    }
}