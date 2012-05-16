/* ObjectModelConverterFactory.java - created on 7 de Jun de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.Link;
import org.theeuropeanlibrary.model.common.Numbering;
import org.theeuropeanlibrary.model.common.Text;
import org.theeuropeanlibrary.model.common.Title;
import org.theeuropeanlibrary.model.common.party.Family;
import org.theeuropeanlibrary.model.common.party.Meeting;
import org.theeuropeanlibrary.model.common.party.Organization;
import org.theeuropeanlibrary.model.common.party.Party;
import org.theeuropeanlibrary.model.common.party.Person;
import org.theeuropeanlibrary.model.common.spatial.BoundingBoxReferencedPlace;
import org.theeuropeanlibrary.model.common.spatial.GeoReferencedPlace;
import org.theeuropeanlibrary.model.common.spatial.NamedPlace;
import org.theeuropeanlibrary.model.common.spatial.SpatialEntity;
import org.theeuropeanlibrary.model.common.subject.Subject;
import org.theeuropeanlibrary.model.common.subject.TitleSubject;
import org.theeuropeanlibrary.model.common.subject.Topic;
import org.theeuropeanlibrary.model.common.time.HistoricalPeriod;
import org.theeuropeanlibrary.model.common.time.Instant;
import org.theeuropeanlibrary.model.common.time.Period;
import org.theeuropeanlibrary.model.common.time.TemporalTextualExpression;
import org.theeuropeanlibrary.model.tel.Edition;
import org.theeuropeanlibrary.model.tel.Facet;
import org.theeuropeanlibrary.model.tel.LabeledText;
import org.theeuropeanlibrary.model.tel.Metadata;
import org.theeuropeanlibrary.model.tel.ObjectModelRegistry;
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
 * @since 7 de Jun de 2011
 */
public final class ObjectModelConverterFactory implements ConverterFactory {
	
	/** 
	 *	Singleton
	 */
	public static final ObjectModelConverterFactory INSTANCE=new ObjectModelConverterFactory();
	
    /**
     * Creates a new instance of this class.
     */
    private ObjectModelConverterFactory()  {
    }
    
	@SuppressWarnings("rawtypes")
	private final Map<Class<?>, Converter> converters = new HashMap<Class<?>, Converter>() {
		{
			final Map<Class<?>, Converter> thisConverters = this;
			{ //Object model converters
				
				final ProxyConverter<Subject> subjectConverter = new ProxyConverter<Subject>();
				final ProxyConverter<TitleSubject> titleSubjectConverter = new ProxyConverter<TitleSubject>();
				
				final AnnotationBasedByteConverter<Instant> instantConverter = new AnnotationBasedByteConverter<Instant>(
						Instant.class, new HashMap<Integer,  FieldConverterInterface>() {
							{
								put(5, new ConverterBasedFieldConverter(subjectConverter));
							}
						});
				final AnnotationBasedByteConverter<Period> periodConverter = new AnnotationBasedByteConverter<Period>(
						Period.class,
						new HashMap<Integer, FieldConverterInterface>() {
							{
								put(1, new ConverterBasedFieldConverter(
										instantConverter));
								put(2, new ConverterBasedFieldConverter(
										instantConverter));
								put(5, new ConverterBasedFieldConverter(subjectConverter));
							}
						});
				final AnnotationBasedByteConverter<Identifier> identifierConverter = new AnnotationBasedByteConverter<Identifier>(
						Identifier.class, null);
				final AnnotationBasedByteConverter<TemporalTextualExpression> temporalTextConverter = new AnnotationBasedByteConverter<TemporalTextualExpression>(
						TemporalTextualExpression.class,
						new HashMap<Integer, FieldConverterInterface>() {
							{
								put(2, new CollectionFieldBufferConverter(
										identifierConverter));
								put(5, new ConverterBasedFieldConverter(subjectConverter));
							}
						});
				final AnnotationBasedByteConverter<NamedPlace> namedPlaceConverter = new AnnotationBasedByteConverter<NamedPlace>(
						NamedPlace.class,
						new HashMap<Integer, FieldConverterInterface>() {
							{
								put(1, new CollectionFieldBufferConverter(
										identifierConverter));
								put(8, new ConverterBasedFieldConverter(subjectConverter));
							}
						});
	
				put(Identifier.class, new AnnotationBasedByteConverter<Identifier>(
						Identifier.class, null));
	
				put(Edition.class, new AnnotationBasedByteConverter<Edition>(
						Edition.class, null));
				put(Facet.class, new AnnotationBasedByteConverter<Facet>(
						Facet.class, null));
				// put(Identifier.class, identifierConverter);
				put(LabeledText.class,
						new AnnotationBasedByteConverter<LabeledText>(
								LabeledText.class, null));
				put(Link.class, new AnnotationBasedByteConverter<Link>(Link.class,
						null));
				put(Numbering.class, new AnnotationBasedByteConverter<Numbering>(
						Numbering.class, null));
				put(Text.class, new AnnotationBasedByteConverter<Text>(Text.class,
						null));
				put(Title.class, new AnnotationBasedByteConverter<Title>(
						Title.class, new HashMap<Integer, FieldConverterInterface>() {
							{
								put(3, new ConverterBasedFieldConverter(titleSubjectConverter));
							}
						}));
	
				put(BoundingBoxReferencedPlace.class,
						new AnnotationBasedByteConverter<BoundingBoxReferencedPlace>(
								BoundingBoxReferencedPlace.class,
								new HashMap<Integer, FieldConverterInterface>() {
									{
										put(1, new CollectionFieldBufferConverter(
												identifierConverter));
										put(8, new ConverterBasedFieldConverter(subjectConverter));
									}
								}));
				put(GeoReferencedPlace.class,
						new AnnotationBasedByteConverter<GeoReferencedPlace>(
								GeoReferencedPlace.class,
								new HashMap<Integer, FieldConverterInterface>() {
									{
										put(1, new CollectionFieldBufferConverter(
												identifierConverter));
										put(8, new ConverterBasedFieldConverter(subjectConverter));
									}
								}));
				put(NamedPlace.class, namedPlaceConverter);
				put(SpatialEntity.class,
						new AnnotationBasedByteConverter<SpatialEntity>(
								SpatialEntity.class,
								new HashMap<Integer, FieldConverterInterface>() {
									{
										put(1, new CollectionFieldBufferConverter(
												identifierConverter));
										put(8, new ConverterBasedFieldConverter(subjectConverter));
									}
								}));
	
				put(Family.class, new AnnotationBasedByteConverter<Family>(
						Family.class,
						new HashMap<Integer, FieldConverterInterface>() {
							{
								put(2, new CollectionFieldBufferConverter(
										identifierConverter));
								put(9, new ConverterBasedFieldConverter(subjectConverter));
								put(10, new MultiClassFieldConverter() {
									{
										add(BoundingBoxReferencedPlace.class,
												thisConverters.get(BoundingBoxReferencedPlace.class));
										add(GeoReferencedPlace.class, thisConverters.get(GeoReferencedPlace.class));
										add(NamedPlace.class, thisConverters.get(NamedPlace.class));
										add(SpatialEntity.class, thisConverters.get(SpatialEntity.class));
									}
								});
							}
						}));
				put(Meeting.class, new AnnotationBasedByteConverter<Meeting>(
						Meeting.class,
						new HashMap<Integer, FieldConverterInterface>() {
							{
								put(2, new CollectionFieldBufferConverter(
										identifierConverter));
								put(4, new ConverterBasedFieldConverter(
										instantConverter));
								put(9, new ConverterBasedFieldConverter(subjectConverter));
								put(10, new MultiClassFieldConverter() {
									{
										add(BoundingBoxReferencedPlace.class,
												thisConverters.get(BoundingBoxReferencedPlace.class));
										add(GeoReferencedPlace.class, thisConverters.get(GeoReferencedPlace.class));
										add(NamedPlace.class, thisConverters.get(NamedPlace.class));
										add(SpatialEntity.class, thisConverters.get(SpatialEntity.class));
									}
								});
							}
						}));
				put(Organization.class,
						new AnnotationBasedByteConverter<Organization>(
								Organization.class,
								new HashMap<Integer, FieldConverterInterface>() {
									{
										put(2, new CollectionFieldBufferConverter(
												identifierConverter));
										put(9, new ConverterBasedFieldConverter(subjectConverter));
										put(10, new MultiClassFieldConverter() {
											{
												add(BoundingBoxReferencedPlace.class,
														thisConverters.get(BoundingBoxReferencedPlace.class));
												add(GeoReferencedPlace.class, thisConverters.get(GeoReferencedPlace.class));
												add(NamedPlace.class, thisConverters.get(NamedPlace.class));
												add(SpatialEntity.class, thisConverters.get(SpatialEntity.class));
											}
										});
									}
								}));
				put(Party.class, new AnnotationBasedByteConverter<Party>(
						Party.class,
						new HashMap<Integer, FieldConverterInterface>() {
							{
								put(2, new CollectionFieldBufferConverter(
										identifierConverter));
								put(9, new ConverterBasedFieldConverter(subjectConverter));
								put(10, new MultiClassFieldConverter() {
									{
										add(BoundingBoxReferencedPlace.class,
												thisConverters.get(BoundingBoxReferencedPlace.class));
										add(GeoReferencedPlace.class, thisConverters.get(GeoReferencedPlace.class));
										add(NamedPlace.class, thisConverters.get(NamedPlace.class));
										add(SpatialEntity.class, thisConverters.get(SpatialEntity.class));
									}
								});
							}
						}));
				put(Person.class, new AnnotationBasedByteConverter<Person>(
						Person.class,
						new HashMap<Integer, FieldConverterInterface>() {
							{
								put(2, new CollectionFieldBufferConverter(
										identifierConverter));
								put(3, new ConverterBasedFieldConverter(
										periodConverter));
								put(4, new ConverterBasedFieldConverter(
										periodConverter));
								put(9, new ConverterBasedFieldConverter(subjectConverter));
								put(10, new MultiClassFieldConverter() {
									{
										add(BoundingBoxReferencedPlace.class,
												thisConverters.get(BoundingBoxReferencedPlace.class));
										add(GeoReferencedPlace.class, thisConverters.get(GeoReferencedPlace.class));
										add(NamedPlace.class, thisConverters.get(NamedPlace.class));
										add(SpatialEntity.class, thisConverters.get(SpatialEntity.class));
									}
								});
							}
						}));
	
				put(Instant.class, instantConverter);
				put(Period.class, periodConverter);
				put(TemporalTextualExpression.class, temporalTextConverter);
				put(HistoricalPeriod.class,
						new AnnotationBasedByteConverter<HistoricalPeriod>(
								HistoricalPeriod.class,
								new HashMap<Integer, FieldConverterInterface>() {
									{
										put(2, new MultiClassFieldConverter() {
											{
												add(Instant.class, instantConverter);
												add(Period.class, periodConverter);
												add(TemporalTextualExpression.class, temporalTextConverter);
											}
										});
										put(3, new ConverterBasedFieldConverter(
												namedPlaceConverter));
										put(4, new CollectionFieldBufferConverter(
												identifierConverter));
										put(5, new ConverterBasedFieldConverter(subjectConverter));
									}
								}));
	
				//Nuno: commented 26th July 2011 - Enums are supported always. so this should not be necessary 
	//			put(Language.class, new BaseTypeEncoderBasedConverter(
	//					new EnumEncoder(Language.class)));
	//
	//			put(Status.class, new BaseTypeEncoderBasedConverter(
	//					new EnumEncoder(Status.class)));
	
				//Nuno: commented 26th July 2011 - converter for string is supported always. so this should not be necessary 
	//			put(String.class,
	//					new BaseTypeEncoderBasedConverter(
	//							BaseTypeFieldConverterFactory
	//									.newFieldEncoderFor(String.class)));
	
				put(Metadata.class, new AnnotationBasedByteConverter<Metadata>(
						Metadata.class, null));
	
				put(Topic.class, new AnnotationBasedByteConverter<Topic>(
						Topic.class,
						new HashMap<Integer, FieldConverterInterface>() {
							{
								put(3, new CollectionFieldBufferConverter(
										identifierConverter));
								
								put(5, new MultiClassFieldConverter() {
									{
										add(BoundingBoxReferencedPlace.class,
												thisConverters.get(BoundingBoxReferencedPlace.class));
										add(GeoReferencedPlace.class, thisConverters.get(GeoReferencedPlace.class));
										add(NamedPlace.class, thisConverters.get(NamedPlace.class));
										add(SpatialEntity.class, thisConverters.get(SpatialEntity.class));
									}
								});
								put(6, new MultiClassFieldConverter() {
									{
										add(Instant.class, instantConverter);
										add(Period.class, periodConverter);
										add(TemporalTextualExpression.class, temporalTextConverter);
									}
								});
								put(9, new MultiClassFieldConverter() {
									{
										add(Instant.class, instantConverter);
										add(Period.class, periodConverter);
										add(TemporalTextualExpression.class, temporalTextConverter);
										add(HistoricalPeriod.class, thisConverters.get(HistoricalPeriod.class) );
									}
								});
								put(10, new MultiClassFieldConverter() {
									{
										add(BoundingBoxReferencedPlace.class,
												thisConverters.get(BoundingBoxReferencedPlace.class));
										add(GeoReferencedPlace.class, thisConverters.get(GeoReferencedPlace.class));
										add(NamedPlace.class, thisConverters.get(NamedPlace.class));
										add(SpatialEntity.class, thisConverters.get(SpatialEntity.class));
									}
								});
							}
						}));
				
				AnnotationBasedByteConverter<Subject> actualSubjectConverter=
					new AnnotationBasedByteConverter<Subject>(
							Subject.class,
							new HashMap<Integer, FieldConverterInterface>() {
								{
									put(9, new MultiClassFieldConverter() {
										{
											add(Instant.class, instantConverter);
											add(Period.class, periodConverter);
											add(TemporalTextualExpression.class, temporalTextConverter);
											add(HistoricalPeriod.class, thisConverters.get(HistoricalPeriod.class) );
										}
									});
									put(10, new MultiClassFieldConverter() {
										{
											add(BoundingBoxReferencedPlace.class,
													thisConverters.get(BoundingBoxReferencedPlace.class));
											add(GeoReferencedPlace.class, thisConverters.get(GeoReferencedPlace.class));
											add(NamedPlace.class, thisConverters.get(NamedPlace.class));
											add(SpatialEntity.class, thisConverters.get(SpatialEntity.class));
											
										}
									});
								}
							});
				
				AnnotationBasedByteConverter<TitleSubject> actualTitleSubjectConverter=
					new AnnotationBasedByteConverter<TitleSubject>(
							TitleSubject.class,
						new HashMap<Integer, FieldConverterInterface>() {
							{
								put(9, new MultiClassFieldConverter() {
									{
										add(Instant.class, instantConverter);
										add(Period.class, periodConverter);
										add(TemporalTextualExpression.class, temporalTextConverter);
										add(HistoricalPeriod.class, thisConverters.get(HistoricalPeriod.class) );
									}
								});
								put(10, new MultiClassFieldConverter() {
									{
										add(BoundingBoxReferencedPlace.class,
												thisConverters.get(BoundingBoxReferencedPlace.class));
										add(GeoReferencedPlace.class, thisConverters.get(GeoReferencedPlace.class));
										add(NamedPlace.class, thisConverters.get(NamedPlace.class));
										add(SpatialEntity.class, thisConverters.get(SpatialEntity.class));
									
									}
								});
								put(13, new CollectionFieldBufferConverter(
										identifierConverter));
								put(11, new MultiClassFieldConverter() {
									{
										add(Instant.class, instantConverter);
										add(Period.class, periodConverter);
										add(TemporalTextualExpression.class, temporalTextConverter);
										add(HistoricalPeriod.class, thisConverters.get(HistoricalPeriod.class) );
									}
								});
							}
						});
	
				titleSubjectConverter.setSerializer(actualTitleSubjectConverter); 
				subjectConverter.setSerializer(actualSubjectConverter); 
			}
			
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
			for (Field field : ObjectModelRegistry.class.getDeclaredFields()) {
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
     * Gets a BaseTypeEncoder for a given Class
     * 
     * @param <T>
     * @param encodedClass
     * @return BaseTypeEncoder
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> BaseTypeEncoder<T> getBaseTypeEncoder(Class<T> encodedClass) {
        return baseTypeEncoders.get(encodedClass);
    }

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
}