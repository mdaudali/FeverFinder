from django.urls import path
from . import views

urlpatterns = [
    path('api/people/', views.PersonListCreate.as_view()),
    path('api/people/get_by_id/', views.PersonByID.as_view()),
    path('api/people/search/', views.PersonFilter.as_view())
]