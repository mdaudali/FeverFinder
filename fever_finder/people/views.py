from django.shortcuts import render
from people.serializers import PersonSerializer
from rest_framework import generics
from people.models import Person

class PersonListCreate(generics.ListCreateAPIView):
    queryset = Person.objects.all()
    serializer_class = PersonSerializer
