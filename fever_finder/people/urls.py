from django.urls import path
from . import views

urlpatterns = [
    path('api/people/', views.PersonListCreate.as_view() ),
]
