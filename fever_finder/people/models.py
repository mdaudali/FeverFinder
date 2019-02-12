import uuid
from datetime import datetime
from django.db import models

# The definition of these classes is basically the same, but let's not an extra
# class from which we inherit them, because that just complicates things further.

class FarmerType(models.Model):
    type = models.CharField(max_length=32)


class TraderOccupationType(models.Model):
    type = models.CharField(max_length=32)


class LassaCause(models.Model):
    type = models.CharField(max_length=32)


class LassaTreatment(models.Model):
    type = models.CharField(max_length=32)


class LassaPrevent(models.Model):
    type = models.CharField(max_length=32)


class RiceType(models.Model):
    type = models.CharField(max_length=32)

class EducationType(models.Model):
    type = models.CharField(max_length=32)


class OccupationType(models.Model):
    type = models.CharField(max_length=32)


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
    knows_lassa_can_prevent = models.CharField(max_length=8, default="Does not know")
    knows_lassa_how_prevent = models.ManyToManyField(LassaPrevent)
    take_vaccine = models.BooleanField(default=False)
    had_lassa = models.BooleanField(default=False)
    fear = models.BooleanField(default=False)
    fear_range = models.IntegerField(default=1)
    rats_present = models.BooleanField(default=False)
    mastomys = models.BooleanField(default=False)
    farm = models.BooleanField(default=False)
    cultivate_rice = models.BooleanField(default=False)
    rice = models.ManyToManyField(RiceType)
    eat_rice = models.BooleanField(default=False)
    make_garri = models.BooleanField(default=False)
    drink_garri = models.BooleanField(default=False)

    name = models.CharField(max_length=128)
