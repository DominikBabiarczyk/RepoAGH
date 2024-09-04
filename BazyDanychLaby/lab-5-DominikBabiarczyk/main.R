# NIE EDYTOWAĆ *****************************************************************
dsn_database = "wbauer_adb_2023"   # Specify the name of  Database
dsn_hostname = "pgsql-196447.vipserv.org"  # Specify host name 
dsn_port = "5432"                # Specify your port number. 
dsn_uid = "wbauer_adb"         # Specify your username. 
dsn_pwd = "adb2020"        # Specify your password.

library(DBI)
library(RPostgres)
library(testthat)
library(stringr)

con <- dbConnect(Postgres(), dbname = dsn_database, host=dsn_hostname, port=dsn_port, user=dsn_uid, password=dsn_pwd) 
# ******************************************************************************

film_in_category <- function(category)
{
  # Funkcja zwracająca wynik zapytania do bazy o tytuł filmu, język, oraz kategorię dla zadanego:
  #     - id: jeżeli categry jest integer
  #     - name: jeżeli category jest character, dokładnie taki jak podana wartość
  # Przykład wynikowej tabeli:
  # |   |title          |languge    |category|
  # |0	|Amadeus Holy	|English	|Action|
  # 
  # Tabela wynikowa ma być posortowana po tylule filmu i języku.
  # 
  # Jeżeli warunki wejściowe nie są spełnione to funkcja powinna zwracać wartość NULL.
  # 
  # Parameters:
  # category (integer,character): wartość kategorii po id (jeżeli typ integer) lub nazwie (jeżeli typ character)  dla którego wykonujemy zapytanie
  # 
  # Returns:
  # DataFrame: DataFrame zawierający wyniki zapytania
  
if(is.integer(category)){
  query <- glue::glue("
  SELECT film.title, language.name, category.name FROM category
  
  JOIN film_category ON category.category_id = film_category.category_id
  JOIN film ON film.film_id = film_category.film_id
  JOIN language ON language.language_id = film.language_id
  
  WHERE category.category_id = {category} 
  ")
  titleLanguageCategory <- dbGetQuery(con, query)
  colnames(titleLanguageCategory) <- c('title', 'languge', 'category')
  return(titleLanguageCategory)
}else if(is.character(category)){
  query <- glue::glue("
  SELECT film.title, language.name, category.name FROM category
  
  JOIN film_category ON category.category_id = film_category.category_id
  JOIN film ON film.film_id = film_category.film_id
  JOIN language ON language.language_id = film.language_id
  
  WHERE category.name LIKE 'variable'
  ")
  query <- str_replace(query, "variable", toString(category))
  result <- dbGetQuery(con, query)
  titleLanguageCategory <- dbGetQuery(con, query)
  colnames(titleLanguageCategory) <- c('title', 'languge', 'category')
  return(titleLanguageCategory)
}
  return(NULL)
}


film_in_category_case_insensitive <- function(category)
{
  #  Funkcja zwracająca wynik zapytania do bazy o tytuł filmu, język, oraz kategorię dla zadanego:
  #     - id: jeżeli categry jest integer
  #     - name: jeżeli category jest character
  #  Przykład wynikowej tabeli:
  #     |   |title          |languge    |category|
  #     |0	|Amadeus Holy	|English	|Action|
  #     
  #   Tabela wynikowa ma być posortowana po tylule filmu i języku.
  #     
  #     Jeżeli warunki wejściowe nie są spełnione to funkcja powinna zwracać wartość NULL.
  
  #   Parameters:
  #   category (integer,str): wartość kategorii po id (jeżeli typ integer) lub nazwie (jeżeli typ character)  dla którego wykonujemy zapytanie
  #
  #   Returns:
  #   DataFrame: DataFrame zawierający wyniki zapytania
  if(is.integer(category)){
    query <- glue::glue("
  SELECT film.title, language.name, category.name FROM category
  
  JOIN film_category ON category.category_id = film_category.category_id
  JOIN film ON film.film_id = film_category.film_id
  JOIN language ON language.language_id = film.language_id
  
  WHERE category.category_id = {category} 
  ")
    titleLanguageCategory <- dbGetQuery(con, query)
    colnames(titleLanguageCategory) <- c('title', 'languge', 'category')
    return(titleLanguageCategory)
  }else if(is.character(category)){
    query <- glue::glue("
  SELECT film.title, language.name, category.name FROM category
  
  JOIN film_category ON category.category_id = film_category.category_id
  JOIN film ON film.film_id = film_category.film_id
  JOIN language ON language.language_id = film.language_id
  
  WHERE category.name ILIKE 'variable'
  ")
    query <- str_replace(query, "variable", toString(category))
    result <- dbGetQuery(con, query)
    titleLanguageCategory <- dbGetQuery(con, query)
    colnames(titleLanguageCategory) <- c('title', 'languge', 'category')
    return(titleLanguageCategory)
  }
  return(NULL)
}

film_cast <- function(title)
{
  # Funkcja zwracająca wynik zapytania do bazy o obsadę filmu o dokładnie zadanym tytule.
  # Przykład wynikowej tabeli:
  #     |   |first_name |last_name  |
  #     |0	|Greg       |Chaplin    | 
  #     
  # Tabela wynikowa ma być posortowana po nazwisku i imieniu klienta.
  # Jeżeli warunki wejściowe nie są spełnione to funkcja powinna zwracać wartość NULL.
  #         
  # Parameters:
  # title (character): wartość id kategorii dla którego wykonujemy zapytanie
  #     
  # Returns:
  # DataFrame: DataFrame zawierający wyniki zapytania
  if (is.character(title)){
    query <- "
    SELECT actor.first_name, actor.last_name FROM actor 
    
    JOIN film_actor ON film_actor.actor_id = actor.actor_id
    JOIN film ON film.film_id = film_actor.film_id
    
    WHERE film.title LIKE 'TITLE'
    ORDER BY actor.last_name, actor.first_name DESC
    "
    query <- str_replace(query, 'TITLE', title)
    Actors <- dbGetQuery(con, query)
    return(Actors)
  }
  return(NULL)
}



film_title_case_insensitive2 <- function(words)
{
  # Funkcja zwracająca wynik zapytania do bazy o tytuły filmów zawierających conajmniej jedno z podanych słów z listy words.
  # Przykład wynikowej tabeli:
  #     |   |title              |
  #     |0	|Crystal Breaking 	| 
  #     
  # Tabela wynikowa ma być posortowana po nazwisku i imieniu klienta.
  # 
  # Jeżeli warunki wejściowe nie są spełnione to funkcja powinna zwracać wartość NULL.
  #         
  # Parameters:
  # words(list[character]): wartość minimalnej długości filmu
  #     
  # Returns:
  # DataFrame: DataFrame zawierający wyniki zapytania
  # 
  if (is.character(words)){
    condition <- paste0("UPPER(title) IN ('", paste(toupper(words), collapse = "','"), "')")
    query <- glue::glue("
    SELECT title FROM film
    
    JOIN inventory ON inventory.film_id = film.film_id
    JOIN rental ON rental.inventory_id = inventory.inventory_id
    JOIN customer ON customer.customer_id = rental.customer_id
    
    WHERE {condition}  ")
    titleInList <- dbGetQuery(con, query)
    return(titleInList)
  }
  return(NULL)
}

film_title_case_insensitive <- function(words)
{
  # Funkcja zwracająca wynik zapytania do bazy o tytuły filmów zawierających conajmniej jedno z podanych słów z listy words.
  # Przykład wynikowej tabeli:
  #     |   |title              |
  #     |0	|Crystal Breaking 	| 
  #     
  # Tabela wynikowa ma być posortowana po nazwisku i imieniu klienta.
  # 
  # Jeżeli warunki wejściowe nie są spełnione to funkcja powinna zwracać wartość NULL.
  #         
  # Parameters:
  # words(list[character]): wartość minimalnej długości filmu
  #     
  # Returns:
  # DataFrame: DataFrame zawierający wyniki zapytania
  # 
  
  if(is.double(words)) {
    return(NULL);
  }
  
  queryIncomplete <- "select title from film where title ~* '";
  
  for(i in seq_along(words)) {
    if(i != 1) {
      queryIncomplete <- paste(queryIncomplete, "|", sep="", end="");
    }
    queryIncomplete <- paste(queryIncomplete, "\\m", sep="", end="");
    queryIncomplete <- paste(queryIncomplete, words[i], sep="", end="");
    queryIncomplete <- paste(queryIncomplete, "\\M", sep="", end="");
  }
  queryCompleted <- paste(queryIncomplete, "' order by title;", sep="", end="");
  
  queryDF <- dbGetQuery(con, queryCompleted);
  colnames(queryDF) <- c("title");
  
  return(queryDF);
}

# NIE EDYTOWAĆ *****************************************************************
test_dir('tests/testthat')
# ******************************************************************************