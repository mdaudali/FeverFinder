# Generated by Django 2.1.5 on 2019-03-02 16:40

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('people', '0014_auto_20190226_2041'),
    ]

    operations = [
        migrations.AlterField(
            model_name='person',
            name='who_live_with',
            field=models.CharField(default='unknown', max_length=1024),
        ),
        migrations.AlterField(
            model_name='person',
            name='who_sharefood_with',
            field=models.CharField(default='unknown', max_length=1024),
        ),
        migrations.AlterField(
            model_name='person',
            name='who_work_with',
            field=models.CharField(default='unknown', max_length=1024),
        ),
    ]
