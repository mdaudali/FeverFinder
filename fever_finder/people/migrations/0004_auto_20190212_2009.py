# Generated by Django 2.1.5 on 2019-02-12 20:09

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('people', '0003_auto_20190212_1927'),
    ]

    operations = [
        migrations.CreateModel(
            name='EducationType',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.CharField(max_length=32)),
            ],
        ),
        migrations.CreateModel(
            name='FarmerType',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.CharField(max_length=32)),
            ],
        ),
        migrations.CreateModel(
            name='LassaCause',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.CharField(max_length=32)),
            ],
        ),
        migrations.CreateModel(
            name='LassaPrevent',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.CharField(max_length=32)),
            ],
        ),
        migrations.CreateModel(
            name='LassaTreatment',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.CharField(max_length=32)),
            ],
        ),
        migrations.CreateModel(
            name='OccupationType',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.CharField(max_length=32)),
            ],
        ),
        migrations.CreateModel(
            name='RiceType',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.CharField(max_length=32)),
            ],
        ),
        migrations.CreateModel(
            name='TraderOccupationType',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.CharField(max_length=32)),
            ],
        ),
        migrations.AddField(
            model_name='person',
            name='age',
            field=models.IntegerField(default=-1),
        ),
        migrations.AddField(
            model_name='person',
            name='community_name',
            field=models.CharField(default='Unknown', max_length=64),
        ),
        migrations.AddField(
            model_name='person',
            name='cultivate_rice',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='drink_garri',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='eat_rice',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='family_size',
            field=models.IntegerField(default=1),
        ),
        migrations.AddField(
            model_name='person',
            name='farm',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='fear',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='fear_range',
            field=models.IntegerField(default=1),
        ),
        migrations.AddField(
            model_name='person',
            name='gender',
            field=models.CharField(default='Female', max_length=8),
        ),
        migrations.AddField(
            model_name='person',
            name='had_lassa',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='income',
            field=models.IntegerField(default=0),
        ),
        migrations.AddField(
            model_name='person',
            name='interview_date',
            field=models.DateTimeField(default=datetime.datetime.now),
        ),
        migrations.AddField(
            model_name='person',
            name='knows_lassa',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='knows_lassa_can_kill',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='knows_lassa_can_prevent',
            field=models.CharField(default='Does not know', max_length=8),
        ),
        migrations.AddField(
            model_name='person',
            name='knows_lassa_can_treat',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='knows_lassa_know_signs',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='lga_name',
            field=models.CharField(default='Unknown', max_length=64),
        ),
        migrations.AddField(
            model_name='person',
            name='make_garri',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='mastomys',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='rats_present',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='state_name',
            field=models.CharField(default='Unknown', max_length=64),
        ),
        migrations.AddField(
            model_name='person',
            name='store_gps_latitude',
            field=models.FloatField(default=0.0),
        ),
        migrations.AddField(
            model_name='person',
            name='store_gps_longitude',
            field=models.FloatField(default=0.0),
        ),
        migrations.AddField(
            model_name='person',
            name='take_vaccine',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='town_name',
            field=models.CharField(default='Unknown', max_length=64),
        ),
        migrations.AddField(
            model_name='person',
            name='village_name',
            field=models.CharField(default='Unknown', max_length=64),
        ),
        migrations.AddField(
            model_name='person',
            name='education',
            field=models.ManyToManyField(to='people.EducationType'),
        ),
        migrations.AddField(
            model_name='person',
            name='farmer_type',
            field=models.ManyToManyField(to='people.FarmerType'),
        ),
        migrations.AddField(
            model_name='person',
            name='knows_lassa_cause',
            field=models.ManyToManyField(to='people.LassaCause'),
        ),
        migrations.AddField(
            model_name='person',
            name='knows_lassa_how_prevent',
            field=models.ManyToManyField(to='people.LassaPrevent'),
        ),
        migrations.AddField(
            model_name='person',
            name='knows_lassa_what_treat',
            field=models.ManyToManyField(to='people.LassaTreatment'),
        ),
        migrations.AddField(
            model_name='person',
            name='occupation',
            field=models.ManyToManyField(to='people.OccupationType'),
        ),
        migrations.AddField(
            model_name='person',
            name='rice',
            field=models.ManyToManyField(to='people.RiceType'),
        ),
        migrations.AddField(
            model_name='person',
            name='trader_occupation',
            field=models.ManyToManyField(to='people.TraderOccupationType'),
        ),
    ]