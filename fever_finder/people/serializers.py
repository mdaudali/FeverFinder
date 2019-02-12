from rest_framework import serializers
from people.models import Person

class PersonSerializer(serializers.ModelSerializer):
    rice_how_store = serializers.StringRelatedField(many=True)
    occupation = serializers.StringRelatedField(many=True)
    farmer_type = serializers.StringRelatedField(many=True)
    trader_occupation = serializers.StringRelatedField(many=True)
    education = serializers.StringRelatedField(many=True)
    knows_lassa_cause = serializers.StringRelatedField(many=True)
    knows_lassa_what_treat = serializers.StringRelatedField(many=True)
    knows_lassa_how_prevent = serializers.StringRelatedField(many=True)

    class Meta:
        model = Person
        fields = '__all__'
