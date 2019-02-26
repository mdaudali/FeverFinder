from django.shortcuts import render
from people.serializers import PersonSerializer
from rest_framework import generics
from people.models import Person

class PersonListCreate(generics.ListCreateAPIView):
    queryset = Person.objects.all()
    serializer_class = PersonSerializer

class PersonFilter(generics.ListAPIView):
    serializer_class = PersonSerializer

    def get_queryset(self):
        queryset = Person.objects.all()
        id = self.request.query_params.get('id', None)

        if id is not None:
            return queryset.filter(id = id)
        return queryset
