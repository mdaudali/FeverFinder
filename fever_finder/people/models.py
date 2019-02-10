import uuid
from django.db import models

class Patient(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    created_at = models.DateTimeField(auto_now_add=True, blank=False, editable=False)

    name = models.CharField(max_length=128)
