#Hunter Backend
Deploy in Heroku (Heroku CLI is required)

docker build -t registry.heroku.com/utn-mobile-hunter/web .
docker push registry.heroku.com/utn-mobile-hunter/web
heroku container:release web --app utn-mobile-hunter
