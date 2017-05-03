%option noyywrap bison-bridge bison-locations
%{
# include <stdio.h>
# include <string.h>
# include <stdlib.h>
# define TAG_EOF 0
# define TAG_IDENT 1
# define TAG_COMMENT 2
# define TAG_KEYWORD 3
# define TAG_ERROR 4


char *tag_names[] ={
"END_OF_PROGRAM", "IDENT", "COMMENT", "KEYWORD"
};

struct Position{
    int line, pos, index;
};

void print_pos (struct Position *p){
    printf ("(%d, %d)", p->line, p->pos);
}

struct Fragment{
    struct Position starting, following;
};

typedef struct Fragment YYLTYPE;

void print_frag(struct Fragment *f){
    print_pos(&(f->starting));
    printf("-");
    print_pos(&(f->following));
}

union Token{
    char *ident;
    char *keyword;
    char *comment;
};
typedef union Token YYSTYPE;

int continued;
long size_comment;
struct Position cur;
#define YY_USER_ACTION {              \
    int i;                           \
    if (!continued)                  \
        yylloc->starting = cur;      \
    continued = 0;                   \
    for ( i = 0; i < yyleng; i++){   \
        if ( yytext[i] == '\n'){     \
            cur.line++;              \
            cur.pos = 1;             \
        }                            \
        else                         \
            cur.pos++;               \
        cur.index++;                 \
    }                                \
    yylloc->following = cur;         \
} 

void init_scanner (char *program){
    continued = 0;
    cur.line = 1;
    cur.pos = 1;
    cur.index = 0;
    yy_scan_string(program);
}

void err(char *msg ){
    printf ("Error");
    print_pos (&cur);
    printf (": %s\n" , msg);
}
%}

KEYWORD (ifif|do|dodo)
IDENT [A-Za-z]{2,}


%x COMMENT

%%
[\n\t ]+
\(\*                  {
                            BEGIN(COMMENT);
                            continued = 1;
                            size_comment = 3;
                            yylval->comment=(char*)malloc(size_comment*sizeof(char));
                            strcpy(yylval->comment,"(*");
                      }
\{                    {
                            BEGIN(COMMENT);
                            continued = 1;
                            size_comment = 2;
                            yylval->comment=(char*)malloc(size_comment*sizeof(char));
                            strcpy(yylval->comment,"{");
                      }

<COMMENT>[^\*\}]*     {
                            continued = 1;
                            size_comment += yyleng;
                            yylval->comment=(char*)realloc(yylval->comment,size_comment*sizeof(char));
                            strcat(yylval->comment,yytext);
                      }
<COMMENT>\*           {
                            size_comment += 1;
                            yylval->comment=(char*)realloc(yylval->comment,size_comment*sizeof(char));
                            strcat(yylval->comment,"*");
                      }
<COMMENT>\*\)         {
                            size_comment += 2;
                            yylval->comment=(char*)realloc(yylval->comment,size_comment*sizeof(char));
                            strcat(yylval->comment,"*)");
                            BEGIN(0);
                            return TAG_COMMENT;
                      }

<COMMENT>\}           {
                            size_comment += 1;
                            yylval->comment=(char*)realloc(yylval->comment,size_comment*sizeof(char));
                            strcat(yylval->comment,"}");
                            BEGIN(0);
                            return TAG_COMMENT;
                      }
<COMMENT><<EOF>>      {
                            err("end of program found, *) or } expected");
                            return 0;
                      }
{KEYWORD}             {
                            yylval->keyword = yytext;
                            return TAG_KEYWORD;
                      }
{IDENT}               {
                            if (strlen(yytext) % 2 != 0) {
                            err ("DOESN'T palindrome");
                            return TAG_ERROR;
                            } else {
                                int len = strlen(yytext);
                                for (int i = 0; i != len/2; i++) {
                                    if (yytext[i] != yytext[len/2 + i]) {
                                        err ("DOESN'T palindrome");
                                        return TAG_ERROR;
                                    }
                                }
                                yylval->ident = yytext;
                                return TAG_IDENT;
                            }
                      }
.                     err ("ERROR");
<<EOF>>               return 0;
   
%%
int main(int argc, char *argv[]){
    int tag;
    YYSTYPE value;
    YYLTYPE coords;
    FILE *inputfile;
    long size_str;
    char *str;
    union Token token;
    inputfile = fopen( "test.txt" , "rb" );
    if (inputfile==NULL){
        fputs ("File error",stderr);
        exit (1);
    }
    fseek(inputfile, 0,SEEK_END);
    size_str = ftell(inputfile);
    rewind(inputfile);

    str=(char*)malloc(sizeof(char)*(size_str+1));
    if (str == NULL) {fputs ("Memory error",stderr); exit (2);}
    
    size_t n = fread(str,sizeof(char),size_str,inputfile);
    if (n != size_str) {fputs ("Reading error",stderr); exit (3);}
    str[size_str] = '\0';
    fclose (inputfile);

    init_scanner(str);
    do{
        tag = yylex(&value,&coords);
        if (tag != 0 && tag != 4){
           printf(" %s",tag_names[tag]);
           print_frag(&coords);
           printf(" %s\n", value.ident);
           
           
        }
    }
    while (tag!=0); 
    free(str);
    return 0;
}


