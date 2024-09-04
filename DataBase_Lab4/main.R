# NIE EDYTOWAĆ *****************************************************************
dsn_database = "wbauer_adb_2023"   # Specify the name of  Database
dsn_hostname = "pgsql-196447.vipserv.org"  # Specify host name 
dsn_port = "5432"                # Specify your port number. 
dsn_uid = "wbauer_adb"         # Specify your username. 
dsn_pwd = "adb2020"        # Specify your password.

library(DBI)
library(RPostgres)
library(testthat)

con <- dbConnect(Postgres(), dbname = dsn_database, host=dsn_hostname, port=dsn_port, user=dsn_uid, password=dsn_pwd) 
# ******************************************************************************

film_in_category<- function(category_id)
{
  # Funkcja zwracająca wynik zapytania do bazy o tytuł filmu, język, oraz kategorię dla zadanego id kategorii.
  # Przykład wynikowej tabeli:
  # |   |title          |language    |category|
  # |0	|Amadeus Holy	|English	|Action|
  # 
  # Tabela wynikowa ma być posortowana po tylule filmu i języku.
  # 
  # Jeżeli warunki wejściowe nie są spełnione to funkcja powinna zwracać wartość NULL
  # 
  # Parameters:
  # category_id (integer): wartość id kategorii dla którego wykonujemy zapytanie
  # 
  # Returns:
  # DataFrame: DataFrame zawierający wyniki zapytania
  # 
  if (!is.integer(category_id) || is.null(category_id)){
    return(NULL)
  }
  
  categoryFilmLanguage <- dbGetQuery(con, glue::glue("SELECT film.title, language.name, category.name from category
  
        INNER JOIN film_category ON category.category_id = film_category.category_id
        INNER JOIN film ON film_category.film_id = film.film_id
        INNER JOIN language ON film.language_id = language.language_id
        
        WHERE category.category_id = {category_id}
        ORDER BY film.title, language.name"))
  
  colnames(categoryFilmLanguage) <- c('title','language','category')
  return(categoryFilmLanguage)
}

number_films_in_category <- function(category_id){
  #   Funkcja zwracająca wynik zapytania do bazy o ilość filmów w zadanej kategori przez id kategorii.
  #     Przykład wynikowej tabeli:
  #     |   |category   |count|
  #     |0	|Action 	|64	  | 
  #     
  #     Jeżeli warunki wejściowe nie są spełnione to funkcja powinna zwracać wartość NULL.
  #         
  #     Parameters:
  #     category_id (integer): wartość id kategorii dla którego wykonujemy zapytanie
  #     
  #     Returns:
  #     DataFrame: DataFrame zawierający wyniki zapytania
  #if (!is.integer(category_id) || is.null(category_id)){
  #  return(NULL)
  #}
  
  categoryFilm <- dbGetQuery(con, glue::glue("
  SELECT 
  DISTINCT category.name,
  COUNT(category.name)
  
  FROM category 
  JOIN film_category ON film_category.category_id = category.category_id

  WHERE category.category_id = {category_id}
  GROUP BY category.name
  "))
  category <- dbGetQuery(con, glue::glue("SELECT name from category
                                         WHERE category_id = {category_id}"))
  quantity <- nrow(categoryFilm)
  result <- data.frame(
    category <- c(category),
    count <- c(quantity)
  )
  
  colnames(categoryFilm) <- c('category', 'count')
  return (categoryFilm)

}


number_film_by_length <- function(min_length, max_length){
  #   Funkcja zwracająca wynik zapytania do bazy o ilość filmów dla poszczegulnych długości pomiędzy wartościami min_length a max_length.
  #     Przykład wynikowej tabeli:
  #     |   |length     |count|
  #     |0	|46 	    |64	  | 
  #     
  #     Jeżeli warunki wejściowe nie są spełnione to funkcja powinna zwracać wartość NULL.
  #         
  #     Parameters:
  #     min_length (int,double): wartość minimalnej długości filmu
  #     max_length (int,double): wartość maksymalnej długości filmu
  #     
  #     Returns:
  #     pd.DataFrame: DataFrame zawierający wyniki zapytania
  
  if (!is.integer(min_length) && !is.double(min_length)){
    return(NULL)
  }
  if (!is.integer(max_length) && !is.double(max_length)){
    return(NULL)
  }
  
  number_film_by_length <- dbGetQuery(con, glue::glue("
          SELECT 
  DISTINCT film.length,
  COUNT(film.length)
  
  FROM film 

  WHERE film.length >= {min_length} AND film.length <= {max_length}
  GROUP BY film.length"))
  
  if(nrow(number_film_by_length) == 0){
    return(NULL)
  }
  
  colnames(number_film_by_length) <- c('length', 'count') 
  
  return(number_film_by_length)
}



client_from_city<- function(city){
  #   Funkcja zwracająca wynik zapytania do bazy o listę klientów z zadanego miasta przez wartość city.
  #     Przykład wynikowej tabeli:
  #     |   |city	    |first_name	|last_name
  #     |0	|Athenai	|Linda	    |Williams
  #     
  #     Tabela wynikowa ma być posortowana po nazwisku i imieniu klienta.
  #     
  #     Jeżeli warunki wejściowe nie są spełnione to funkcja powinna zwracać wartość NULL.
  #         
  #     Parameters:
  #     city (character): nazwa miaste dla którego mamy sporządzić listę klientów
  #     
  #     Returns:
  #     DataFrame: DataFrame zawierający wyniki zapytania
  if (!is.character(city) || is.null(city)){
    return(NULL)
  }
  query <- 
    "  SELECT 
  city.city,
  customer.first_name,
  customer.last_name
  
  FROM city
  JOIN address ON address.city_id = city.city_id
  JOIN customer ON customer.address_id = address.address_id
    WHERE city.city = 'CITY'"
  
  query <- str_replace(query, "CITY", city)

  clientFromCity <- dbGetQuery(con, query)
  
  colnames(clientFromCity) <- c('city', 'first_name', 'last_name')
  return(clientFromCity)
}

avg_amount_by_length<-function(length){
  #   Funkcja zwracająca wynik zapytania do bazy o średnią wartość wypożyczenia filmów dla zadanej długości length.
  #     Przykład wynikowej tabeli:
  #     |   |length |avg
  #     |0	|48	    |4.295389
  #     
  #     
  #     Jeżeli warunki wejściowe nie są spełnione to funkcja powinna zwracać wartość NULL.
  #         
  #     Parameters:
  #     length (integer,double): długość filmu dla którego mamy pożyczyć średnią wartość wypożyczonych filmów
  #     
  #     Returns:
  #     DataFrame: DataFrame zawierający wyniki zapytania
  
  if (!(is.integer(length) || is.double(length) )|| is.null(length)){
    return(NULL)
  }
  
  query <- 
  "SELECT film.length, AVG(payment.amount) FROM payment
  
  JOIN rental ON payment.rental_id = rental.rental_id
  JOIN inventory ON rental.inventory_id = inventory.inventory_id
  JOIN film ON inventory.film_id = film.film_id
  
  WHERE film.length = 'LEN'
  GROUP BY film.length"
  query <- str_replace(query, "LEN", toString(length))
  result <- dbGetQuery(con, query)
  colnames(result) <- c("length", "avg")
  return(result)
}


client_by_sum_length<-function(sum_min){
  #   Funkcja zwracająca wynik zapytania do bazy o sumaryczny czas wypożyczonych filmów przez klientów powyżej zadanej wartości .
  #     Przykład wynikowej tabeli:
  #     |   |first_name |last_name  |sum
  #     |0  |Brian	    |Wyman  	|1265
  #     
  #     Tabela wynikowa powinna być posortowane według sumy, imienia i nazwiska klienta.
  #     Jeżeli warunki wejściowe nie są spełnione to funkcja powinna zwracać wartość NULL.
  #         
  #     Parameters:
  #     sum_min (integer,double): minimalna wartość sumy długości wypożyczonych filmów którą musi spełniać klient
  #     
  #     Returns:
  #     DataFrame: DataFrame zawierający wyniki zapytania
  
  if (!(is.integer(sum_min) || is.double(sum_min) )|| is.null(sum_min)){
    return(NULL)
  }
  
  query <- "
  SELECT customer.first_name, customer.last_name, SUM(film.length) FROM customer
  
  JOIN rental ON rental.customer_id = customer.customer_id
  JOIN inventory ON inventory.inventory_id = rental.inventory_id
  JOIN film ON film.film_id = inventory.film_id
  
  GROUP BY customer.last_name, customer.first_name
  HAVING SUM(film.length) > sumMin
  ORDER BY SUM(film.length), customer.last_name, customer.first_name "
  
  query <- str_replace(query, 'sumMin', toString(sum_min))
  
  result <- dbGetQuery(con, query)
  colnames(result) <- c('first_name', 'last_name', 'sum')
  return(result)
}


category_statistic_length<-function(name){
  #   Funkcja zwracająca wynik zapytania do bazy o statystykę długości filmów w kategorii o zadanej nazwie.
  #     Przykład wynikowej tabeli:
  #     |   |category   |avg    |sum    |min    |max
  #     |0	|Action 	|111.60 |7143   |47 	|185
  #     
  #     Jeżeli warunki wejściowe nie są spełnione to funkcja powinna zwracać wartość NULL.
  #         
  #     Parameters:
  #     name (character): Nazwa kategorii dla której ma zostać wypisana statystyka
  #     
  #     Returns:
  #     DataFrame: DataFrame zawierający wyniki zapytania
  if(!is.character(name) || is.null(name)){
    return(NULL)
  }
  query <- "
  SELECT 
  category.name,
  AVG(film.length),
  SUM(film.length),
  MIN(film.length),
  MAX(film.length)
  FROM category
  
  JOIN film_category ON film_category.category_id = category.category_id
  JOIN film ON film.film_id = film_category.film_id
  
  WHERE category.name = 'NAME'
  GROUP BY category.name"


  query <- str_replace(query, 'NAME', toString(name))
  
  result <- dbGetQuery(con, query)

  colnames(result) <- c('category', 'avg', 'sum', 'min', 'max')

  return(result)
}

# NIE EDYTOWAĆ *****************************************************************
test_dir('tests/testthat')
# ******************************************************************************