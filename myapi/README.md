#myApi

##Конфигурация
Для разработки я использовал Intellij Idea Community edition, для запросов на сервер - Postman

Сервер запускается на порту 8001
Перед началом эксплуатации необходимо иметь в postgresql базу данных с названием "hibernate", а в ней схему с названием "schema".
В файле конфигурации main/resources/hibernate.cfg.xml прописаны следующие строки:
 		<property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/hibernate</property>
        <property name="hibernate.connection.username">postgres</property>
        <property name="hibernate.connection.password">123</property>
Соответственно, для использования другого пользователя необходимо поменять в файле username и password.
Таким же образом можно изменить БД, но схему изменить немного сложнее.
В схеме нужно создать следующие таблицы: documents и goods
CREATE TABLE documents(
	id serial,
	goods text,
	warehouse varchar(255),
	warehouse_to varchar(255)
);

CREATE TABLE goods(
	id serial,
	name varchar(255),
	amount int,
	code bigint,
	warehouse_name varchar(255),
	buys int,
	sells int
);

я использовал PostgreSQLDialect9, который позволяет создавать auto_increment поля - serial
После создания базы данных и двух таблиц можно приступать к работе

##Стек технологий
Я использовал Java socket для создания серверной части, Hibernate Framework для работы с базами данных и Google GSON для сериализации и десериализации объектов.
##Описание проекта
###Описание возможностей
Все запросы на API должны быть в формате JSON либо с пустым телом, в случае пустого тела там где необходим JSON-документ будет возвращено сообщение "Empty request body", в случае неудачного парсинга - "Ошибка парсинга документа"
API предоставляет шесть эндпойнтов, на каждый эндпойнт - один HTTP-метод.
При запросе на первые три из описанных эндпойнтов генерируются документы.
Я исходил из того, что есть определенный перечень складов и создал для них Set прямо в коде, соответственно там где необходимо указать имя склада в запросе, его нужно указать верно, иначе вернется сообщение о несуществующем складе.
Перечень созданных складов:
{Kolomenskoe,
ZIL,
Yasenevo,
Pobeda,
Vorsino}
Этот список можно дополнять в классе Controller, функционал не испортится.
####1
http://localhost:8001/api/buygoods
POST
Результат успешного выполнения команды - создание перечня товаров, которые были указаны в запросе на складе, также указанном в запросе; кроме того, если товар пришел с уже существующим в БД артикулем, то цена продажи меняется для всех товаров с этим артикулем; для новых товаров цена продажи устанавливается равной нулю.
Если база обновлена - ответ будет "Database updated".
В теле обязательно должен быть JSON включающий в себя имя склада (warehouse) и массив товаров, товар - code, name, amount, lastBuyPrice, lastSellPrice
#####пример
{
	"warehouse" : "ZIL",
	"goods":
	[
		{
			"code" : "121223",
			"name" : "Glass",
			"amount" : "6",
			"lastBuyPrice" : "14445.12",
			"lastSellPrice" : "1234.55"
		},
		{
			"code" : "5343",
			"name" : "Hair",
			"amount" : "3",
			"lastBuyPrice" : "14445.12",
			"lastSellPrice" : "1234.55"
		},
		{
			"code" : "15343",
			"name" : "Bike",
			"amount" : "10",
			"lastBuyPrice" : "14445.12",
			"lastSellPrice" : "1234.55"
		},
		{
			"code" : "623",
			"name" : "Mirror",
			"amount" : "1",
			"lastBuyPrice" : "14445.12",
			"lastSellPrice" : "1234.55"
		}
	]
}
####2
http://localhost:8001/api/sellgoods
POST
Результат успешного выполнения команды - списание со склада, указанного в запросе, перечня товаров и установка им новой цены продажи. Указанная цена покупки не влияет на записи в БД, она нужна для успешного парсинга.
Все изменения в БД будут описаны в ответе.
В теле обязательно должен быть JSON включающий в себя имя склада (warehouse) и массив товаров, товар - code, name, amount, lastBuyPrice, lastSellPrice
#####пример
{
	"warehouse" : "ZIL",
	"goods":
	[
		{
			"code" : "121223",
			"name" : "Glass",
			"amount" : "6",
			"lastBuyPrice" : "14445.12",
			"lastSellPrice" : "134.55"
		},
		{
			"code" : "5343",
			"name" : "Hair",
			"amount" : "3",
			"lastBuyPrice" : "14445.12",
			"lastSellPrice" : "124.55"
		}
	]
}
####3
http://localhost:8001/api/movegoods
PUT
Результат успешного выполнения команды - списание с указанного склада (warehouse), указанного в запросе, перечня товаров и добавление их на другой склад (warehouse_to).
В теле обязательно должен быть JSON включающий в себя имя склада (warehouse), имя склада на который нужно переместить товар(warehouse_to) и массив товаров, товар - code, name, amount, lastBuyPrice, lastSellPrice
#####пример
{
	"warehouse" : "ZIL",
	"warehouse_to" :"Kolomenskoe",
	"goods":
	[
		{
			"code" : "1111",
			"name" : "Hair",
			"amount" : "3",
			"lastBuyPrice" : "13335.12",
			"lastSellPrice" : "12111.55"
		},
		{
			"code" : "121223",
			"name" : "Glass",
			"amount" : "6",
			"lastBuyPrice" : "111111.12",
			"lastSellPrice" : "111111.55"
		}
	]
}
####4
http://localhost:8001/api/getdocuments
GET
Запрос возвращает все документы, сгенерированные в ходе работы с API.
Тело запроса пустое

####5
http://localhost:8001/api/seegoodswhichleft
GET
Запрос возвращает общий список товаров (артикул, наименование, цены закупки и продажи). В качестве опционального параметра может быть передан фильтр по имени товара.
#####пример
{"name":"Mirror"}

//

Либо пустое тело для получения всех товаров
####6
http://localhost:8001/api/seegoodslist
GET
Остатки товаров на складах (артикул, наименование, остаток по всем складам). В качестве опционального параметра может быть передан фильтр по складу.
#####пример
{"warehouse":"ZIL"}

//

Либо пустое тело для получения всех имеющихся товаров по всем складам
###Краткое описание классов
Фантазии на имена классов и переменных у меня оказалось недостаточно, поэтому это должно немного помочь в понимании структуры проекта:
######package Controller:
Включает в себя основной контроллер и три класса для реализации логики работы с базой данных.
__________
Controller - здесь реализованы сокеты, прием и возврат данных
PostBuyController - реализует прием товаров на склады
PostSellController - реализует продажу товаров со складов
UpdateController - реализует перемещение товаров между складами

######package DAO:
Два интерфейса и два расширяющих их класса для hibernate-query
____
DocumentDAOImpl - запросы к таблице documents
GoodsDAOImpl - запросы к таблице goods

######package Entity:
Здесь находятся классы, созданные специально для сериализации, а также классы-Entity для маппинга с Hibernate
______
Document - сериализатор приходящих JSON на POST
DocumentEntity - маппинг с documents
DocumentMove - сериализатор приходящих JSON на PUT(добавляется warehouse_to, другой конструктор)
Goods - маппинг с goods и собственный static toJson
Name - сериализатор для выдачи товаров по name
ResponseAllGoods - реализация логики для выдачи всех товаров
ResponseGood - класс для десериализации и создания ответов ResponseSee
ResponseSee - реализация логики для выдачи остатков товаров
Warehouse - сериализатор для выдачи товаров по warehouse