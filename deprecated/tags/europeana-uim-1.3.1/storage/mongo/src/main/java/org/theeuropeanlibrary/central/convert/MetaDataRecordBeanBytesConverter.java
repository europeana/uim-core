package org.theeuropeanlibrary.central.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.theeuropeanlibrary.repository.convert.Converter;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;
import eu.europeana.uim.store.bean.MetaDataRecordBean;

/**
 * Converts between provider bean and byte array.
 * 
 * @author Andreas Juffinger <andreas.juffinger@kb.nl>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Feb 18, 2011
 */
@SuppressWarnings("rawtypes")
public class MetaDataRecordBeanBytesConverter extends Converter<byte[], MetaDataRecordBean> {
    /**
     * Single convenience instance of a string type converter
     */
    public static final MetaDataRecordBeanBytesConverter INSTANCE = new MetaDataRecordBeanBytesConverter();

    private static final int                             ID       = 1;
    private static final int                             FIELD    = 2;

    private ConverterFactory                             converterFactory;

    /**
     * Creates a new instance of this class.
     */
    private MetaDataRecordBeanBytesConverter() {
        converterFactory = new CombinedConverterFactory(ObjectModelConverterFactory.INSTANCE,
                AuthorityModelConverterFactory.INSTANCE);
    }

    @Override
    public Class<byte[]> getEncodeType() {
        return byte[].class;
    }

    @Override
    public Class<MetaDataRecordBean> getDecodeType() {
        return MetaDataRecordBean.class;
    }

    @Override
    public MetaDataRecordBean<Long> decode(byte[] data) {
        byte[] decompressedData;
        if (data[0] == 120) {
            decompressedData = decompress(data);
        } else {
            decompressedData = data;
        }

        MetaDataRecordBean<Long> bean = new MetaDataRecordBean<Long>();
        ByteArrayInputStream bin = new ByteArrayInputStream(decompressedData);
        CodedInputStream input = CodedInputStream.newInstance(bin);
        int tag;
        try {
            while ((tag = input.readTag()) != 0) {
                int field = WireFormat.getTagFieldNumber(tag);
                switch (field) {
                case ID:
                    bean.setId(input.readFixed64());
                    break;
                case FIELD:
                    ByteString b = input.readBytes();
                    readField(bean, b.toByteArray());
                    break;
                default:
                    break;
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException("Could not read MetaDataRecordBean from byte array!", e);
        } finally {
            try {
                bin.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close input stream!", e);
            }
        }
        return bean;
    }

    private byte[] decompress(byte[] data) {
        Inflater decompressor = new Inflater();
        decompressor.setInput(data);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);

        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            try {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            } catch (DataFormatException e) {
            }
        }
        try {
            bos.close();
        } catch (IOException e) {
        }

        byte[] decompressedData = bos.toByteArray();
        return decompressedData;
    }

    @SuppressWarnings("unchecked")
    @Override
    public byte[] encode(MetaDataRecordBean bean) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CodedOutputStream output = CodedOutputStream.newInstance(bout);
        try {
            output.writeFixed64(ID, (Long)bean.getId());
            Set<TKey<?, ?>> keys = bean.getAvailableKeys();
            if (keys != null && keys.size() > 0) {
                for (TKey<?, ?> key : keys) {
                    List<QualifiedValue<?>> values = bean.getQualifiedValues(key);
                    byte[] bytes = writeField(key, values);
                    ByteString b = ByteString.copyFrom(bytes);
                    output.writeBytes(FIELD, b);
                }
            }
            output.flush();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Could not write MetaDataRecordBean '" + bean.getId() +
                                       "' to byte array!", e);
        } finally {
            try {
                bout.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close output stream!", e);
            }
        }

        byte[] uncompressed = bout.toByteArray();
        byte[] compressed = compress(uncompressed);
        return compressed;
    }

    private byte[] compress(byte[] uncompressed) {
        Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);

        compressor.setInput(uncompressed);
        compressor.finish();

        ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressed.length);

        byte[] buf = new byte[1024];
        while (!compressor.finished()) {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (IOException e) {
        }

        byte[] compressed = bos.toByteArray();
        return compressed;
    }

    private static final int FIELD_KEY   = 1;
    private static final int FIELD_ENTRY = 2;

    @SuppressWarnings("unchecked")
    private <NS, T> void readField(MetaDataRecordBean<Long> bean, byte[] data) {
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        CodedInputStream input = CodedInputStream.newInstance(bin);
        int tag;
        try {
            TKey<NS, T> key = null;
            List<byte[]> encodedValues = new ArrayList<byte[]>();
            while ((tag = input.readTag()) != 0) {
                int field = WireFormat.getTagFieldNumber(tag);
                switch (field) {
                case FIELD_KEY:
                    if (key != null) { throw new RuntimeException(
                            "Only one key is allowed per field!"); }
                    key = (TKey<NS, T>)TKey.fromString(input.readString());
                    break;
                case FIELD_ENTRY:
                    ByteString b = input.readBytes();
                    encodedValues.add(b.toByteArray());
                    break;
                default:
                    break;
                }
            }

            List<QualifiedValue<T>> values = new ArrayList<QualifiedValue<T>>(encodedValues.size());
            for (byte[] encodedValue : encodedValues) {
                readEntry(key, values, encodedValue);
            }
            if (key != null) {
                bean.setValue(key, values);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read field from byte array!", e);
        } finally {
            try {
                bin.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close input stream!", e);
            }
        }
    }

    private byte[] writeField(TKey<?, ?> key, List<QualifiedValue<?>> values) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CodedOutputStream output = CodedOutputStream.newInstance(bout);
        try {
            output.writeString(FIELD_KEY, key.toString());
            for (QualifiedValue<?> value : values) {
                byte[] bytes = writeEntry(value);
                ByteString b = ByteString.copyFrom(bytes);
                output.writeBytes(FIELD_ENTRY, b);
            }
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("Could not write key '" + key.getFullName() +
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

    private static final int FIELD_ENTRY_ORDER     = 1;
    private static final int FIELD_ENTRY_VALUE     = 2;
    private static final int FIELD_ENTRY_QUALIFIER = 3;

    @SuppressWarnings("unchecked")
    private <NS, T> void readEntry(TKey<NS, T> key, List<QualifiedValue<T>> values, byte[] data) {
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        CodedInputStream input = CodedInputStream.newInstance(bin);
        int tag;
        try {
            int order = -1;
            T value = null;
            Set<Enum<?>> qualifiers = new HashSet<Enum<?>>();
            while ((tag = input.readTag()) != 0) {
                int field = WireFormat.getTagFieldNumber(tag);
                switch (field) {
                case FIELD_ENTRY_ORDER:
                    order = input.readInt32();
                    break;
                case FIELD_ENTRY_VALUE:
                    Converter converter = converterFactory.getConverter(key);
                    if (converter != null) {
                        try {
                            ByteString b = input.readBytes();
                            value = (T)converter.decode(b.toByteArray());
                        } catch (Exception e) {
                            throw new RuntimeException("Error decoding a " +
                                                       key.getType().getName(), e);
                        }
                    } else {
                        BaseTypeEncoder encoder = converterFactory.getBaseTypeEncoder(key.getType());
                        if (encoder == null)
                            throw new RuntimeException("Unsupported class for metadata record: " +
                                                       key.getType().getName());
                        value = (T)encoder.decode(input);
                    }
                    break;
                case FIELD_ENTRY_QUALIFIER:
                    String encoded = input.readString();
                    String[] split = encoded.split("@");
                    Class<? extends Enum> type;
                    try {
                        type = (Class<? extends Enum>)Class.forName(split[0]);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Could not convert encoded enum '" + encoded +
                                                   "'!", e);
                    }
                    Enum<?> enumValue = Enum.valueOf(type, split[1]);
                    qualifiers.add(enumValue);
                    break;
                default:
                    break;
                }
            }
            if (value != null) {
                if (order < 0) { throw new RuntimeException("No valued index read"); }
                values.add(new QualifiedValue<T>(value, qualifiers, order));
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read field entry (" +
                                       key.getType().getSimpleName() + ") from byte array (" +
                                       data.length + " bytes)!", e);
        } finally {
            try {
                bin.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close input stream!", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private byte[] writeEntry(QualifiedValue<?> value) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CodedOutputStream output = CodedOutputStream.newInstance(bout);
        try {
            output.writeInt32(FIELD_ENTRY_ORDER, value.getOrderIndex());
            Converter converter = converterFactory.getConverter(value.getValue().getClass());
            if (converter != null) {
                ByteString b = ByteString.copyFrom((byte[])converter.encode(value.getValue()));
                output.writeBytes(FIELD_ENTRY_VALUE, b);
            } else {
                BaseTypeEncoder encoder = converterFactory.getBaseTypeEncoder(value.getValue().getClass());
                if (encoder != null) {
                    encoder.encode(FIELD_ENTRY_VALUE, value.getValue(), output);
                } else {
                    // No encoder or converter, the value is not persisted
                }
            }
            Set<Enum<?>> qualifiers = value.getQualifiers();
            for (Enum<?> qualifier : qualifiers) {
                String qualifierEncoded = qualifier.getClass().getName() + "@" + qualifier.name();
                output.writeString(FIELD_ENTRY_QUALIFIER, qualifierEncoded);
            }
            output.flush();
        } catch (Exception e) {
            throw new RuntimeException("Could not write qualified value to byte array!", e);
        } finally {
            try {
                bout.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close output stream!", e);
            }
        }
        return bout.toByteArray();
    }

    /**
     * Indicates if it is able to convert a given class
     * 
     * @param cls
     * @return can or cannot
     */
    public boolean canConvert(Class<?> cls) {
        Converter converter = converterFactory.getConverter(cls);
        if (converter != null) {
            return true;
        } else {
            BaseTypeEncoder encoder = converterFactory.getBaseTypeEncoder(cls);
            if (encoder != null) {
                return true;
            } else 
                return false;
        }
    }
    
//    public static void main(String[] args) throws Exception {
//        String longText = "asd;flkajlk ;ajskd jfakls d;fkla sd;lkf jas;lkfd jas;lkdjf ;laskdjf ;laskdj f;laskdjf;laskjd f;lkasdj f;lkasjd f;alksjd fl;kasjd flkasjdl kfjas;ldk fjas;k djfl;as kdjfl;kasj dlk;fjaskdjfiweruqpwoivm nxcmjpaiwepruqidnvzxmcnvp wieru";
//        
//        byte[] byteText = longText.getBytes();
//        
//        
//        
//        ByteArrayOutputStream bout = new ByteArrayOutputStream();
//        GZIPOutputStream gzip = new GZIPOutputStream(bout);
//        bout.write(byteText);
//        
//        gzip.flush();
//        gzip.close();
//        bout.close();
//        
//        byte[] gzipConverted = bout.toByteArray();
//        
//        
//        
//        
//        Deflater compressor = new Deflater();
//        compressor.setLevel(Deflater.BEST_COMPRESSION);
//
//        compressor.setInput(byteText);
//        compressor.finish();
//
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(byteText.length);
//
//        byte[] buf = new byte[1024];
//        while (!compressor.finished()) {
//            int count = compressor.deflate(buf);
//            bos.write(buf, 0, count);
//        }
//        try {
//            bos.close();
//        } catch (IOException e) {
//        }
//
//        byte[] compressed = bos.toByteArray();
//        
//        
//        
//        System.out.println(byteText.length);
//        System.out.println(gzipConverted.length);
//        System.out.println(compressed.length);
//        
//    }
}