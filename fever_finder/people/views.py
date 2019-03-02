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

        who_sharefood_with = self.request.query_params.get('eatsWith')
        who_live_with = self.request.query_params.get('livesWith')
        who_work_with = self.request.query_params.get('worksWith')
        symptom_score = self.request.query_params.get('symptomScore')
        risk_score = self.request.query_params.get('riskScore')

        queryset = Person.objects.all().filter(patient_name__contains=patient_name,
                                               who_sharefood_with__contains=who_sharefood_with,
                                               who_live_with__contains=who_live_with,
                                               who_work_with__contains=who_work_with,
                                               symptom_score__level__gte=symptom_score,
                                               risk_score__level__gte=risk_score)

        return queryset
