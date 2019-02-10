from django.shortcuts import render
from people.serializers import PatientSerializer
from rest_framework import generics
from people.models import Patient

class PatientListCreate(generics.ListCreateAPIView):
    queryset = Patient.objects.all()
    serializer_class = PatientSerializer
