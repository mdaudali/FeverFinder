from django.urls import path
from . import views

urlpatterns = [
    path('api/people/', views.PatientListCreate.as_view() ),
]
