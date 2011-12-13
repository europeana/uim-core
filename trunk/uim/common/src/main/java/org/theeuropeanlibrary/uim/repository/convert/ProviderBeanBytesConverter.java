package org.theeuropeanlibrary.uim.repository.convert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.theeuropeanlibrary.repository.convert.Converter;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;

import eu.europeana.uim.store.bean.ProviderBean;

/**
 * Converts between provider bean and byte array.
 * 
 * @author Andreas Juffinger <andreas.juffinger@kb.nl>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Feb 18, 2011
 */
@SuppressWarnings("rawtypes")
public class ProviderBeanBytesConverter extends Converter<byte[], ProviderBean> {
    /**
     * Single convenience instance of a string type converter
     */
    public static final ProviderBeanBytesConverter INSTANCE   = new ProviderBeanBytesConverter();

    private static final int                       ID         = 1;
    private static final int                       MNEMONIC   = 2;
    private static final int                       NAME       = 3;
    private static final int                       AGGREGATOR = 4;
    private static final int                       OAIBASE    = 5;
    private static final int                       OAIMETA    = 6;
    private static final int                       VALUES     = 7;

    @Override
    public Class<byte[]> getEncodeType() {
        return byte[].class;
    }

    @Override
    public Class<ProviderBean> getDecodeType() {
        return ProviderBean.class;
    }

    @Override
    public ProviderBean<Long> decode(byte[] data) {
        ProviderBean<Long> bean = new ProviderBean<Long>();
        CodedInputStream input = CodedInputStream.newInstance(data);
        int tag;
        try {
            while ((tag = input.readTag()) != 0) {
                int field = WireFormat.getTagFieldNumber(tag);
                switch (field) {
                case ID:
                    bean.setId(input.readFixed64());
                    break;
                case MNEMONIC:
                    bean.setMnemonic(input.readString());
                    break;
                case NAME:
                    bean.setName(input.readString());
                    break;
                case AGGREGATOR:
                    bean.setAggregator(input.readBool());
                    break;
                case OAIBASE:
                    bean.setOaiBaseUrl(input.readString());
                    break;
                case OAIMETA:
                    bean.setOaiMetadataPrefix(input.readString());
                    break;
                case VALUES:
                    Map<String, String> map = CollectionBeanBytesConverter.decodeMap(input.readString());
                    for (Entry<String, String> entry : map.entrySet()) {
                        bean.putValue(entry.getKey(), entry.getValue());
                    }
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
    public byte[] encode(ProviderBean data) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CodedOutputStream output = CodedOutputStream.newInstance(bout);
        try {
            if (data.getId() != null) {
                output.writeFixed64(ID, (Long)data.getId());
            }
            if (data.getMnemonic() != null) {
                output.writeString(MNEMONIC, data.getMnemonic());
            }
            if (data.getName() != null) {
                output.writeString(NAME, data.getName());
            }
            output.writeBool(AGGREGATOR, data.isAggregator());
            if (data.getOaiBaseUrl() != null) {
                output.writeString(OAIBASE, data.getOaiBaseUrl());
            }
            if (data.getOaiMetadataPrefix() != null) {
                output.writeString(OAIMETA, data.getOaiMetadataPrefix());
            }
            if (data.values() != null && !data.values().isEmpty()) {
                output.writeString(VALUES, CollectionBeanBytesConverter.encodeMap(data.values()));
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
