package com.stm.top10word.service.process;

import com.stm.top10word.exception.ProcessException;

/**
 * Интерфейс для запуска и обработки результата, определенного в BuilderProcessService, процесса.
 *
 * @param <IN> - Тип входного параметра
 * @param <OUT> - Тип выходного параметра
 *
 * @author STMLabs.
 */
public interface ProcessRunnerService<IN, OUT> {
    /**
     * Запускает процесс
     *
     * @param input - Входное значение
     * @return - Выходное значение
     * @throws ProcessException - исключение в случае
     */
    OUT startProcess(IN input) throws ProcessException;
}
