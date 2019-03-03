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

        # Get parameters
        who_sharefood_with = self.request.query_params.get('eatsWith')
        who_live_with = self.request.query_params.get('livesWith')
        who_work_with = self.request.query_params.get('worksWith')
        symptom_score = self.request.query_params.get('symptomScore')
        risk_score = self.request.query_params.get('riskScore')

        # Construct filter
        kwargs = {}
        if patient_name is not None:
            kwargs['patient_name__contains'] = patient_name
        if who_sharefood_with is not None:
            kwargs['who_sharefood_with__contains'] = who_sharefood_with
        if who_live_with is not None:
            kwargs['who_live_with__contains'] = who_live_with
        if who_work_with is not None:
            kwargs['who_work_with__contains'] = who_work_with
        if symptom_score is not None:
            kwargs['sick__range'] = (symptom_score, 1.0)
        if risk_score is not None:
            kwargs['risk__range'] = (risk_score, 1.0)

        # Call the query
        queryset = Person.objects.all().filter(**kwargs)

        return queryset
