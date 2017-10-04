% ConOps
% Patrice Desrochers, Gildo Conte, Audrey Eugène
% 2017-09-17

# Liste des acronymes et abréviation {-}
IA
: Intelligence artificielle


# Historique des révisions {-}

| **Date**   | **Version** | **Description**  | **Auteur**                                                                           |
| :--------- | :---------: | :--------------- | :----------------------------------------------------------------------------------- |
| 2017-09-17 |     0.1     | Version initiale | Patrice Desrochers, Gildo Conte, Audrey Eugène |

# Préface {-}


# Introduction

## Identification du système

Le système a pour nom A.L.I.C.E. et il est en version 0.1.

## Objectifs du document

Communiquer les besoins et attentes des usagers du système, des développeurs ou d'un acheteur potentiel du système. ce document s'adresse aux :

-	Usagers : Des participants actifs développement personnel du système. Ce document tentera de décrire leurs besoins de façon fidèle.
-	Acheteurs : Pour tout acheteur potentiel, ce document permettra de mieux comprendre les besoins des usagers et le but du système.
-	Développeurs : Le document vise aussi à laisser une trace des besoins des usagers pour le développement futur de l'application.

## Vue d'ensemble du système

A.L.I.C.E. a pour but principal d'offrir une expérience interactive et communicative pour des usagers de tous les âges et d'intérêts variés. En plus de sa facette divertissante, le système contient aussi plusieurs buts secondaires, qui motivent son développement :

- Faire avancer les technologies associées à la problématique des intelligences artificielles
- Fournir un outil de psychologie avancé, en rendant le système communicatif et en lui permettant de développer une personnalité
- Faire participer les internautes; la personnalité et l'intelligence du système doit se développer grâce à un grand nombre d'usagers

Le développement de ce système n'est pas commandité. Il est, à sa conception, développé par Patrice Desrochers, Gildo Conte et Audrey Eugène. A.L.I.C.E. est une application qui fonctionnera sur n'importe quel ordinateur Windows, MacOS et Linux.

![Diagramme de contexte](./image/contextdiagram.png)

# Références

https://www.existor.com/products/cleverbot-data-for-machine-learning/

https://fr.wikipedia.org/wiki/AIML

https://en.wikipedia.org/wiki/Cleverbot

# Le système actuel

Il existe plusieurs intelligences artificielles sur le marché dont le but principal est d'imiter le dialogue avec un individu. Ces logiciels sont communément appelés « Chatterbot ». Nous prendrons ici l'exemple de Cleverbot, une IA qui a été programmée en 1988, puis mise en ligne en 1997. Cleverbot est une application web qui permet d'entretenir une conversation avec une intelligence artificielle. Celle-ci apprend à répondre en fonction des réponses de ses utilisateurs.

## Contexte, objectifs et portée du système actuel

### Portée

Cleverbot maintient une conversation en cherchant dans sa base de données les réponses qu'elle a reçues pour une question en particulier. Ainsi, elle peut reproduire le « flow » d'une conversation et apprendre des expressions ainsi que des phrases pré-construites. Cependant, Cleverbot n'est pas une entité a part entière, puisqu'elle ne fait qu'apprendre à imiter les humains avec qui elle interagit. Elle n'a pas de préférence ou d'opinion, elle n'a aucun concept du soi, et elle requiert une question pour pouvoir répondre : elle ne pose aucune question par elle-même.

## Politiques opérationnelles et contraintes

Étant donnée que Cleverbot est une application web, elle fonctionne 24 heures sur 24, 7 jours sur 7 et peut fonctionner sur n'importe quel système. Aussi, la quantité maximale de personnes connectées en simultané dépend du serveur sur lequel l'application est hébergée et non l'application elle-même.

## Description du système actuel

Les fonctionnalités du système actuel sont :

- Chatterbot
: une IA qui "comprends" et qui répond aux phrases qu'on lui dit.
- Sauvegarde de la plus récente conversation
- Réinitialisation d'une conversation pour recommencer à 0
- Possibilité de laisser l'IA choisir un sujet de conversation
- Possibilité de faire l'IA réfléchir sur le dernier sujet discuté

## Les modes d'opération du système

Un seul mode d'opération (connu) existe à l'usage de Cleverbot, soit le mode régulier, qui permet de maintenir une conversation avec l'intelligence artificielle.

## Les classes d'utilisateurs et les autres personnes impliqués

Une seule classe d'utilisateur existe à l'usage de Cleverbot, soit le locuteur, qui intéragit avec l'intelligence artificielle.

### La structure organisationnelle

Les locuteurs ne sont pas en relation et ne communiquent pas entre eux. Leur seule interaction est avec l'application. Cette interaction encourage l'apprentissage de l'intelligence artificielle, qui améliore ensuite l'expérience de tous les utilisateurs.

### Le profil de chaque classe d'utilisateurs

- Le locuteur est définit par une entité capable de fournir des entrées de texte à l'intelligence artificielle. La nature du locuteur est sans conséquence : celui-ci peut-être un individu en chair et en os, ou une autre intelligence artificielle, pour autant qu'il fournit une entrée de texte que Cleverbot peut lire. Le locuteur intéragit avec l'application et permet à l'application d'apprendre et de mieux répondre aux prochaines entrées qui lui seront envoyées. De cette façon, le locuteur est à la fois un consommateur du service et un outil de maintenance passif, profitant du divertissement procuré en entretenant un dialogue avec l'IA, mais fournissant aussi des données qui permettront l'amélioration du service.

### Les interactions entre les utilisateurs

S/O

### Autre personnel impliqué



## L'environnement de support

Cleverbot doit être supporter par un serveur ainsi qu'une liaison vers internet. Il n'y a pas de maitenance, puisque l'application est une machine qui apprends en fonction des gens avec qui elle interagit.

# La justification et la nature des changements

## Justification des changements


## Description des changements

Les changements apporté à l'application sont que A.L.I.C.E. va être une personnalité à part entière qui va pouvoir interagir avec comme un être humain, mais elle va pouvoir aussi lancer une conversation avec la personne. Donc, non seulement l'application va recueillir les réponses pour mieux répondre aux autres personnes, elle va aussi déterminé quel sujet sont apprécié ou rejeté par l'utilisateur.

## L'ordre de priorité dans les changements

## Les changements considérés mais non-inclus

## Hypothèses et contraintes

# Le concept du système proposé
