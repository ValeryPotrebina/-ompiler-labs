KW_NONTERM -> non-terminal
KW_TERM -> terminal
KW_AXIOM -> axiom
KW_EPSILON -> epsilon
NONTERM -> [A-Z][A-Z0-9]*
TERM -> [a-z0-9]|'\S'
SEMICOLON -> ;
COMMA -> ,
EQUAL -> ::=
OR -> |

#Грамматика

S -> KW_NONTERM NontermList SEMICOLON KW_TERM TermList SEMICOLON Rules Axiom NONTERM
NontermList -> Nonterm NontermList2
NontermList2 -> COMMA Nonterm NontermList2 | e
TermList -> Term TermList2
TermList2 -> COMMA TermList TermList | e
Rules -> Rule Rules
Rule -> NonTerm EQUAL RuleBody SEMICOLON
RuleBody -> Alter RuleBody2
RuleBody2 -> OR Alter RuleBody2 | e
Alter -> Token Alter2 | KW_EPSILON
Alter2 -> Token Alter2 | e
Token -> Term | NonTerm


Follow(Alter2) = Follow(alter) = First(RuleBody2) = {OR} | Follow(RuleBody2) = {OR} | Follow(RuleBody) = {OR, SEMICOLON}


# Табличка 
f(S, KW_NONTERM) = KW_NONTERM NontermList SEMICOLON KW_TERM TermList SEMICOLON Rules Axiom NONTERM
f(S, ...) = ERROR
f(NontermList, NONTERM) = NONTERM NontermList2
f(NontermList, ...) = ERROR
f(NontermList2, COMMA) = COMMA NONTERM NontermList2
f(NontermList2, SEMICOLON) = e
f(NontermList2, ...) = ERROR
f(TermList, TERM) = TERM TermList2
f(TermList, ...) = ERROR
f(TermList2, COMMA) = COMMA TERM TermList2
f(TermList2, SEMICOLON) = e
f(TermList2, ...) = ERROR
f(Rules, NONTERM) = Rule Rules
f(Rules, ...) = ERROR
f(Rule, NONTERM) = NONTERM EQUAL RuleBody SEMICOLON
f(Rule, ...) = ERROR
f(RuleBody, TERM) = f(RuleBody, NONTERM) = f(RuleBody, KW_EPSILON) = Alter RuleBody2
f(RuleBody, ...) = ERROR
f(RuleBody2, OR) = OR Alter RuleBody2
f(RuleBody2, SEMICOLON) = e
f(RuleBody2, ...) = ERROR
f(Alter, TERM) = f(Alter, NONTERM) = Token Alter2
f(Alter, KW_EPSILON) = KW_EPSILON
f(Alter, ...) = ERROR
f(Alter2, TERM) = f(Alter2, NONTERM) = Token Alter2
f(Alter2, OR) = f(Alter2, SEMICOLON) = e
f(Alter2, ...) = ERROR
f(Token, TERM) = TERM
f(Token, NONTERM) = NONTERM
f(Token, ...) = ERROR
