# Generated by Django 2.1.5 on 2019-02-25 15:11

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('people', '0010_auto_20190224_0841'),
    ]

    operations = [
        migrations.AlterField(
            model_name='person',
            name='contact',
            field=models.CharField(default='unknown', max_length=1024),
        ),
    ]
