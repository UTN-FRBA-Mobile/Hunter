# Hunter Backend
## Deploy in Heroku (Heroku CLI is required)

docker build -t registry.heroku.com/utn-mobile-hunter/web .

docker push registry.heroku.com/utn-mobile-hunter/web

heroku container:release web --app utn-mobile-hunter

## View Logs
heroku logs --tail --app utn-mobile-hunter

##ENV
DATABASE_URL=postgres://{user}:{password}@{host}:5432/{database}
PORT=8080
