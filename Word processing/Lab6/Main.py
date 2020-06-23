import re
import codecs
import pymorphy2
import operator
from math import log
from collections import namedtuple

Pair = namedtuple("Pair", ["first", "second"])
NOUN = 'NOUN'
ADJECTIVE = 'ADJF'


def get_pos(word):
    return morph.parse(word)[0].tag.POS


def get_lemma(word):
    return morph.parse(word)[0].normal_form


def is_noun(word):
    return get_pos(word) == NOUN


def is_adjective(word):
    return get_pos(word) == ADJECTIVE


if __name__ == "__main__":
    morph = pymorphy2.MorphAnalyzer()

    text = codecs.open('geografia.txt', 'r', 'cp1251').read()

    # Формируем список всех слов
    words = re.findall(r'[а-я]{2,}', text.lower())

    # Формируем список всех конструкций прилагательное + существительное
    bigrams = []
    for i in range(len(words) - 1):
        if is_adjective(words[i]) and is_noun(words[i + 1]):
            bigrams.append(Pair(
                get_lemma(words[i]),
                get_lemma(words[i + 1])))
    print(bigrams)

    # Формируем частотный список униграм (для меры взаимной информации)
    unigrams_counter = {}
    for bigram in bigrams:
        unigrams_counter[bigram.first] = unigrams_counter.get(bigram.first, 0) + 1
        unigrams_counter[bigram.second] = unigrams_counter.get(bigram.second, 0) + 1

    # Формируем частотный список конструкций прил+существительное
    counter_bigrams = {}
    for bigram in bigrams:
        counter_bigrams[bigram] = counter_bigrams.get(bigram, 0) + 1

    # Формируем список конструкций для меры взаимной информации
    mutual_counter_bigrams = {}
    for key, value in counter_bigrams.items():
        mutual_counter_bigrams[key] = log((value * len(words) / (unigrams_counter.get(key.first) * unigrams_counter.get(key.second))))

    # Сортировка
    sorted_counter_bigrams = sorted(counter_bigrams.items(), key=operator.itemgetter(1), reverse=True)
    sorted_mutual_counter_bigrams = sorted(mutual_counter_bigrams.items(), key=operator.itemgetter(1), reverse=True)

    print("\nПо частотности:")
    for bigram in sorted_counter_bigrams[:20]:
        print("\hline")
        print(bigram[0].first + " " + bigram[0].second + " & " + str(bigram[1]) + " % \\\\")

    print("\nПо мере взаимной информации:")
    for bigram in sorted_mutual_counter_bigrams[:100]:
        print("\hline")
        print(bigram[0].first + " " + bigram[0].second + " & " + str(bigram[1]) + " % \\\\")
