#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct Unit {
        long key, count;
	char word[10];
	struct Unit *parent, *left, *right;
} *root;
struct Unit* InitUnit(long k, char *v, long q, struct Unit **mas)
{
	struct Unit *x = malloc(sizeof(struct Unit));
	x->parent = NULL;
	x->left = NULL;
	x->right = NULL;
	x->count = 1;
	strcpy(x->word, v);
	x->key = k;
	mas[q] = x;
	return x;
}
struct Unit* Insert(struct Unit *t, long k, char *v, long q, struct Unit **mas) {
	if (t == NULL) 
		return InitUnit(k, v, q, mas);
	if (k < t->key) 
		t->left = Insert(t->left, k, v, q, mas);
	else if (k > t->key) 
		t->right = Insert(t->right, k, v, q, mas);
	else 
		strcpy(t->word, v);
	t->count = 1;
		if (t->left != NULL) 
			t->count += t->left->count; 
		if (t->right != NULL) 
			t->count += t->right->count; 
	return t;
}
void Lookup(struct Unit *t, long k)
{
	struct Unit *x = t;
	while ((x != NULL) && (x->key != k)) 
		if (k < x->key)
			x = x->left;
		else
			x = x->right;
	printf("%s\n", x->word);
}
struct Unit* Minimum(struct Unit *t)
{
	struct Unit *x;
	if (t == NULL)
		x = NULL;
	else {
		x = t;
		while (x->left != NULL)
			x = x->left;
	}
	return x;
}
struct Unit* DeleteMinimum(struct Unit *t) 
{
	if (t->left == NULL) 
		return t->right;
	t->left = DeleteMinimum(t->left);
	t->count = 1;
		if (t->left != NULL) 
			t->count += t->left->count; 
		if (t->right != NULL) 
			t->count += t->right->count; 
	return t;
}
struct Unit* Delete(struct Unit *t, int k) 
{
	struct Unit *x = t;
	if (k < t->key) 
		t->left = Delete(t->left, k);
	else if (k > t->key) 
		t->right = Delete(t->right, k);
	else {
		if (t->right == NULL) 
			return t->left;
		if (t->left == NULL) 
			return t->right;
		t = Minimum(x->right);
		t->right = DeleteMinimum(x->right);
		t->left = x->left;
	}
	t->count = 1;
		if (t->left != NULL) 
			t->count += t->left->count; 
		if (t->right != NULL) 
			t->count += t->right->count; 
	return t;
}
struct Unit* Search1(struct Unit *t, long k)
{
	long a;
	if (t->left == NULL)
		a = 0;
	else
		a = t->left->count;
	if (a > k) 
		return Search1(t->left, k);
	else if (a < k) 
		return Search1(t->right, k-a-1);
	else 
		return t;
}
void Search(struct Unit *t, long k)
{
	struct Unit *x = Search1(t, k);
	printf("%s\n", x->word);
}
void Free(struct Unit *t)
{
	if (t->left != NULL)
		Free(t->left);
	if (t->right != NULL)
		Free(t->right);
	free(t);
}
int main(int argc, char **argv)
{
	root = NULL;	
	long i, n, k, q = 0;
	scanf("%ld", &n);
	struct Unit *mas[n];
	char v[9];
	for(i = 0; i < n; i++) {
		scanf("%s %ld", v, &k);
		if (v[0] == 'I') {
			scanf("%s", v);
			root = Insert(root, k, v, q, mas);
			q++;
			continue;
		}
		if (v[0] == 'L') {
			Lookup(root, k);
			continue;
		}
		if (v[0] == 'D') {
			root = Delete(root, k);
			continue;
		}
		if (v[0] == 'S')
			Search(root, k);
	}
	
	//printf("%ld - %s -- %ld\n", root->left->key, root->left->word, root->left->count);
	//printf("%ld - %s -- %ld\n", root->right->key, root->right->word, root->right->count);
	//Free(root);
	//free(root);
	for(i = 0; i < q; i++) {
		root = mas[i];
		free(root);
	}
	//printf("%s -- %ld\n", root->word, root->count);
	return 0;
}
