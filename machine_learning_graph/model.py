class Node:
    properties = []

    def __init__(self, data, graph):
        # LOAD person attributes
        self.graph = graph
        self.sick_weights = {}
        self.infectious_weights = {}
        self.self_sick = 0
        self.self_infectious = 0
        self.from_others = 0

    def compute_probability_sick(self):
        return self.self_sick or self.set_sick(sum(self.sick_weights[k] * v for k, v in self.properties))

    def compute_probability_infectious(self):
        return self.self_infectious or self.set_infectious(sum(self.sick_weights[k] * v for k, v in self.properties))

    def get_infect_from(self, node, relation):
        return self.resolve_lazy_load(node).compute_probability_infectious() * self.sick_weights[relation]

    def resolve_lazy_load(self, node):
        return self.graph.resolve(node)

    def set_sick(self, sick_val):
        self.self_sick = sick_val
        return self.self_sick

    def set_infectious(self, infectious_val):
        self.self_infectious = infectious_val
        return self.self_infectious


class Graph:
    def __init__(self):
        self.nodes = self.load_and_build_nodes()

    def load_and_build_nodes(self):
        nodes = self.load_nodes()
        node_dict = {}
        for node in nodes:
            node_dict[node["id"]] = self.build_node(node)
        return node_dict

    def load_nodes(self):
        return [{}]
        # Load the json file

    def build_node(self, data):
        return Node(data, self)

    def resolve(self, node):
        return self.nodes[node]






