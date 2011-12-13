package org.theeuropeanlibrary.uim.repository.convert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.theeuropeanlibrary.repository.convert.Converter;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;

import eu.europeana.uim.store.bean.ExecutionBean;

/**
 * Converts between collection bean and byte array.
 * 
 * @author Andreas Juffinger <andreas.juffinger@kb.nl>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Feb 18, 2011
 */
@SuppressWarnings("rawtypes")
public class ExecutionBeanBytesConverter extends Converter<byte[], ExecutionBean> {
    /**
     * logging variable for execution converter
     */
    private static final Logger                      log       = Logger.getLogger(ExecutionBeanBytesConverter.class.getName());

    /**
     * Single convenience instance of a string type converter
     */
    public static final ExecutionBeanBytesConverter INSTANCE  = new ExecutionBeanBytesConverter();

    private static final int                        ID        = 1;
    private static final int                        ACTIVE    = 2;
    private static final int                        STARTTIME = 3;
    private static final int                        ENDTIME   = 4;
    private static final int                        CANCEL    = 5;
    private static final int                        WORKFLOW  = 6;
    private static final int                        NAME      = 7;
    private static final int                        SUCCESS   = 8;
    private static final int                        ERROR     = 9;
    private static final int                        PROCESSED = 10;
    private static final int                        VALUES    = 11;
    private static final int                        LOGFILE   = 12;

    @Override
    public Class<byte[]> getEncodeType() {
        return byte[].class;
    }

    @Override
    public Class<ExecutionBean> getDecodeType() {
        return ExecutionBean.class;
    }

    @Override
    public ExecutionBean<Long> decode(byte[] data) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");

        ExecutionBean<Long> bean = new ExecutionBean<Long>();
        CodedInputStream input = CodedInputStream.newInstance(data);
        int tag;
        try {
            while ((tag = input.readTag()) != 0) {
                int field = WireFormat.getTagFieldNumber(tag);
                switch (field) {
                case ID:
                    bean.setId(input.readFixed64());
                    break;
                case ACTIVE:
                    bean.setActive(input.readBool());
                    break;
                case STARTTIME:
                    String time = input.readString();
                    if (time != null && time.trim().length() > 0) {
                        try {
                            bean.setStartTime(df.parse(time));
                        } catch (Throwable t) {
                            log.severe("Failed to parse start: <" + time + "> as date.");
                        }
                    }
                    break;
                case ENDTIME:
                    time = input.readString();
                    if (time != null && time.length() > 0) {
                        try {
                            bean.setEndTime(df.parse(time));
                        } catch (Throwable t) {
                            log.severe("Failed to parse start: <" + time + "> as date.");
                        }
                    }
                    break;
                case CANCEL:
                    bean.setCanceled(input.readBool());
                    break;
                case WORKFLOW:
                    bean.setWorkflow(input.readString());
                    break;
                case NAME:
                    bean.setName(input.readString());
                    break;
                case SUCCESS:
                    bean.setSuccessCount(input.readInt32());
                    break;
                case ERROR:
                    bean.setFailureCount(input.readInt32());
                    break;
                case PROCESSED:
                    bean.setProcessedCount(input.readInt32());
                    break;
                case VALUES:
                    Map<String, String> map = CollectionBeanBytesConverter.decodeMap(input.readString());
                    for (Entry<String, String> entry : map.entrySet()) {
                        bean.putValue(entry.getKey(), entry.getValue());
                    }
                    break;
                case LOGFILE:
                    bean.setLogFile(input.readString());
                    break;
                default:
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read ProviderBean from byte array!", e);
        } finally {
        }
        return bean;
    }

    @SuppressWarnings("unchecked")
    @Override
    public byte[] encode(ExecutionBean data) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CodedOutputStream output = CodedOutputStream.newInstance(bout);
        try {
            if (data.getId() != null) {
                output.writeFixed64(ID, (Long)data.getId());
            }
            output.writeBool(ACTIVE, data.isActive());

            if (data.getStartTime() != null) {
                output.writeString(STARTTIME, df.format(data.getStartTime()));
            }

            if (data.getEndTime() != null) {
                output.writeString(ENDTIME, df.format(data.getEndTime()));
            }

            output.writeBool(CANCEL, data.isCanceled());

            if (data.getWorkflow() != null) {
                output.writeString(WORKFLOW, data.getWorkflow());
            }
            if (data.getName() != null) {
                output.writeString(NAME, data.getName());
            }
            output.writeInt32(SUCCESS, data.getSuccessCount());
            output.writeInt32(ERROR, data.getFailureCount());
            output.writeInt32(PROCESSED, data.getProcessedCount());

            if (data.values() != null && !data.values().isEmpty()) {
                output.writeString(VALUES, CollectionBeanBytesConverter.encodeMap(data.values()));
            }

            if (data.getLogFile() != null) {
                output.writeString(LOGFILE, data.getLogFile());
            }
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("Could not write ProviderBean '" + data.getId() +
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
