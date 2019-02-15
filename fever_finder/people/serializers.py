from rest_framework import serializers
from people.models import *

class PersonSerializer(serializers.ModelSerializer):
    rice_how_store = serializers.SlugRelatedField(many=True, slug_field='value', queryset=RiceType.objects.all())
    occupation = serializers.SlugRelatedField(many=True, slug_field='value', queryset=OccupationType.objects.all())
    farmer_type = serializers.SlugRelatedField(many=True, slug_field='value', queryset=FarmerType.objects.all())
    trader_occupation = serializers.SlugRelatedField(many=True, slug_field='value', queryset=TraderOccupationType.objects.all())
    education = serializers.SlugRelatedField(many=True, slug_field='value', queryset=EducationType.objects.all())
    knows_lassa_cause = serializers.SlugRelatedField(many=True, slug_field='value', queryset=LassaCause.objects.all())
    knows_lassa_what_treat = serializers.SlugRelatedField(many=True, slug_field='value', queryset=LassaTreatment.objects.all())
    knows_lassa_how_prevent = serializers.SlugRelatedField(many=True, slug_field='value', queryset=LassaPrevent.objects.all())

    class Meta:
        model = Person
        fields = '__all__'
