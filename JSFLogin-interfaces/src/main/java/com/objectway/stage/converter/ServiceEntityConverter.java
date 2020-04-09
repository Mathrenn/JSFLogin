package com.objectway.stage.converter;

import java.util.List;

public interface ServiceEntityConverter<T, E>{
	public T entityToService(E e);
	public E serviceToEntity(T t);
	public List<T> entityListToServiceList(List<E> el);
	public List<E> serviceListToEntityList(List<T> tl);
}
