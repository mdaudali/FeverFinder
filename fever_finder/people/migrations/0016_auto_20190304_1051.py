# Generated by Django 2.1.5 on 2019-03-04 10:51

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('people', '0015_auto_20190302_1640'),
    ]

    operations = [
        migrations.AddField(
            model_name='person',
            name='has_cough',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='has_facial_swelling',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='has_hypertension',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='has_meningitis',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='has_muscle_fatigue',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='person',
            name='has_vomiting',
            field=models.BooleanField(default=False),
        ),
    ]