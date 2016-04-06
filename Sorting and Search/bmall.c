#include <stdio.h>
#include <string.h>
#include <stdlib.h>
void delta1(char *s, int *q)
{ 
        int a = 0, j, size = 256, len = strlen(s);
        while (a < size) {
		q[a] = len;
		a++;
	}
	j = 0;
	while (j < len) {
		q[s[j]] = len - j - 1;
		j++;
	}
	
}
void suffix(char *s, int *q)
{
	int len = strlen(s), t, i;
	t = len - 1;	
	q[len-1] = t;
	i = len - 2;
	while (i >= 0) {
		while ((t < (len - 1)) && (s[t] != s[i])) 
			t = q[t + 1];
		if (s[t] == s[i])
			t--;
		q[i] = t;
		i--;
	}
}	
void delta2(char *s, int *q)
{
	int t, i, len = strlen(s);
	int suff[len];
	suffix(s, suff);
	i = 0;
	t = suff[0];
	while (i < len) {
		while (t < i)
			t = suff[t+1];
		q[i] = -i + t + len;
		i++;
	}
	i = 0;
	while (i < len - 1) {
		t = i;
		while (t < len - 1) {
			t = suff[t + 1];
			if (s[i] != s[t])
				q[t] = -(i + 1) + len;
		}
		i++;
	}
}
int max(int x1, int x2)
{
        return (x1 > x2) ? x1 : x2; 
} 
void bmsubst(char *s, char *t)
{
	int k, i, lens = strlen(s), lent = strlen(t), q1[256], q2[lens];
	delta1(s, q1);
	delta2(s, q2);
	k = lens - 1;
	while (k < lent) {
		i = lens - 1;
		while (t[k] == s[i]) {
			if (i == 0) {
				printf("%d ", k);
				break;
			}
			i--;
			k--;
		}
		k += max(q1[t[k]], q2[i]);
	}
	printf("\n");
}
int main(int argc, char **argv)
{
	
	bmsubst(argv[1], argv[2]);
	return 0;
}
