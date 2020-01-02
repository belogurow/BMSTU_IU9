class CharacteristicFunction:
    def __init__(self, key, value: int):
        self.key = set(key)
        self.value = value

    @staticmethod
    def get_value(functions, key) -> int:
        result_function = list(filter(lambda f: f.key == key, functions))
        if len(result_function) > 1:
            raise ValueError(f'Find more than one value with key {key}')

        return result_function[0].value

    def __repr__(self):
        return f'v{self.key} = {self.value}'