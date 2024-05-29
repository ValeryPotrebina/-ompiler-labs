# Лабораторная работа № 2.4.    
29.05.2024

# Цель работы


# Индивидуальный вариант


# Грамматика

Терминал - не раскрывается
Нетерминал = раскрывается в псоледовательность нетерм и термим

# Абстрактный синтаксис

Specification => 
    '%class' ClassName '%tokens' Tokens '%types' TypesDefs '%methods' MethodDefs '%grammar' RulesList '%axiom' Axiom '%end'

IDENT => [a-zA-Z][a-zA-Z0-9]*
Type=> IDENT | IDENT[]


ClassName => IDENT

Tokens => Token*
Token => IDENT

TypesDefs => TypeDef*
TypeDef => TypeContent ':' Type ';'
TypeContent => IDENT ',' ... ',' IDENT


MethodDefs => MethodDef*
MethodDef => MethodType MethodName '(' MethodArgs ')' ';'
MethodType => Type
MethodName => IDENT
MethodArgs => Type ',' ... ',' Type

RulesList => Rule*
Rule => RuleHead '=' RuleBody ';'
RuleHead => IDENT 
RuleBody => Alter '|' ... '|' Alter
Alter => AlterContent AlterMethod?
AlterContent => AlterElement*
AlterElement => ElemRep? ElemContent
ElemContent => IDENT | '(' RuleBody ')'
ElemRep => '%rep'
AlterMethod => '/' IDENT

Axiom => IDENT

# Грамматика


IDENT => [a-zA-Z][a-zA-Z0-9]*
ARR_TYPE =? '[]'
LPAR => '('
RPAR => ')'
COLON => ':'
SEMICOLON => ';'
COMMA => ','
SLASH => '/'
OR => '|'
EQUAL => '='
KW_CLASS -> '%class'
KW_TOKENS -> '%tokens'
KW_TYPES -> '%types'
KW_METHODS -> '%methods'
KW_GRAMMAR -> '%grammar'
KW_AXIOM -> '%axiom'
KW_REP -> '%rep'
KW_END -> '%end'

Specification =>
KW_CLASS ClassName KW_TOKENS Tokens KW_TYPES TypesDefs KW_METHODS MethodDefs KW_GRAMMAR RulesList KW_AXIOM Axiom KW_END

ClassName => IDENT

Type=> IDENT | IDENT ARR_TYPE
Tokens => Tokens Token | e
Token => IDENT

TypesDefs => TypeDefs TypeDef | e
TypeDef => TypeContent COLON Type SEMICOLON
TypeContent => TypeContent COMMA IDENT | IDENT

MethodDefs => MethodDefs MethodDef | e
MethodDef => MethodType MethodName LPAR MethodArgs RPAR SEMICOLON
MethodType => Type
MethodName => IDENT
MethodArgs =>  MethodArgsList | e
MethodArgsList => MethodArgsList COMMA Type | Type

RulesList => RulesList Rule | e
Rule => RuleHead EQUAL RuleBody SEMICOLON
RuleHead => IDENT
RuleBody => RuleBody Alter | Alter
Alter => AlterContent AlterMethod
AlterMethod => SLASH IDENT | e
AlterContent => AlterContent AlterElement | e
AlterElement => ElemRep ElemContent
ElemRep => KW_REP | e
ElemContent => IDENT OR LPAR RuleBody RPAR


Axiom => IDENT