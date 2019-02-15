# FeverFinder
Cambridge Part 1B Group Project 2018-2019

## Getting started

### Front end

Setting up React development server should be easy provided you have NodeJS:

```
cd front_end
npm install
npm install --save react-google-maps
npm start
```

After this you should be able to see the React app hosted via a development server at `localhost:3000`.

### Back end

To be able to test some things you do locally you'll need to interact with the API. It's best to run a
Django development server locally for this, although I'm setting up a build system on our Amazon instance.

You'll need `Python 3` and `pip` to set a local development server up.

First, install and set up `virtualenv`:
```
pip install virtualenv
virtualenv --python=python3 env
```

Activate the environments and install requirements:
```
source env/bin/activate
pip install -r requirements.txt
```

If all goes well, at this point you should have Django installed. You still need to get your DB sorted out, though:
```
cd fever_finder
python manage.py migrate
```

I prepared some dummy data too which you can load with:
```
python manage.py loaddata people/fixtures/initial_data.json
```

After this you should be able to run the server:
```
python manage.py runserver
```

This will allow you some very basic interaction with the Django REST framework on `localhost:8000/api/people`.
