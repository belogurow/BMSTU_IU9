# Реализация алгоритмов выравнивания последовательностей

1. [Smith–Waterman](https://en.wikipedia.org/wiki/Smith–Waterman_algorithm)
2. [Needleman–Wunsch](https://en.wikipedia.org/wiki/Needleman–Wunsch_algorithm)
3. [Needleman–Wunsch](https://en.wikipedia.org/wiki/Needleman–Wunsch_algorithm) c аффинным штрафом

## Пример

   На вход подается имя файла *.fasta, который содержит последовательности
```python
# python3
python Main.py input.fasta
```

```
Введите цифру алгоритма, который необходимо воспроизвести:
	1. SmithWaterman
	2. NeedlemanWunsch
	3. AffineSequenceAlignment

1

Введите значения для MATCH MISMATCH GAP_PENALTY (10 -5 -5)
10 -5 -5

Calculating...

CGTAACAAGGTTTCCGTAGGTGAACCTGCGGAAGGATCATTGATGAGAC--CGTGGAATAAACGATCGAGTGAATCCGGA
||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
CGTAACAAGGTTTCCGTAGGTGAACCTGCGGAAGGATCATTGTTGAGACAAC--AGAATATATGATCGAGTGAATCTGGA

...
```



