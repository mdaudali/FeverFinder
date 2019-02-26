#!/bin/bash

echo "Using virtual environment..."
source env/bin/activate

echo "Building front end..."

cd front_end
npm run build
cd ..

echo "Copying built front end files into 'frontend' Django app..."

mkdir fever_finder/frontend/static
cp front_end/build/* fever_finder/frontend/static/ -r
cp front_end/build/index.html fever_finder/frontend/templates/frontend/

echo "Migrating database..."

cd fever_finder
python manage.py migrate

echo "Starting the server..."

python manage.py runserver 0.0.0.0:8000