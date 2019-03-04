import json
import random
from collections import namedtuple
from functools import partial
import time
class Node:
    def __init__(self, graph, properties):
        self.graph = graph
        self.self_sick = 0
        self.self_infectious = 0
        self.self_risk = 0
        self.total_sick = 0
        self.from_others = 0
        self.properties = properties

    def compute_probability_sick(self):
        if not self.self_sick:
            self.self_sick = sum(map(partial(self.properties.get_weight, ill_type="symptom"), self.properties.get_symptoms()))
            self.total_sick = self.self_sick + self.compute_from_others()
        return self.total_sick

    def compute_probability_infectious(self):
        if not self.self_infectious:
            self.self_infectious = sum(map(partial(self.properties.get_weight, ill_type="infectious"), self.properties.get_infectious()))
        return self.self_infectious

    def compute_probability_risk(self):
        if not self.self_risk:
            self.self_risk = sum(map(partial(self.properties.get_weight, ill_type="risk"), self.properties.get_risk_factors()))
        return self.self_risk

    def get_infect_from(self, node, relation):
        if node == 10000:
            return 0
        return self.resolve_lazy_load(node).compute_probability_infectious() * self.properties.relation_weight(relation)

    def resolve_lazy_load(self, node):
        return self.graph.resolve(node)

    def set_and_get_sick(self, sick_val):
        self.self_sick = sick_val
        return self.self_sick

    def set_and_get_infectious(self, infectious_val):
        self.self_infectious = infectious_val
        return self.self_infectious

    def get_id(self):
        return self.properties.get_id()

    def compute_from_others(self):
        if not self.from_others:
            self.from_others = sum(map(partial(self.get_infect_from, relation="knows_people"), self.properties.get_known()))
        return self.from_others


class Graph:
    def __init__(self, resource):
        self.nodes = self.load_and_build_nodes(resource)

    def load_and_build_nodes(self, resource):
        nodes = self.load_nodes(resource)
        node_dict = {}
        for node in nodes:
            node_dict[node["_id"]] = self.build_node(node)
        return node_dict

    def load_nodes(self, resource):
        with open(resource, "r") as f:
            nodes = json.load(f)
        return nodes["features"]

    def build_node(self, data):
        return Node(self, Properties(data))

    def resolve(self, node):
        return self.nodes[node]

    def compute_all(self):
        for k, v in self.nodes.items():
            v.compute_probability_sick()
            v.compute_probability_infectious()
            v.compute_from_others()
        self.linear_interpolate_all()

    def linear_interpolate_all(self):
        min_sick, max_sick = self.get_min_max("sick")
        for k, v in self.nodes.items():
            v.total_sick = self.compute_linear_interpolation(min_sick, max_sick, v.total_sick)

        min_i, max_i = self.get_min_max("infectious")
        for k, v in self.nodes.items():
            v.self_infectious = self.compute_linear_interpolation(min_i, max_i, v.self_infectious)

        min_r, max_r = self.get_min_max("risk")
        for k, v in self.nodes.items():
            v.self_risk = self.compute_linear_interpolation(min_r, max_r, v.self_risk)

    def compute_linear_interpolation(self, minv, maxv, v):
        return (v - minv)/(maxv - minv)

    def get_min_max(self, property):
        if property == "sick":
            min_s = float("inf")
            max_s = float("-inf")
            for k, v in self.nodes.items():
                min_s = min(min_s, v.compute_probability_sick())
                max_s = max(max_s, v.compute_probability_sick())
            return min_s, max_s
        elif property == "infectious":
            min_s = float("inf")
            max_s = float("-inf")
            for k, v in self.nodes.items():
                min_s = min(min_s, v.compute_probability_infectious())
                max_s = max(max_s, v.compute_probability_infectious())
            return min_s, max_s
        elif property == "risk":
            min_s = float("inf")
            max_s = float("-inf")
            for k, v in self.nodes.items():
                min_s = min(min_s, v.compute_probability_risk())
                max_s = max(max_s, v.compute_probability_risk())
            return min_s, max_s

    def dump_to(self, filename):
        mapping = {}
        for k, v in self.nodes.items():
            mapping[k] = {}
            mapping[k]["sick"] = v.compute_probability_sick()
            mapping[k]["infectious"] = v.compute_probability_infectious()
            mapping[k]["risk"] = v.compute_probability_risk()
        with open(filename, "w") as f:
            json.dump(mapping, f)

def temperature_func(temperature):
    if temperature <= 35:
        return 3
    elif 35 < temperature <= 36:
        return 1
    elif 36 < temperature <= 38:
        return 0
    elif 38 < temperature < 40:
        return 2
    elif temperature >= 40:
        return 3

class Properties:

    ProbTuple = namedtuple('ProbTuple', "symptom infectious risk")
    FunctionTuple = namedtuple('FunctionTuple', "function prob_weight")
    symptom_map = {
        "has_facial_swelling": ProbTuple(0.2, 0, 0.3),
        "has_muscle_fatigue": ProbTuple(0.1, 0, 0.1),
        "has_vomiting": ProbTuple(0.1, 0.4, 0.3),
        "has_cough": ProbTuple(0.05, 0.1, 0.1),
        "has_meningitis": ProbTuple(0.4, 0, 0.4),
        "has_hypertension": ProbTuple(0.2, 0, 0.3),
        "temperature": FunctionTuple(temperature_func, ProbTuple(0.3, 0, 0.2)),
        "knows_lassa": FunctionTuple(lambda x: not x, ProbTuple(0, 0, 0.3)),
        "take_vaccine": FunctionTuple(lambda x: not x, ProbTuple(0, 0, 0.8)),
        "rats_present": ProbTuple(0, 0, 0.6),
        "drink_garri": ProbTuple(0, 0, 0.3)
    }

    symptom_names = [x for x, v in symptom_map.items() if getattr(v, "symptom", 0) or (type(v).__name__ == "FunctionTuple" and v.prob_weight.symptom)]
    infectious_names = [x for x, v in symptom_map.items() if getattr(v, "infectious", 0) or (type(v).__name__ == "FunctionTuple" and v.prob_weight.infectious)]
    risk_factor_names = [x for x, v in symptom_map.items() if getattr(v, "risk", 0) or (type(v).__name__ == "FunctionTuple" and v.prob_weight.risk)]

    relation_mapping = {
        "knows_people": 0.1
    }

    def __init__(self, blob):
        self.symptoms = {}
        self.infectious = {}
        self.risk_factors = {}
        self.blob = blob

    def get_id(self):
        return self.blob["_id"]

    def get_symptoms(self):
        if not self.symptoms:
            self.symptoms = {k: self.blob[k] for k in self.symptom_names}
        return self.symptoms

    def get_infectious(self):
        if not self.infectious:
            self.infectious = {k: self.blob[k] for k in self.infectious_names}
        return self.infectious

    def get_risk_factors(self):
        if not self.risk_factors:
            self.risk_factors = {k: self.blob[k] for k in self.risk_factor_names}
        return self.risk_factors

    def get_known(self):
        return self.blob["knows_people_with_id"]

    def get_weight(self, property, ill_type):
        val = self.symptom_map[property]
        if type(val).__name__ == "FunctionTuple":
            processed_value = val.function(self.blob[property])
            return processed_value * getattr(val.prob_weight, ill_type)
        else:
            return getattr(val, ill_type) * self.blob[property]

    def relation_weight(self, property):
        return self.relation_mapping[property]



start_time = time.time()
graph = Graph("people-db.json")
graph.compute_all()
print ("Compute time", time.time() - start_time)
graph.dump_to("output_data.json")

