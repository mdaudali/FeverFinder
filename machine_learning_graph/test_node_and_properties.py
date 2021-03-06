import unittest

from machine_learning_graph.model import Node, Properties


class TestNodeAndPropertiesMethods(unittest.TestCase):
    feature = {
        "_id": 1,
        "name": "Cleo Knowles",
        "gender": "female",
        "age": 69,
        "community_name": "Carlos",
        "village_name": "Allentown",
        "town_name": "Lindisfarne",
        "state_name": "Wyoming",
        "lga_name": "Mulino",
        "knows_people_with_id": [
            5674,
            2666,
            2607,
            7737,
            8105,
            24,
            6739,
            7226,
            7503,
            8543,
            3660,
            1533,
            15,
            9616,
            8878,
            7748,
            3981,
            9918,
            9215,
            6259,
            2979,
            2782,
            2830,
            183,
            4055,
            5887,
            9920,
            2549,
            2625,
            2072
        ],
        "rice_how_store": "Dry and store in sack",
        "occupation": "Artisan",
        "farmer_type": "Cassava",
        "trader_occupation": "Rice trader",
        "education": "Secondary education",
        "interview_date": "2015-07-21T03:52:40 -01:00",
        "created_at": "2016-05-22T05:50:10 -01:00",
        "latitude": 35.16,
        "longitude": 37.0432,
        "knows_lassa": True,
        "knows_lassa_what_treat": "Prayers",
        "knows_lassa_how_prevent": [
            "Personal hygiene",
            "Vaccine",
            "Vaccine"
        ],
        "knows_lassa_can_prevent": False,
        "knows_lassa_can_treat": True,
        "knows_lassa_can_kill": True,
        "knows_lassa_cause": "I don't know",
        "take_vaccine": True,
        "had_lassa": False,
        "knows_lassa_know_signs": True,
        "income": "$3,248.80",
        "family_size": 11,
        "eat_rice": False,
        "cultivate_rice": False,
        "rats_present": True,
        "mastomys": False,
        "farm": False,
        "make_garri": False,
        "drink_garri": True,
        "fear_range": 2,
        "fear": False,
        "sick": 0,
        "risk": 0,
        "temperature": 40.02,
        "has_facial_swelling": False,
        "has_muscle_fatigue": False,
        "has_vomiting": False,
        "has_cough": True,
        "has_meningitis": False,
        "has_hypertension": False
    }
    property_instance = Properties(feature)

    def test_construct_properties(self):
        self.assertEqual(Properties(self.feature).blob, self.feature)

    def test_get_id(self):
        self.assertEqual(self.property_instance.get_id(), 1)



if __name__ == '__main__':
    unittest.main()