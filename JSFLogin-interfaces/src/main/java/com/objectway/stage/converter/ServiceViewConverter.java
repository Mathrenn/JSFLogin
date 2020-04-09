package com.objectway.stage.converter;

import java.util.List;

public interface ServiceViewConverter<T, E>{
	public T viewToService(E e);
	public E serviceToView(T t);
	public List<T> viewListToServiceList(List<E> el);
	public List<E> serviceListToViewList(List<T> tl);
}
