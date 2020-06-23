# import pymorphy2
import codecs
# import re
# import operator
#
# if __name__ == "__main__":
#     morph = pymorphy2.MorphAnalyzer()
#
#     text = codecs.open('igra_prestolov.txt', 'r', 'cp1251').read()
#     # Формируем список всех слов
#     words = re.findall(r'[а-я]+', text.lower())
#
#     # Формируем список лемм
#     lemmas = list(map(lambda x: morph.parse(x)[0].normal_form, words))
#
#     counter_lemmas = {}
#     for lemma in lemmas:
#         counter_lemmas[lemma] = counter_lemmas.get(lemma, 0) + 1
#
#     # Сортировка
#     sorted_counter_lemmas = sorted(counter_lemmas.items(), key=operator.itemgetter(1), reverse=True)
#
#     for lemma in sorted_counter_lemmas:
#         print(lemma)
#
#

import re
from collections import Counter

file_train = 'study.txt'
file_test = 'test.txt'

WORD = 0
COUNT = 1

def read_words(filename: str) -> list:
    # contents = open(filename, 'r').read().casefold()
    contents = codecs.open(filename, 'r', 'UTF-8').read()
    clean = re.sub(r'[^a-z0-9]', ' ', contents)   # add cyrillic letters
    # splitted = re.compile(r'\s+').split(clean)
    # return splitted[:-1] # remove last empty word
    return re.findall(r'[а-я]+', contents.lower())

def print_words(filename: str, word_probs: dict):
    with open(filename, 'w+') as out:
        for word, prob in word_probs.items():
            print(f'{word}\t\t{prob}', file=out)

def count_bigrams(word_list: list) -> dict:
    bigram_count = {}
    i = 1
    while i < len(word_list):
        bigram = (word_list[i - 1], word_list[i])
        if bigram in bigram_count:
            bigram_count[bigram] += 1
        else:
            bigram_count[bigram] = 1
        i += 1
    return bigram_count

def count_words(word_list: list) -> dict:
    words = Counter(word_list).most_common()
    # cast list of (word, count) into dict {word: count}
    return {w[WORD]: w[COUNT] for w in words}

def unigram_additive_model(word_list: list) -> dict:
    word_count = count_words(word_list)
    N = len(word_list)
    V = len(word_count)
    print(f'N (total words): {N}, V (num of types): {V}')

    word_probability = {}
    for word, count in word_count.items():
        word_probability[word] = (count + 1) / (N + V)
    return word_probability


def bigram_witten_bell_model(word_list: list, unigram_model) -> dict:
    bigram_count = count_bigrams(word_list)
    word_count = count_words(word_list)

    # returns number of N-gram types which start with a given word 'prefix'
    N = lambda prefix: len(list(filter(lambda bi: bi[0] == prefix, list(bigram_count.keys()))))

    # Max likelyhood: count(bigram) / count (bigram-prefix)
    Pml = lambda bigram: bigram_count[bigram] / word_count[bigram[0]]

    # Number of bigram histories (bigrams that end with given word)
    def E(word: str) -> int:
        end_with_word = list(filter(lambda bi: bi[0][1] == word, bigram_count.items()))
        return sum(w[1] for w in end_with_word)

    # 1 - lambda = Num-of-prefixes / (Num-of-prefixes + Total-occurence)
    Lambda = lambda bigram: 1 - (N(bigram[0]) / (N(bigram[0]) + E(bigram[1])))

    bigram_probability = {}
    for bigram in bigram_count:
        print(f'Lambda {bigram} {Lambda(bigram)}')
        bigram_probability[bigram] = Lambda(bigram) * Pml(bigram) + \
                                     (1 - Lambda(bigram)) * unigram_model[bigram[1]]
    return bigram_probability


def main():
    train_set = read_words(file_train)

    # count models
    uni_train_model = unigram_additive_model(train_set)
    bi_train_model = bigram_witten_bell_model(train_set, uni_train_model)

    # use models against test set
    test_set = read_words(file_test)
    product = 1
    for w in test_set:
        if w in uni_train_model:
            product *= uni_train_model[w]
    pp_unigram = product ** (-1 / len(test_set))

    product = 1
    i = 1
    while i < len(test_set):
        bigram = (test_set[i - 1], test_set[i])
        if bigram in bi_train_model:
            product *= bi_train_model[bigram]
        i += 1
    pp_bigram = product ** (-1 / len(test_set))

    print(f'Unigram perplexity: {pp_unigram}\n'
          f'Bigram  perplexity: {pp_bigram}')
    print_words('out_unigrams.txt', uni_train_model)
    print_words('out_bigrams.txt', bi_train_model)


if __name__ == '__main__':
    main()