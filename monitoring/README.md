# Black Desert monitoring

Основные функции:
- мониторинг происходящего на экране во время работы бота(фото или видео режим);
- архив версий бота;
- баг трекер;
- руководство пользователя;
- оплата;
- админка для добавления новых пользователей.

[Клиент для использования](https://github.com/Symb1OS/fishing).

### Requirements

- mysql 5.5;
- docker >= 18.03.0-ce.

### Building

```
docker image build -t monitoring .

```

### Running

```
docker run -it -d -p 8081:8080 monitoring

```

### Use

После запуска личный кабинет пользователя станет доступен по следующему url:

```
http://localhost:8081/monitoring/
user - admin
password - pa55word

```

Добавление новых пользователей:

```
http://localhost:8081/monitoring/user

```