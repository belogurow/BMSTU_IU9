import random
import sys


class GeneticAlgorithm():
	def __init__(self):
		self.alpha = 0
		self.beta = 5
		self.population_size = 60
		self.number_of_genes = 20
		self.crossover_probability = 0.5
		self.mutation_probability = 1/self.number_of_genes
		self.tournament_selection_parameter = 0.75
		self.tournament_size = 3
		self.number_of_variables = 3
		self.variable_range = 5
		self.number_of_generations = 150
		self.number_of_best_individual_copies = 1
		self.fitness = [0 for x in range(self.population_size)]

		self.population = self.initialize_population(self.population_size, self.number_of_genes)

	def run_ga(self):
		k = 0
		for iGenerations in range(self.number_of_generations):
			print(k)
			k += 1

			maximum_fitness = 0.0
			x_best = [0 for _ in range(self.number_of_variables)]
			best_individual = None

			# Decode chromosome and evaluate individual
			for i in range(self.population_size):
				chromosome = self.population[i]
				x = self.decode_chromosome(chromosome, self.number_of_variables, self.variable_range)
				self.fitness[i] = self.evaluate_individual(x)
				if self.fitness[i] > maximum_fitness:
					maximum_fitness = self.fitness[i]
					x_best = x
					best_individual = chromosome
			temp_population = self.population

			# Tournament selection
			# Loop through every other individual (should be a nicer way?)
			for i in range(round(self.population_size/2)):
				j = i*2
				i1 = self.tournament_select(self.fitness, self.tournament_selection_parameter, self.tournament_size)
				i2 = self.tournament_select(self.fitness, self.tournament_selection_parameter, self.tournament_size)
				chromosome_1 = self.population[i1]
				chromosome_2 = self.population[i2]

				# Crossover
				r = random.random()
				if r < self.crossover_probability:
					new_chromosome_pair = self.cross(chromosome_1, chromosome_2)
					temp_population[j] = new_chromosome_pair[0]
					temp_population[j+1] = new_chromosome_pair[1]
				else:
					temp_population[j] = chromosome_1
					temp_population[j+1] = chromosome_2

			# Mutate
			for i in range(self.population_size):
				original_chromosome = temp_population[i]
				mutated_chromosome = self.mutate(original_chromosome, self.mutation_probability)
				temp_population[i] = mutated_chromosome

			# Insert best individual
			if best_individual is not None:
				temp_population = self.insert_best_individual(temp_population, best_individual, self.number_of_best_individual_copies)
			population = temp_population

		print("f({}) = {}".format(x_best, 1/maximum_fitness))

	def initialize_population(self, population_size, number_of_genes):
		population = [[0 for y in range(number_of_genes)] for x in range(population_size)]
		step = (self.beta - self.alpha) / self.number_of_genes
		for i in range(population_size):
			for j in range(number_of_genes):
				while True:
					population[i][j] = step * random.random() + step

					if 0 <= population[i][j] <= 1:
						break
		return population

	def decode_chromosome(self, chromosome, number_of_variables, variable_range):
		n_genes = len(chromosome)
		n_split = round(n_genes/self.number_of_variables)
		x = [0.0 for x in range(self.number_of_variables)]

		for i in range(self.number_of_variables):
			for j in range(n_split):
				x[i] = x[i] + chromosome[n_split*(i-1)+j]*2**(-j)
			x[i] = -self.variable_range + 2*self.variable_range*x[i]/(1-2**(-n_split))

		return x

	def evaluate_individual(self, x):
		# The fitness function
		x_values = x
		if len(x_values) == 3:
			a = 2
			b = 3
			c = 1

			g = - 3 * a / (
					-c / 3.0 + b * (pow(x_values[0] - 4.0, 2) + pow(x_values[1] - 4.0, 2) + pow(x_values[2] - 4.0, 2)))

			fitness_value = 1/g
		else:
			print("ERROR: Wrong size individual")
			sys.exit()
		return fitness_value

	def tournament_select(self, fitness, tournament_selection_parameter, tournament_size):
		i_tmp_vector = [0 for x in range(tournament_size)]
		fitness_vector = [0 for x in range(tournament_size)]
		i_selected = None
		for i in range(tournament_size):
			i_tmp_vector[i] = int(random.random()*self.population_size)
			fitness_vector[i] = fitness[i_tmp_vector[i]]

		no_chosen_index = True
		while no_chosen_index:
			idx_maximum = fitness_vector.index(max(fitness_vector))
			if len(fitness_vector) > 1:
				if random.random() < tournament_selection_parameter:
					i_selected = i_tmp_vector[idx_maximum]
					no_chosen_index = False
				else:
					fitness_vector.pop(idx_maximum)
					i_tmp_vector.pop(idx_maximum)
			else:
				i_selected = i_tmp_vector[0]
				no_chosen_index = False

		return i_selected

	def cross(self, chromosome_1, chromosome_2):
		n_genes = len(chromosome_1)
		crossover_point = round(random.random() * n_genes)

		new_chromosome_pair = [[0 for y in range(n_genes)] for x in range(2)]
		for j in range(n_genes):
			if j < crossover_point:
				new_chromosome_pair[0][j] = chromosome_1[j]
				new_chromosome_pair[1][j] = chromosome_2[j]
			else:
				new_chromosome_pair[0][j] = chromosome_2[j]
				new_chromosome_pair[1][j] = chromosome_1[j]

		return new_chromosome_pair

	def mutate(self, chromosome, mutation_probability):
		mutated_chromosome = chromosome
		for j in range(self.number_of_genes):
			if random.random() < mutation_probability:
				mutated_chromosome[j] = 1-chromosome[j]

		return mutated_chromosome

	def insert_best_individual(self, population, best_individual, number_of_best_individual_copies):
		for i in range(number_of_best_individual_copies):
			population[i] = best_individual

		return population


if __name__ == "__main__":
	ga = GeneticAlgorithm()
	ga.run_ga()
