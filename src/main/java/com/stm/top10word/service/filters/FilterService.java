package com.stm.top10word.service.filters;

/**
 * Общий фильтр объекта. Реализация паттерна цепочка обязанностей
 *
 * @author STMLabs
 */
public interface FilterService<T> {
    /**
     * На основе этого значения выстраивается цепочка фильтров
     *
     * @return значение приоритета
     */
    int getPriority();

    /**
     * @param t проверяемый объект
     * @return рузльтат проверки
     */
    boolean check(T t);
}
