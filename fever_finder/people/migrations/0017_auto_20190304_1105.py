# Generated by Django 2.1.5 on 2019-03-04 11:05

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('people', '0016_auto_20190304_1051'),
    ]

    operations = [
        migrations.AlterField(
            model_name='person',
            name='who_live_with',
            field=models.CharField(default='[]', max_length=1024),
        ),
        migrations.AlterField(
            model_name='person',
            name='who_sharefood_with',
            field=models.CharField(default='[]', max_length=1024),
        ),
        migrations.AlterField(
            model_name='person',
            name='who_work_with',
            field=models.CharField(default='[]', max_length=1024),
        ),
    ]
