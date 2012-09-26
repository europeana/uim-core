/* ObjectModelConverterFactoryNew.INSTANCE.java - created on 7 de Jun de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.Title;
import org.theeuropeanlibrary.model.common.party.Family;
import org.theeuropeanlibrary.model.common.party.Meeting;
import org.theeuropeanlibrary.model.common.party.Organization;
import org.theeuropeanlibrary.model.common.party.Party;
import org.theeuropeanlibrary.model.common.party.Person;
import org.theeuropeanlibrary.model.common.qualifier.Country;
import org.theeuropeanlibrary.model.common.spatial.NamedPlace;
import org.theeuropeanlibrary.model.common.subject.Topic;
import org.theeuropeanlibrary.model.tel.AuthorityObjectModelRegistry;
import org.theeuropeanlibrary.model.tel.authority.Coordinates;
import org.theeuropeanlibrary.model.tel.authority.FeatureClass;
import org.theeuropeanlibrary.model.tel.authority.NamedPlaceNameForm;
import org.theeuropeanlibrary.model.tel.authority.Occurrences;
import org.theeuropeanlibrary.model.tel.authority.OrganizationNameForm;
import org.theeuropeanlibrary.model.tel.authority.PersonNameForm;
import org.theeuropeanlibrary.model.tel.authority.TopicNameForm;
import org.theeuropeanlibrary.model.tel.authority.UpdateFromDataSource;
import org.theeuropeanlibrary.repository.convert.Converter;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * This class defines generic functionality to get specific converters for given names, classes and
 * base types. It is used in the {@link MetaDataRecordBeanBytesConverter} to convert generic object
 * models represented in the {@link MetaDataRecord}. However, not this is a class holding only
 * functions, registration and validation is done in registries, this class is only used to get all
 * kinds of converters.
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 7 de Jun de 2011
 */
public final class AuthorityModelConverterFactory implements ConverterFactory {
	/** 
	 *	Singleton
	 */
	public static final AuthorityModelConverterFactory INSTANCE=new AuthorityModelConverterFactory();
	
    private AuthorityModelConverterFactory()  {
    }
    
	@SuppressWarnings("rawtypes")
	private final Map<Class<?>, Converter> converters = new HashMap<Class<?>, Converter>() {
		{
			final Map<Class<?>, Converter> thisConverters = this;

			thisConverters.put(PersonNameForm.class, new AnnotationBasedByteConverter<PersonNameForm>(
					PersonNameForm.class,
					new HashMap<Integer, FieldConverterInterface>() {
						{
							put(1, new MultiClassFieldConverter() {
								{
									add(Person.class,
											ObjectModelConverterFactory.INSTANCE
													.getConverter(Person.class));
									add(Organization.class,
											ObjectModelConverterFactory.INSTANCE
													.getConverter(Organization.class));
									add(Meeting.class,
											ObjectModelConverterFactory.INSTANCE
													.getConverter(Meeting.class));
									add(Party.class,
											ObjectModelConverterFactory.INSTANCE
													.getConverter(Party.class));
									add(Family.class,
											ObjectModelConverterFactory.INSTANCE
											.getConverter(Family.class));
									
								}
							});
							put(2,
									new CollectionFieldBufferConverter(
											new BaseTypeEncoderBasedConverter(
													BaseTypeFieldConverterFactory
															.newFieldEncoderFor(String.class))));
						}
					}));

			thisConverters.put(OrganizationNameForm.class, new AnnotationBasedByteConverter<OrganizationNameForm>(
					OrganizationNameForm.class,
					new HashMap<Integer, FieldConverterInterface>() {
						{
							put(1, new MultiClassFieldConverter() {
								{
									add(Person.class,
											ObjectModelConverterFactory.INSTANCE
													.getConverter(Person.class));
									add(Organization.class,
											ObjectModelConverterFactory.INSTANCE
													.getConverter(Organization.class));
									add(Meeting.class,
											ObjectModelConverterFactory.INSTANCE
													.getConverter(Meeting.class));
									add(Party.class,
											ObjectModelConverterFactory.INSTANCE
													.getConverter(Party.class));
									add(Family.class,
											ObjectModelConverterFactory.INSTANCE
											.getConverter(Family.class));
								}
							});
							put(2,
									new CollectionFieldBufferConverter(
											new BaseTypeEncoderBasedConverter(
													BaseTypeFieldConverterFactory
															.newFieldEncoderFor(String.class))));
						}
					}));

			thisConverters.put(NamedPlaceNameForm.class, new AnnotationBasedByteConverter<NamedPlaceNameForm>(
					NamedPlaceNameForm.class,
					new HashMap<Integer, FieldConverterInterface>() {
						{
							put(1, new ConverterBasedFieldConverter(ObjectModelConverterFactory.INSTANCE
									.getConverter(NamedPlace.class)));
							put(2,
									new CollectionFieldBufferConverter(
											new BaseTypeEncoderBasedConverter(
													BaseTypeFieldConverterFactory
															.newFieldEncoderFor(String.class))));
						}
					}));
			
			thisConverters.put(Country.class, BaseTypeFieldConverterFactory
					.newConverterFor(Country.class));

			thisConverters.put(Identifier.class, ObjectModelConverterFactory.INSTANCE.getConverter(Identifier.class));

			final AnnotationBasedByteConverter<Occurrences> occurrencesConverter = new AnnotationBasedByteConverter<Occurrences>(
					Occurrences.class,
					new HashMap<Integer, FieldConverterInterface>() {
						{
							put(1, new MultiClassFieldConverter() {
								{
									add(Title.class,
											ObjectModelConverterFactory.INSTANCE
											.getConverter(Title.class));
									add(Identifier.class,
											ObjectModelConverterFactory.INSTANCE
													.getConverter(Identifier.class));
									add(String.class,
											BaseTypeFieldConverterFactory
											.newConverterFor(String.class));
								}
							});
						}
					});
			thisConverters.put(Occurrences.class, occurrencesConverter);

			thisConverters.put(UpdateFromDataSource.class, 
					new AnnotationBasedByteConverter<UpdateFromDataSource>(UpdateFromDataSource.class, null));
			
			thisConverters.put(Coordinates.class, 
					new AnnotationBasedByteConverter<Coordinates>(Coordinates.class, null));
			
			thisConverters.put(TopicNameForm.class, 
					new AnnotationBasedByteConverter<TopicNameForm>(TopicNameForm.class,
					new HashMap<Integer, FieldConverterInterface>() {
						{
							put(1, new ConverterBasedFieldConverter(ObjectModelConverterFactory.INSTANCE
									.getConverter(Topic.class)));
							put(2,
									new CollectionFieldBufferConverter(
											new BaseTypeEncoderBasedConverter(
													BaseTypeFieldConverterFactory
															.newFieldEncoderFor(String.class))));
						}
					}));

			thisConverters.put(FeatureClass.class, BaseTypeFieldConverterFactory
					.newConverterFor(FeatureClass.class));
			
		}
			
	};

	@SuppressWarnings({ "rawtypes", "cast", "unchecked" })
	private final Map<Class<?>, BaseTypeEncoder> baseTypeEncoders = new HashMap<Class<?>, BaseTypeEncoder>() {
		{
			put(String.class, new StringEncoder());
			put(Boolean.class, new BooleanEncoder());
			put(Date.class, new DateEncoder());
			put(Integer.class, new IntegerEncoder());
			put(Long.class, new LongEncoder());
			put(Short.class, new ShortEncoder());
			put(Double.class, new DoubleEncoder());
			put(Float.class, new FloatEncoder());
			
			//add also support for Enums in the object models
	        for (Field field : AuthorityObjectModelRegistry.class.getDeclaredFields()) {
	            try {
	                Object object = field.get(null);
	                if (object instanceof TKey) {
	                    TKey k = (TKey)object;
	                    if(k.getType().isEnum()) {
	            			put(k.getType(), new EnumEncoder((Class<? extends Enum>)k.getType()));
	                    }
	                }
	            } catch (IllegalArgumentException e) {
	                throw new RuntimeException(e);
	            } catch (IllegalAccessException e) {
					//Ok, we are not supposed to get it. just ignore it
	            }
	        }
		}
	};

    /**
     * Gets a Converter for a given Class
     * 
     * @param <T>
     * @param convertedClass
     * @return Converter
     */
    @Override
	@SuppressWarnings("unchecked")
    public <T> Converter<byte[], T> getConverter(Class<T> convertedClass) {
        return converters.get(convertedClass);
    }

    /**
     * Gets a Converter for a given TKey
     * 
     * @param <NS>
     * @param <T>
     * @param tKey
     * @return Converter
     */
    @Override
	@SuppressWarnings("unchecked")
    public <NS, T> Converter<byte[], T> getConverter(TKey<NS, T> tKey) {
        return converters.get(tKey.getType());
    }

	@Override
	public Set<Class<?>> getSupportedClasses() {
		return converters.keySet();
	}

	@Override
	public Set<Class<?>> getSupportedBaseTypeEncoders() {
		return baseTypeEncoders.keySet();
	}


	@Override
    @SuppressWarnings("unchecked")
	public <T> BaseTypeEncoder<T> getBaseTypeEncoder(Class<T> encodedClass) {
		return baseTypeEncoders.get(encodedClass);
	}
}