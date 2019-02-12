import uuid
import re
from datetime import datetime
from django.db import models

# TODO: do this nicely with inheritance
class FarmerType(models.Model):
    value = models.CharField(max_length=32, default="")

    def __str__(self):
        return self.value

class TraderOccupationType(models.Model):
    value = models.CharField(max_length=32, default="")

    def __str__(self):
        return self.value

class LassaCause(models.Model):
    value = models.CharField(max_length=32, default="")

    def __str__(self):
        return self.value

class LassaTreatment(models.Model):
    value = models.CharField(max_length=32, default="")

    def __str__(self):
        return self.value


class LassaPrevent(models.Model):
    value = models.CharField(max_length=32, default="")

    def __str__(self):
        return self.value

class RiceType(models.Model):
    value = models.CharField(max_length=32, default="")

    def __str__(self):
        return self.value

class OccupationType(models.Model):
    value = models.CharField(max_length=32, default="")

    def __str__(self):
        return self.value

class EducationType(models.Model):
    value = models.CharField(max_length=32, default="")

    def __str__(self):
        return self.value

class Person(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    created_at = models.DateTimeField(auto_now_add=True, blank=False, editable=False)

    # SimPrints specific columns based on survey (not everything from the survey)
    community_name = models.CharField(max_length=64, default="Unknown")
    village_name = models.CharField(max_length=64, default="Unknown")
    town_name = models.CharField(max_length=64, default="Unknown")
    state_name = models.CharField(max_length=64, default="Unknown")
    lga_name = models.CharField(max_length=64, default="Unknown")
    interview_date = models.DateTimeField(default=datetime.now)
    store_gps_latitude = models.FloatField(default=0.0)
    store_gps_longitude = models.FloatField(default=0.0)
    age = models.IntegerField(default=-1)
    gender = models.CharField(max_length=8, default="Female")
    occupation = models.ManyToManyField(OccupationType)
    farmer_type = models.ManyToManyField(FarmerType)
    trader_occupation = models.ManyToManyField(TraderOccupationType)
    education = models.ManyToManyField(EducationType)
    income = models.IntegerField(default=0)
    family_size = models.IntegerField(default=1)
    knows_lassa = models.BooleanField(default=False)
    knows_lassa_cause = models.ManyToManyField(LassaCause)
    knows_lassa_know_signs = models.BooleanField(default=False)
    knows_lassa_can_kill = models.BooleanField(default=False)
    knows_lassa_can_treat = models.BooleanField(default=False)
    knows_lassa_what_treat = models.ManyToManyField(LassaTreatment)
    knows_lassa_can_prevent = models.BooleanField(default=False)
    knows_lassa_how_prevent = models.ManyToManyField(LassaPrevent)
    take_vaccine = models.BooleanField(default=False)
    had_lassa = models.BooleanField(default=False)
    fear = models.BooleanField(default=False)
    fear_range = models.IntegerField(default=1)
    rats_present = models.BooleanField(default=False)
    mastomys = models.BooleanField(default=False)
    farm = models.BooleanField(default=False)
    cultivate_rice = models.BooleanField(default=False)
    rice_how_store = models.ManyToManyField(RiceType)
    eat_rice = models.BooleanField(default=False)
    make_garri = models.BooleanField(default=False)
    drink_garri = models.BooleanField(default=False)
