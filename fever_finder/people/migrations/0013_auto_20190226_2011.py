# Generated by Django 2.1.5 on 2019-02-26 20:11

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('people', '0012_auto_20190226_1957'),
    ]

    operations = [
        migrations.AlterField(
            model_name='person',
            name='can_prevent',
            field=models.CharField(default='False', max_length=128),
        ),
    ]
