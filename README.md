# Platonus University Management System

Java 17 және SQLite негізінде жасалған университеттің оқу процесін басқаруға арналған оқу жобасы.

> Бұл жоба — оқу мақсатындағы консольдік прототип. Ол нақты Platonus production серверіне немесе университеттің ішкі деректеріне қосылмайды.

## Мүмкіндіктері
- Студенттерді, оқытушыларды, пәндерді және оқу топтарын CRUD арқылы басқару
- Сабақтар мен сабаққа қатысуды басқару
- Бағаларды енгізу және өзгерту
- Бағалар бойынша статистика және орташа балл есептеу
- CSV және JSON экспорт
- Бағаларды параллель өңдеу (multithreading)
- SQLite дерекқоры, JDBC және DAO қабаты
- Generic Repository үлгісі
- Сабақ кестесіндегі оқытушы, топ және аудитория уақыттарының қабаттасуын тексеретін `ScheduleConflictService`
- Бос жұмыс күнін, уақыт аралығын және аудиторияны таңдап, сабақтарды greedy алгоритмімен орналастыратын `ScheduleGenerator`
- JUnit 5 тесттері

## Технологиялар
Java 17, Maven, SQLite, JDBC, JUnit 5, Git/GitHub, CSV, JSON.

## Жоба құрылымы
```text
PlatonusSystem/
├── .github/                 # CI workflow және PR шаблоны
├── data/exports/            # Экспорт файлдары (жергілікті)
├── docs/
│   ├── research.md          # Research
│   └── uml/                 # UML және дерекқор құжаттары
├── src/main/java/           # Қолданба коды
├── src/test/java/           # JUnit 5 тесттері
├── .gitignore
├── pom.xml
└── README.md
```

## Іске қосу
Талаптар: JDK 17+ және Maven.

Тесттер:
```bash
mvn clean verify
```

Қосымшаны іске қосу:
```bash
mvn exec:java
```

## Құжаттама
- [Research](docs/research.md)
- [Database schema](docs/uml/database-schema.md)
- [UML диаграммалары](docs/uml/)

## Деректер қоры
SQLite дерекқоры `platonus.db` файлы ретінде жоба түбірінде жасалады. Бұл файл `.gitignore` арқылы Git-ке қосылмайды; қосымша іске қосылғанда кестелерді `DatabaseInitializer` жасайды.

## Тестілеу
Тесттерді қайта орындап, ағымдағы нақты нәтижені `mvn clean verify` қорытындысынан алыңыз. README ішінде алдын ала бекітілген тест саны көрсетілмейді, себебі тесттер саны өзгеруі мүмкін.

## GitHub командалық жұмыс
Feature branch жасаңыз, өзгерістерді commit етіңіз, GitHub-та Pull Request ашыңыз. PR-ға кемінде бір командалас код шолуын қалдырып, CI тесттері өткеннен кейін ғана merge жасаңыз. CI конфигурациясы `.github/workflows/maven.yml` ішінде.
