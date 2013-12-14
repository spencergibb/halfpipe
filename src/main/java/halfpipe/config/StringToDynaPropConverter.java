package halfpipe.config;

import com.google.common.base.Throwables;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;

/**
 * User: spencergibb
 * Date: 12/12/13
 * Time: 5:38 PM
 */
public class StringToDynaPropConverter implements ConditionalGenericConverter {

    private Field resolvableType;
    private ConversionService conversionService;

    public StringToDynaPropConverter(ConversionService conversionService) {
        System.out.println("converter");
        this.conversionService = conversionService;
        try {
            resolvableType = TypeDescriptor.class.getDeclaredField("resolvableType");
        } catch (NoSuchFieldException e) {
            Throwables.propagate(e);
        }
        resolvableType.setAccessible(true);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, DynaProp.class));
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        try {
            TypeDescriptor generic = getGenericType(targetType);
            return this.conversionService.canConvert(sourceType, generic);
        } catch (Exception e) {
            e.printStackTrace();  //TODO: implement catch
        }
        return false;
    }

    private TypeDescriptor getGenericType(TypeDescriptor targetType) throws NoSuchFieldException, IllegalAccessException {
        Field resolvableType = this.resolvableType;
        ResolvableType r = (ResolvableType) resolvableType.get(targetType);
        ResolvableType generic = r.getGeneric(0);
        return TypeDescriptor.valueOf(generic.getRawClass());
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        try {
            TypeDescriptor typeDescriptor = getGenericType(targetType);
            return new DynaProp(conversionService.convert(source, sourceType, typeDescriptor));
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        return null;
    }
}
