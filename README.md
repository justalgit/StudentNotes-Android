# Мобильное приложение "StudentNotes"
<ul>
  <li><b>Исполнитель:</b> Ларин А.В.</li>
  <li><b>Группа:</b> М8О-203М-20</li>
  <li><b>Научный руководитель:</b> Ухов П.А.</li>
</ul>
Выполнено в рамках выпускной квалификационной работы на тему <b>«Микросервисное приложение "Ежедневник студента"»</b>.

### Структура проекта

Весь основной код сосредоточен [в данном модуле](https://github.com/justalgit/StudentNotes-Android/tree/master/app/src/main).

Директория [data](https://github.com/justalgit/StudentNotes-Android/tree/master/app/src/main/java/com/example/studentnotes/data) содержит код слоя данных:
<ul>
  <li>База данных</li>
  <li>Репозитории</li>
  <li>Классы для сущностей, хранимых в таблице</li>
  <li>Интерфейсы взаимодействия с локальной БД и сервером</li>
</ul>

Директория [ui](https://github.com/justalgit/StudentNotes-Android/tree/master/app/src/main/java/com/example/studentnotes/ui) содержит код слоя представлений:
<ul>
  <li>Экраны пользовательского интерфейса</li>
  <li>Виджеты и визуальные компоненты</li>
  <li>Файлы конфигурации и стилизации</li>
</ul>

Директория [utils](https://github.com/justalgit/StudentNotes-Android/tree/master/app/src/main/java/com/example/studentnotes/utils) содержит служебные утилиты, используемые в бизнес-логике приложения.