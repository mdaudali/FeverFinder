from people.serializers import PersonSerializer
from rest_framework import generics
from people.models import Person


class PersonListCreate(generics.ListCreateAPIView):
    queryset = Person.objects.all()
    serializer_class = PersonSerializer


class PersonByID(generics.ListAPIView):
    serializer_class = PersonSerializer

    def get_queryset(self):
        queryset = Person.objects.all()
        id = self.request.query_params.get('id', None)

        if id is not None:
            return queryset.filter(id = id)
        return queryset


class PersonFilter(generics.ListAPIView):
    serializer_class = PersonSerializer

    def get_queryset(self):
        patient_name = self.request.query_params.get('name')

        # TODO: filter with these other things as well
        eats_with = self.request.query_params.get('eatsWith')
        lives_with = self.request.query_params.get('livesWith')
        works_with = self.request.query_params.get('worksWith')
        symptom_score = self.request.query_params.get('symptomScore')
        risk_score = self.request.query_params.get('riskScore')

        queryset = Person.objects.all().filter(patient_name__contains=patient_name)

        return queryset