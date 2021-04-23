# Top 10 word

### Настройка окружения
* Java version 1.8;
* Maven3.

### Описание параметров в файле application.properties:

*folder.path* - путь до папки с файлами;

*delimiter.regExp* - разделить между словами;

*word.length* - минимальная длина слова;

### Запуск
* Установить зависимости и собрать jar: _mvn clean install_;
* Запустить полученный jar файл: java -jar top10word-1.0-SNAPSHOT.jar --folder.path=<ins>ПУТЬ ДО ФАЙЛА</ins>