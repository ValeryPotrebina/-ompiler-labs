%option noyywrap bison-bridge bison-locations



%{

#include <stdio.h>

#include <stdlib.h>

#include <string>

#include <vector>

#include <map>



#define TAG_IDENT 1

#define TAG_CHAR 2

#define TAG_Z 3

#define TAG_FOR 4

#define TAG_FORWARD 5



char *tag_names[] = {

        "END_OF_PROGRAMM" , "IDENT" , "CHAR" , "Z", "FOR", "FORWARD"

};



struct Position {

    int line, pos;

};



void print_pos(const struct Position *p) {

    printf("(%d, %d)", p->line, p->pos);

}



struct Fragment {

    struct Position starting , following;

};



typedef struct Fragment YYLTYPE;



void print_frag(struct Fragment *f) {

    print_pos(&(f->starting));

    printf(" - ");

    print_pos(&(f->following));

}



union Token {

    int code;

    char* value;

};



typedef union Token YYSTYPE;



struct Error {

    struct Position pos;

    const char *msg;

};



void print_err(struct Error *err) {

    printf("Error ");

    print_pos(&(err->pos));

    printf(": %s", err->msg);

}



std::vector<Error> errors;



std::map<int, std::string> codeName;

std::map<std::string, int> nameCode;



struct Position cur;



# define YY_USER_ACTION {                   \

    int i;                                  \

    yylloc->starting = cur;                 \

    for (i = 0; i < yyleng; i++) {          \

        if (yytext[i] == '\n') {            \

            cur.line++;                     \

            cur.pos = 1;                    \

        } else {                            \

            cur.pos++;                      \

        }                                   \

    }                                       \

    yylloc->following = cur;                \

}



void init_scanner(const char *program) {

    cur.line = 1;

    cur.pos = 1;

    yy_scan_string(program);

}

void err(const char * msg) {

    struct Error err;

    err.pos = cur;

    err.msg = msg;

    errors.push_back(err);

}



%}



LETTER [a-zA-Z]

DIGIT [0-9]

HEX [a-fA-F0-9]

IDENT {LETTER}({LETTER}|{DIGIT}){0,8}{LETTER}

Z  z|Z

FOR (FOR)|(for)

FORWARD (forward)|(FORWARD)

CHAR '([^'\\\n]|(\\({HEX}{4}|[n'\\])))'



%x FOR FORWARD Z IDENT CHAR



%%

[\t\r\n ]+

{Z} {

    return TAG_Z;

}

{FORWARD} {

    return TAG_FORWARD;

}

{FOR} {

    return TAG_FOR;

}

{IDENT} {

    int id = codeName.size();

    if(nameCode.find(yytext) != nameCode.end()){

        id = nameCode[yytext];

        yylval->code = id;

        return TAG_IDENT;

    }

    nameCode[yytext] = id;

    codeName[id] = yytext;

    yylval->code = id;

    return TAG_IDENT;

}

{CHAR} {

    yylval->value = yytext;

    return TAG_CHAR;

}

. err("Unexpected token");



%%



int main (int argc, char **argv) {

    FILE *f;

    if (argc != 2) {

        printf("Usage: out <inputPath>");

    }

    if ((f = fopen(argv[1], "rb")) == NULL) {

        printf("Can't open file\n");

        return -1;

    }

    fseek(f, 0, SEEK_END);

    const int size = ftell(f);

    fseek(f, 0, SEEK_SET);

    char program[size+1];

    if (fread(program, sizeof(char), size, f) != size) {

        printf("Can't read file\n");

        free(program);

        fclose(f);

        return -2;

    }

    program[size] = '\0';

    int tag;

    YYSTYPE value;

    YYLTYPE coords;

    init_scanner(program);

    while(1) {

        tag = yylex(&value, &coords);

        printf("%-6s ", tag_names[tag]);

        if (tag == 0) {

            break;

        }

        print_frag(&coords);

        switch(tag){

            case 1: printf(": (%d, %s)", value.code, 

codeName[value.code].c_str()); break;

            case 2: printf(": %s", value.value); break;

            case 3:

            case 4:

            case 5: break;

        }

        printf("\n");

    }

    printf("\n\n");

    for(int i = 0; i < errors.size(); i++){

        print_err(&errors[i]);

        printf("\n");

    }

    free(program);

    fclose(f);

    

    return 0;

}