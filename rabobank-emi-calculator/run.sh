 docker volume create my_sqlite_data
 docker build -t ch . &&  docker run -v my_sqlite_data:/usr/app/data -p 8080:8080 ch
