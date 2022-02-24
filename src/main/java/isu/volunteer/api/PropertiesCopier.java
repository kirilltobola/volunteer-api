package isu.volunteer.api;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

@Component
public class PropertiesCopier<T, S> {

    public T copyProperties(T entity, S dto) {
        BeanUtils.copyProperties(dto, entity, getNullProperties(dto));
        return entity;
    }

    private String[] getNullProperties(S dto) {
        final BeanWrapper beanWrapper = new BeanWrapperImpl(dto);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        List<String> nullProperties = new ArrayList<>();

        for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if(beanWrapper.getPropertyValue(propertyDescriptor.getName()) == null) {
                nullProperties.add(propertyDescriptor.getName());
            }
        }
        return nullProperties.toArray(new String[nullProperties.size()]);
    }
}
