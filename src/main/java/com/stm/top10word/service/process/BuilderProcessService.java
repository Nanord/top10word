package com.stm.top10word.service.process;

/**
 * Интерфейс для объединения цепочек процесса.
 *
 * @param <I> Тип входной объект для старта процесса.
 * @param <O> Тип результат работы процесса.
 *
 * @author STMLabs.
 */
public interface BuilderProcessService<I, O> {
    /**
     * @param input Входной объект для старта процесса.
     * @return Результат работы процесса.
     */
    O build(I input);
}
