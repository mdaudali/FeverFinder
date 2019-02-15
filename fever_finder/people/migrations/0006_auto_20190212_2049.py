# Generated by Django 2.1.5 on 2019-02-12 20:49

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('people', '0005_auto_20190212_2030'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='educationtype',
            name='type',
        ),
        migrations.RemoveField(
            model_name='farmertype',
            name='type',
        ),
        migrations.RemoveField(
            model_name='lassacause',
            name='type',
        ),
        migrations.RemoveField(
            model_name='lassaprevent',
            name='type',
        ),
        migrations.RemoveField(
            model_name='lassatreatment',
            name='type',
        ),
        migrations.RemoveField(
            model_name='occupationtype',
            name='type',
        ),
        migrations.RemoveField(
            model_name='ricetype',
            name='type',
        ),
        migrations.RemoveField(
            model_name='traderoccupationtype',
            name='type',
        ),
        migrations.AddField(
            model_name='educationtype',
            name='value',
            field=models.CharField(default='', max_length=32),
        ),
        migrations.AddField(
            model_name='farmertype',
            name='value',
            field=models.CharField(default='', max_length=32),
        ),
        migrations.AddField(
            model_name='lassacause',
            name='value',
            field=models.CharField(default='', max_length=32),
        ),
        migrations.AddField(
            model_name='lassaprevent',
            name='value',
            field=models.CharField(default='', max_length=32),
        ),
        migrations.AddField(
            model_name='lassatreatment',
            name='value',
            field=models.CharField(default='', max_length=32),
        ),
        migrations.AddField(
            model_name='occupationtype',
            name='value',
            field=models.CharField(default='', max_length=32),
        ),
        migrations.AddField(
            model_name='ricetype',
            name='value',
            field=models.CharField(default='', max_length=32),
        ),
        migrations.AddField(
            model_name='traderoccupationtype',
            name='value',
            field=models.CharField(default='', max_length=32),
        ),
        migrations.AlterField(
            model_name='person',
            name='education',
            field=models.ManyToManyField(null=True, to='people.EducationType'),
        ),
    ]
